/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.exception.control.extension;

import org.jboss.seam.exception.control.ExceptionHandlerComparator;
import org.jboss.seam.exception.control.HandlerMethod;
import org.jboss.seam.exception.control.HandlerMethodImpl;
import org.jboss.seam.exception.control.Handles;
import org.jboss.seam.exception.control.HandlesExceptions;
import org.jboss.weld.extensions.reflection.HierarchyDiscovery;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * CDI extension to find handlers at startup.
 */
@SuppressWarnings({"unchecked"})
public class CatchExtension implements Extension
{
   private final Map<? super Type, Collection<AnnotatedMethod>> allHandlers;

   public CatchExtension()
   {
      this.allHandlers = new HashMap<Type, Collection<AnnotatedMethod>>();
   }

   /**
    * Listener to ProcessManagedBean event to locate handlers.
    *
    * @param pmb Event from CDI SPI
    * @param bm  Activated Bean Manager
    *
    * @throws TypeNotPresentException if any of the actual type arguments refers to a non-existent type declaration when
    *                                 trying to obtain the actual type arguments from a {@link ParameterizedType}
    * @throws java.lang.reflect.MalformedParameterizedTypeException
    *                                 if any of the
    *                                 actual type parameters refer to a parameterized type that cannot
    *                                 be instantiated for any reason when trying to obtain the actual type arguments
    *                                 from a {@link ParameterizedType}
    */
   public void findHandlers(@Observes final ProcessManagedBean pmb, final BeanManager bm)
   {
      final AnnotatedType type = pmb.getAnnotatedBeanClass();

      if (type.isAnnotationPresent(HandlesExceptions.class))
      {
         final Set<AnnotatedMethod> methods = type.getMethods();

         for (AnnotatedMethod method : methods)
         {
            if (method.getParameters().size() > 0
                && ((AnnotatedParameter) method.getParameters().get(0)).isAnnotationPresent(Handles.class))
            {
               final AnnotatedParameter p = (AnnotatedParameter) method.getParameters().get(0);
               final Class exceptionType = (Class) ((ParameterizedType) p.getBaseType()).getActualTypeArguments()[0];

               if (this.allHandlers.containsKey(exceptionType))
               {
                  this.allHandlers.get(exceptionType).add(method);
               }
               else
               {
                  this.allHandlers.put(exceptionType, new HashSet<AnnotatedMethod>(Arrays.asList(method)));
               }
            }
         }
      }
   }

   /**
    * Obtains the applicable handlers for the given type or super type of the given type.  Also makes use of
    * {@link org.jboss.seam.exception.control.ExceptionHandlerComparator} to order the handlers.
    *
    * @param exceptionClass    Type of exception to narrow handler list
    * @param bm                active BeanManager
    * @param handlerQualifiers additional handlerQualifiers to limit handlers
    *
    * @return An order collection of handlers for the given type.
    */
   public Collection<HandlerMethod> getHandlersForExceptionType(Type exceptionClass, BeanManager bm,
                                                                Set<Annotation> handlerQualifiers)
   {
      final Set<HandlerMethod> returningHandlers = new TreeSet<HandlerMethod>(new ExceptionHandlerComparator());
      final HierarchyDiscovery h = new HierarchyDiscovery(exceptionClass);
      final Set<Type> closure = h.getTypeClosure();

      for (Type hierarchyType : closure)
      {
         if (this.allHandlers.get(hierarchyType) != null)
         {
            for (AnnotatedMethod handler : this.allHandlers.get(hierarchyType))
            {
               HandlerMethod handlerMethod = new HandlerMethodImpl(handler, bm);
               if (handlerQualifiers.isEmpty() && handlerMethod.getQualifiers().isEmpty())
               {
                  returningHandlers.add(handlerMethod);
               }
               else
               {
                  if (!handlerQualifiers.isEmpty() && handlerMethod.getQualifiers().containsAll(handlerQualifiers))
                  {
                     returningHandlers.add(handlerMethod);
                  }
               }
            }
         }
      }

      return Collections.unmodifiableCollection(returningHandlers);
   }
}

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
package org.jboss.seam.exception.control;

import org.jboss.seam.exception.control.config.CatchConfig;
import org.jboss.seam.exception.control.config.ExceptionTypeConfig;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Finds and invokes all {@link org.jboss.seam.exception.control.ExceptionHandler} instants for a particular exception and {@link org.jboss.seam.exception.control.State}. <p> If any handlers are
 * found and invoked the the {@link org.jboss.seam.exception.control.ExceptionEvent#setExceptionHandled(boolean)} is set to true. </p>
 */
public class ExceptionHandlerExecutor
{
   /**
    * Observes the event, finds the correct exception handler(s) and invokes them.
    *
    * @param handlingException
    * @param config
    * @param beanManager
    */
   @SuppressWarnings({"unchecked", "MethodWithMultipleLoops", "NestedAssignment", "ThrowableResultOfMethodCallIgnored"})
   public void executeHandlers(@Observes @Any final Throwable handlingException, final CatchConfig config,
                               final BeanManager beanManager)
   {
      final Stack<Throwable> unwrappedExceptions = new Stack<Throwable>();
      Throwable currentException = handlingException;

      MethodParameterTypeHelper handlerMethodParameters;

      do
      {
         unwrappedExceptions.push(currentException);
      }
      while ((currentException = currentException.getCause()) != null);

      // Finding the correct exception handlers using reflection based on the method
      // to determine if it's the correct
      while (!unwrappedExceptions.isEmpty())
      {
         currentException = unwrappedExceptions.pop();

         for (ExceptionHandlerChain chain : config.getRegistry().getExceptionHandlerChainsFor(currentException))
         {
            if (chain.isActive(currentException))
            {
               for (Object h : chain.getExceptionHandlers()) // complete generic fail
               {
                  ExceptionHandler handler = null;
                  try
                  {
                     handler = (ExceptionHandler) ((Class) h).newInstance();

                     if (handler instanceof ConfigurableExceptionHandler)
                     {
                        ((ConfigurableExceptionHandler) handler).initialize();
                     }
                  }
                  catch (InstantiationException e)
                  {
                     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                  }
                  catch (IllegalAccessException e)
                  {
                     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                  }

                  if (handler instanceof ExceptionTypeConfig)
                  {

                  }
               }
            }
         }
         for (ExceptionHandler handler : this.allHandlers)
         {
            handlerMethodParameters = new MethodParameterTypeHelper(handler);

            if (handlerMethodParameters.containsExceptionTypeOrSuperType(unwrapped.getClass())
                && handlerMethodParameters.containsStateTypeOrSuperType(state.getClass()))
            {
               handler.handle(chain, state, unwrapped);
               event.setExceptionHandled(true);

               if (((HandlerChainImpl) chain).isChainEnd())
               {
                  break;
               }
            }
         }
      }
   }

   /**
    * Finds all instances of {@link org.jboss.seam.exception.control.ExceptionHandler} and creates contextual instances for use.
    * <p/>
    * Method taken from Seam faces BeanManagerUtils.
    *
    * @param manager BeanManager instance
    *
    * @return List of instantiated  found by the bean manager
    */
   @SuppressWarnings("unchecked")
   @Produces
   public List<ExceptionHandler> getContextualExceptionHandlerInstances(BeanManager manager)
   {
      List<ExceptionHandler> result = new ArrayList<ExceptionHandler>();
      for (Bean<?> bean : manager.getBeans(ExceptionHandler.class))
      {
         CreationalContext<ExceptionHandler> context =
            (CreationalContext<ExceptionHandler>) manager.createCreationalContext(
               bean);
         if (context != null)
         {
            result.add((ExceptionHandler) manager.getReference(bean, ExceptionHandler.class, context));
         }
      }

      Collections.sort(result, new ExceptionHandlerComparator());

      return result;
   }
}

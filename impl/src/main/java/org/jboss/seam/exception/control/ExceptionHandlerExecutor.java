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

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Finds and invokes all {@link org.jboss.seam.exception.control.ExceptionHandler}
 * instants for a particular exception and {@link org.jboss.seam.exception.control.State}.
 */
public class ExceptionHandlerExecutor
{
   @Inject
   private List<ExceptionHandler> allHandlers;

   /**
    * Observes the event, finds the correct exception handler(s) and invokes them.
    *
    * @param event Event Payload
    *
    * @throws Throwable Either the return of ExceptionEvent#getException if it was not handled, explicitly asked to be
    *                   re-thrown from a handler, or a new exception to throw from a handler.
    */
   @SuppressWarnings({"unchecked", "MethodWithMultipleLoops", "ThrowableResultOfMethodCallIgnored", "NestedAssignment"})
   public void executeHandlers(@Observes ExceptionEvent event) throws Throwable
   {
      final HandlerControl chain = new HandlerChainImpl();
      final Stack<Throwable> unwrappedExceptions = new Stack<Throwable>();
      final State state = event.getState();
      final BeanManager beanManager = state.getBeanManager();

      Throwable exception = event.getException();
      MethodParameterTypeHelper handlerMethodParameters;

      do
      {
         unwrappedExceptions.push(exception);
      }
      while ((exception = exception.getCause()) != null);

      // Finding the correct exception handlers using reflection based on the method
      // to determine if it's the correct

      boolean handled = false;
      while (!unwrappedExceptions.isEmpty())
      {
         final Throwable unwrapped = unwrappedExceptions.pop();
         for (ExceptionHandler handler : this.allHandlers)
         {
            handlerMethodParameters = new MethodParameterTypeHelper(handler);

            if (handlerMethodParameters.containsExceptionTypeOrSuperType(unwrapped.getClass())
                && handlerMethodParameters.containsStateTypeOrSuperType(state.getClass()))
            {
               handler.handle(chain, state, unwrapped); // TODO: This will return an object
               handled = true;

               if (((HandlerChainImpl) chain).isChainEnd()) // TODO: inspect and run the returned object
               {
                  break;
               }
            }
         }
      }

      if (!handled)
      {
         throw event.getException();
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

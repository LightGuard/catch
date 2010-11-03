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

import org.jboss.seam.exception.control.extension.CatchExtension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 */
public class ExceptionHandlerExecutor
{
   @Inject
   private CatchExtension extension;

   /**
    * Observes the event, finds the correct exception handler(s) and invokes them.
    */
   @SuppressWarnings({"unchecked", "MethodWithMultipleLoops", "ThrowableResultOfMethodCallIgnored"})
   public void executeHandlers(@Observes Throwable eventException)
   {
      final Stack<Throwable> unwrappedExceptions = new Stack<Throwable>();

      Throwable exception = eventException;
      MethodParameterTypeHelper handlerMethodParameters;

      do
      {
         unwrappedExceptions.push(exception);
      }
      while ((exception = exception.getCause()) != null);

      // Finding the correct exception handlers using reflection based on the method
      // to determine if it's the correct
      int indexOfException = 0;
      while (indexOfException < unwrappedExceptions.size())
      {
         ExceptionHandlingEvent ehe = new ExceptionHandlingEvent(new StackInfo(unwrappedExceptions, indexOfException));

         List<AnnotatedMethod> handlerMethods = new ArrayList<AnnotatedMethod>(this.extension.getHandlers()
            .get(unwrappedExceptions.get(indexOfException)));

         // TODO: Sort handlerMethods

         for (AnnotatedMethod handler : handlerMethods)
         {
            // TODO: Get bean from AnnotatedMethod, create bean, call method

            // TODO: Make sure things like mute are handled
         }

         // TODO rollbacks, throws, etc

         indexOfException++;
      }
   }
}

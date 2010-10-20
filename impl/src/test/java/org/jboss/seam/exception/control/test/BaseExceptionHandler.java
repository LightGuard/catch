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

package org.jboss.seam.exception.control.test;

import org.jboss.seam.exception.control.ExceptionHandlerOutcome;
import org.jboss.seam.exception.control.Outcome;
import org.jboss.seam.exception.control.State;
import org.joda.time.DateTime;

public class BaseExceptionHandler
{
   protected boolean handleCalled;
   protected boolean callEnd;
   protected DateTime timeCalled;

   public boolean isHandleCalled()
   {
      return this.handleCalled;
   }

   public void shouldCallEnd(boolean callEnd)
   {
      this.callEnd = callEnd;
   }

   public DateTime getTimeCalled()
   {
      return this.timeCalled;
   }

   /**
    * Method called to execute logic for an uncaught exception.
    *
    * @param state container for any useful application state
    * @param e     uncaught exception
    */
   public ExceptionHandlerOutcome baseHandle(State state, Throwable e)
   {
      this.timeCalled = new DateTime();
      this.handleCalled = true;

      if (this.callEnd)
      {
         return new ExceptionHandlerOutcome(Outcome.ABORT);
      }

      return null;
   }
}

/*
 * JBoss, Home of Professional Open Source
 *  Copyright 2010, Red Hat, Inc., and individual contributors
 *  by the @authors tag. See the copyright.txt in the distribution for a
 *  full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.exception.control;

/**
 * Information about the desired outcome of a {@link org.jboss.seam.exception.control.ExceptionHandler}. This class
 * is immutable, as such it is also thread safe.
 */
public final class ExceptionHandlerOutcome
{
   private final Outcome outcome;
   private final Throwable exception;

   /**
    * Basic Constructor.
    *
    * @param outcome Desired outcome.
    */
   public ExceptionHandlerOutcome(Outcome outcome)
   {
      this(outcome, null);
   }

   /**
    * Basic Constructor.
    *
    * @param outcome   Desired outcome.
    * @param exception Exception to be thrown. If exception is null, the exception from
    *                  {@link ExceptionEvent#getException()} will be used
    *
    * @throws IllegalArgumentException if an exception is given and
    *                                  {@link this#isThrow} returns false for outcome
    */
   public ExceptionHandlerOutcome(Outcome outcome, Throwable exception)
   {
      this.outcome = outcome;
      this.exception = exception;

      if (this.exception != null && !this.isThrow())
      {
         throw new IllegalArgumentException("This outcome should not throw an exception");
      }

   }

   /**
    * @return true if this represents an outcome to continue processing handlers, false otherwise
    */
   public boolean isContinue()
   {
      switch (this.outcome)
      {
         case CONTINUE:
         case CONTINUE_ROLLBACK_ONLY:
            return true;
         default:
            return false;
      }
   }

   /**
    * @return true if this represents an outcome to abort processing handlers, false otherwise
    */
   public boolean isAbort()
   {
      switch (this.outcome)
      {
         case ABORT_AND_ROLLBACK_AND_THROW:
         case ABORT_AND_ROLLBACK:
         case ABORT_AND_THROW:
         case ABORT:
            return true;
         default:
            return false;
      }
   }

   /**
    * @return true if this represents an outcome to rollback the current transaction, false otherwise
    */
   public boolean isRollback()
   {
      switch (this.outcome)
      {
         case ABORT_AND_ROLLBACK_AND_THROW:
         case ABORT_AND_ROLLBACK:
         case CONTINUE_ROLLBACK_ONLY:
            return true;
         default:
            return false;
      }
   }

   /**
    * @return true if this represents an outcome to throw an exception, false otherwise
    */
   public boolean isThrow()
   {
      switch (this.outcome)
      {
         case ABORT_AND_ROLLBACK_AND_THROW:
         case ABORT_AND_THROW:
            return true;
         default:
            return false;
      }
   }

   public Throwable getException()
   {
      return exception;
   }

   public Outcome getOutcome()
   {
      return outcome;
   }
}

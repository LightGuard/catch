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
 * Possible outcomes for an {@link org.jboss.seam.exception.control.ExceptionHandlerOutcome}.
 */
public enum Outcome
{
   /**
    * Continue processing other handlers
    */
   CONTINUE,
   /**
    * Continue processing other handlers, but rollback the current transaction
    */
   CONTINUE_ROLLBACK_ONLY,
   /**
    * Abort further processing of handlers and throw an exception
    */
   ABORT_AND_THROW,
   /**
    * Abort further processing of handlers and rollback the current transaction
    */
   ABORT_AND_ROLLBACK,
   /**
    * Abort further processing of handlers, rollback the current transaction, and throw an exception
    */
   ABORT_AND_ROLLBACK_AND_THROW,
   /**
    * Simply abort further processing
    */
   ABORT
}

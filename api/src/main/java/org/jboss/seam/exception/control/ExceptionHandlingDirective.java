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
 * An enum indicating how the status of the exception handling
 * after giving a handler a shot at dealing with it.
 *
 * @author Dan Allen
 */
public enum ExceptionHandlingDirective
{
   /**
    * Proceed with normal execution, which means notifying
    * each handler in the chains for this exception type
    * in sequence, then popping to the exception cause
    * and repeating until flow ends or is interrupted.
    */
   PROCEED,

   /**
    * Consider the exception handled and do not notify
    * any more handlers.
    */
   HANDLED,

   /**
    * Stop notifying handlers for the current exception,
    * pop to the exception cause and begin notifying
    * the handlers for that exception.
    */
   POP_STACK
}

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

/**
 * Provides the methods to affect the traversal of the chain of
 * {@link org.jboss.seam.exception.control.ExceptionHandler} instances and actions taken after the chain of handlers
 * has executed such as transaction rollback.
 */
public interface HandlerControl
{
   /**
    * Tells the chain to continue processing the remaining handlers.
    */
   void proceed();

   /**
    * Instructs the current transaction to be rolled back after the chain of handlers has been processed.
    */
   void queueTransactionRollback();

   /**
    * Instructs the exception to be re-thrown after the chain of handlers has been processed.
    */
   void queueExceptionRethrow();
}

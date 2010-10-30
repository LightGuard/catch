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

package org.jboss.seam.exception.control.config;

import org.jboss.seam.exception.control.ExceptionHandler;
import org.jboss.seam.exception.control.ExceptionHandlerChain;

public class ExceptionTypeConfig<T extends Throwable> extends CatchConfig
{
   protected final Class<T> exceptionType;

   public ExceptionTypeConfig(ExceptionHandlerRegistry registry)
   {
      this(registry, null);
   }

   public ExceptionTypeConfig(ExceptionHandlerRegistry registry, Class<T> exceptionType)
   {
      super(registry);
      this.exceptionType = exceptionType;
   }

   // using ? super T here causes a warning to appear about casting a vararg array to a generic array
   // however, it does provide us safety against using handlers that aren't parameterized to the exception hierarchy
   public ExceptionHandlerChainDef<T> handleWith(Class<ExceptionHandler<? super T>> handler)
   {
      ExceptionHandlerChain<T> chain = new ExceptionHandlerChain<T>(handler);
      if (exceptionType != null)
      {
         registry.addChainForExceptionType(exceptionType, chain);
      }
      else
      {
         registry.addGlobalChain(chain);
      }

      return new ExceptionHandlerChainDef<T>(this, chain);
   }
}

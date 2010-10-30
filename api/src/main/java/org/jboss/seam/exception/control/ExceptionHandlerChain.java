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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExceptionHandlerChain<T extends Throwable>
{
   private ArrayList<Class<ExceptionHandler<? super T>>> handlers;

   private List<ExceptionHandlerActivation> activations = new ArrayList<ExceptionHandlerActivation>();

   public ExceptionHandlerChain(Class<ExceptionHandler<? super T>>... handlers)
   {
      this.handlers = new ArrayList<Class<ExceptionHandler<? super T>>>(Arrays.asList(handlers));
   }

   public void addAll(ExceptionHandlerActivation... activations)
   {
      this.activations.addAll(Arrays.asList(activations));
   }

   public boolean isActive(Throwable t)
   {
      for (ExceptionHandlerActivation a : activations)
      {
         if (!a.isActive(t))
         {
            return false;
         }
      }
      return true;
   }

   public List<Class<ExceptionHandler<? super T>>> getExceptionHandlers()
   {
      return Collections.unmodifiableList(this.handlers);
   }

   public List<ExceptionHandlerActivation> getActivations()
   {
      return Collections.unmodifiableList(this.activations);
   }
}

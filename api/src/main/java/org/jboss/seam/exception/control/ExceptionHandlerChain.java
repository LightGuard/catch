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

// TODO needs generic parameters to the effect of <T extends Throwable> and <H extends ExceptionHandler<? super T>>
public class ExceptionHandlerChain<T extends Throwable, H extends ExceptionHandler<? super T>>
{
   private List<H> handlers;

   private List<ExceptionHandlerActivation> activations = new ArrayList<ExceptionHandlerActivation>();

   public ExceptionHandlerChain(H ... handlers)
   {
      this.handlers = new ArrayList<H>(Arrays.<? extends H>asList(handlers));
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

   public List<H> getExceptionHandlers()
   {
      return Collections.unmodifiableList(handlers);
   }

   public List<ExceptionHandlerActivation> getActivations()
   {
      return Collections.unmodifiableList(activations);
   }
}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.exception.control.ExceptionHandlerChain;

public class ExceptionHandlerRegistry
{
   private List<ExceptionHandlerChain> globalChains;
   private Map<Class<? extends Throwable>, List<ExceptionHandlerChain>> chainsByExceptionType;

   public ExceptionHandlerRegistry()
   {
      globalChains = new ArrayList<ExceptionHandlerChain>();
      chainsByExceptionType = new HashMap<Class<? extends Throwable>, List<ExceptionHandlerChain>>();
   }

   void addGlobalChain(ExceptionHandlerChain chain)
   {
      globalChains.add(chain);
   }

   void addChainForExceptionType(Class<? extends Throwable> type, ExceptionHandlerChain chain)
   {
      List<ExceptionHandlerChain> chains;
      if (!chainsByExceptionType.containsKey(type))
      {
         chains = new ArrayList<ExceptionHandlerChain>();
         chainsByExceptionType.put(type, chains);
      }
      else
      {
         chains = chainsByExceptionType.get(type);
      }
      chains.add(chain);
   }

   public List<ExceptionHandlerChain> getExceptionHandlerChainsFor(Throwable t)
   {
      List<ExceptionHandlerChain> chains = new ArrayList<ExceptionHandlerChain>();
      chains.addAll(globalChains);
      if (chainsByExceptionType.containsKey(t.getClass()))
      {
         chains.addAll(chainsByExceptionType.get(t.getClass()));
      }
      else
      {
         // huge hack to find more general exception types
         Class<?> superType = t.getClass().getSuperclass();
         while (superType != null)
         {
            if (chainsByExceptionType.containsKey(superType))
            {
               chains.addAll(chainsByExceptionType.get(superType));
               break;
            }
            superType = superType.getSuperclass();
         }
      }
      return Collections.unmodifiableList(chains);
   }
}

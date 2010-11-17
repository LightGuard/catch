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

package org.jboss.seam.exception.control.example.jaxrs;

import org.jboss.seam.exception.control.example.jaxrs.handler.CatchBridge;
import org.jboss.seam.exception.control.example.jaxrs.resource.AuthorResource;
import org.jboss.seam.exception.control.example.jaxrs.resource.BookResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/*")
public class LibraryApplication extends Application
{
   @Override
   public Set<Class<?>> getClasses()
   {
      final Set<Class<?>> classes = new HashSet<Class<?>>();
      classes.addAll(Arrays.asList(CatchBridge.class, AuthorResource.class, BookResource.class));

      return classes;
   }
}

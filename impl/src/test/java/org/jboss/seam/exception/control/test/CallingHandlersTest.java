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

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.exception.control.CatchEvent;
import org.jboss.seam.exception.control.extension.CatchExtension;
import org.jboss.seam.exception.control.test.handler.CalledExceptionHandler;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class CallingHandlersTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      return ShrinkWrap.create(JavaArchive.class)
         .addPackage(CatchEvent.class.getPackage())
         .addClasses(CalledExceptionHandler.class, CatchExtension.class)
         .addManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension")
         .addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject
   private BeanManager bm;

   @Test
   public void assertOutboundHanldersAreCalled()
   {
      bm.fireEvent(new IllegalArgumentException());

      assertTrue(CalledExceptionHandler.OUTBOUND_HANDLER_CALLED);
   }

   @Test
   public void assertOutboundHanldersAreCalledOnce()
   {
      CalledExceptionHandler.OUTBOUND_HANDLER_TIMES_CALLED = 0;
      bm.fireEvent(new IllegalArgumentException());
      assertEquals(1, CalledExceptionHandler.OUTBOUND_HANDLER_TIMES_CALLED);
   }

   @Test
   public void assertInboundHanldersAreCalledOnce()
   {
      CalledExceptionHandler.INBOUND_HANDLER_TIMES_CALLED = 0;
      bm.fireEvent(new IllegalArgumentException());
      assertEquals(1, CalledExceptionHandler.INBOUND_HANDLER_TIMES_CALLED);
   }
}

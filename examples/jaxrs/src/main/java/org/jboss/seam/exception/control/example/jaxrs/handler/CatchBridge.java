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

package org.jboss.seam.exception.control.example.jaxrs.handler;

import org.jboss.seam.exception.control.ExceptionToCatchEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class CatchBridge implements ExceptionMapper<Throwable>
{
//   @Inject @RestCatch
//   private Response.ResponseBuilder responseBuilder;

   @Inject Event<ExceptionToCatchEvent> event;

   @Inject BeanManager bm;

   public Response toResponse(Throwable exception)
   {
      final Class<Response.ResponseBuilder> responseBuilderType = Response.ResponseBuilder.class;

      final Bean<?> bean = this.bm.resolve(this.bm.getBeans(responseBuilderType, RestCatchLiteral.INSTANCE));
      final CreationalContext<?> ctx = this.bm.createCreationalContext(bean);
      final Response.ResponseBuilder responseBuilder = (Response.ResponseBuilder) this.bm.getReference(bean,
                                                                                                       responseBuilderType,
                                                                                                       ctx);

      this.bm.fireEvent(new ExceptionToCatchEvent(exception, RestCatchLiteral.INSTANCE));
//      event.fire(new ExceptionToCatchEvent(exception, RestCatchLiteral.INSTANCE));
      return responseBuilder.build();
   }
}

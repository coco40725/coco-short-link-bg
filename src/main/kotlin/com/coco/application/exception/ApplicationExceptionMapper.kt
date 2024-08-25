package com.coco.application.exception

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

/**
@author Yu-Jing
@create 2024-08-24-下午 01:07
 */

@ApplicationScoped
class ApplicationExceptionMapper {

    @ServerExceptionMapper(ApplicationException::class)
    fun handleApplicationException(e: ApplicationException): Response {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(e.message)
            .build()
    }
}
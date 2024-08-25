package com.coco.infra.exception

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

/**
@author Yu-Jing
@create 2024-08-24-下午 05:47
 */

@ApplicationScoped
class GrpcExceptionMapper {

    @ServerExceptionMapper(GrpcConnectionException::class)
    fun handleGrpcConnectionException(exception: GrpcConnectionException): Response {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(exception.message)
            .build()
    }
}
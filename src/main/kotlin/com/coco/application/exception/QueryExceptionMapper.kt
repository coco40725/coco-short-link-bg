package com.coco.application.exception

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

/**
@author Yu-Jing
@create 2024-08-24-下午 01:07
 */

@ApplicationScoped
class QueryExceptionMapper {
    @ServerExceptionMapper(NoSuchQueryException::class)
    fun handleNoSuchQueryException(exception: NoSuchQueryException): Response {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(QueryHandlerCastException::class)
    fun handleQueryHandlerCastException(exception: QueryHandlerCastException): Response {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(QueryValidationException::class)
    fun handleQueryValidationException(exception: QueryValidationException): Response {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(exception.message)
            .build()
    }

}
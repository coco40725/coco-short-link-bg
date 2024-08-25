package com.coco.application.exception

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

/**
@author Yu-Jing
@create 2024-08-24-下午 01:07
 */

@ApplicationScoped
class CommandExceptionMapper {
    @ServerExceptionMapper(NoSuchCommandException::class)
    fun handleNoSuchCommandException(exception: NoSuchCommandException): Response {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(CommandHandlerCastException::class)
    fun handleCommandHandlerCastException(exception: CommandHandlerCastException): Response {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(exception.message)
            .build()
    }
    @ServerExceptionMapper(CommandValidationException::class)
    fun handleCommandValidationException(exception: CommandValidationException): Response {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(exception.message)
            .build()
    }
}
package com.coco.infra.exceptionMapper

import com.coco.application.cqrs.command.base.CommandHandlerCastException
import com.coco.application.cqrs.command.base.CommandValidationException
import com.coco.application.cqrs.command.base.NoSuchCommandException
import com.coco.application.cqrs.query.base.NoSuchQueryException
import com.coco.application.cqrs.query.base.QueryHandlerCastException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

/**
@author Yu-Jing
@create 2024-08-11-下午 05:14
 */

@ApplicationScoped
class ExecutorExceptionMapper {

    @ServerExceptionMapper(NoSuchQueryException::class)
    fun handleNoSuchQueryException(exception: NoSuchQueryException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(QueryHandlerCastException::class)
    fun handleQueryHandlerCastException(exception: QueryHandlerCastException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(NoSuchCommandException::class)
    fun handleNoSuchCommandException(exception: NoSuchCommandException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(CommandHandlerCastException::class)
    fun handleCommandHandlerCastException(exception: CommandHandlerCastException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }
    @ServerExceptionMapper(CommandValidationException::class)
    fun handleCommandValidationException(exception: CommandValidationException): Response {
        return Response
            .status(422)
            .entity(exception.message)
            .build()
    }

}
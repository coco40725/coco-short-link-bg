package com.coco.infra.exceptionMapper

import com.coco.application.service.exception.CheckShortLinkIsExpiredException
import com.coco.infra.repo.exception.LinkInfoInsertException
import com.coco.infra.repo.exception.LinkInfoUpdateException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

/**
@author Yu-Jing
@create 2024-08-11-下午 05:25
 */
@ApplicationScoped
class LinkInfoExceptionMapper {
    @ServerExceptionMapper(LinkInfoInsertException::class)
    fun handleLinkInfoInsertException(exception: LinkInfoInsertException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(LinkInfoUpdateException::class)
    fun handleLinkInfoUpdateException(exception: LinkInfoUpdateException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }

    fun handleCheckShortLinkIsExpiredException(exception: CheckShortLinkIsExpiredException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }
}
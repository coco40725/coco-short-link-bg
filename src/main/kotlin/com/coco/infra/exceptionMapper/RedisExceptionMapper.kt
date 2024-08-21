package com.coco.infra.exceptionMapper

import com.coco.infra.repo.exception.RedisCacheDeleteFailedException
import com.coco.infra.repo.exception.RedisCacheSetFailedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

/**
@author Yu-Jing
@create 2024-08-11-下午 05:15
 */

@ApplicationScoped
class RedisExceptionMapper {

    @ServerExceptionMapper(RedisCacheSetFailedException::class)
    fun handleRedisCacheUpdateFailedException(exception: RedisCacheSetFailedException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }

    @ServerExceptionMapper(RedisCacheDeleteFailedException::class)
    fun handleRedisCacheDeleteFailedException(exception: RedisCacheDeleteFailedException): Response {
        return Response
            .status(500)
            .entity(exception.message)
            .build()
    }
}
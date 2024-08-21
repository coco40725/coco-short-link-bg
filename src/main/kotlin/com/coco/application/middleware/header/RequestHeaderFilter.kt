package com.coco.application.middleware.header

import com.coco.domain.vo.RequestHeaderData
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.ext.Provider

/**
@author Yu-Jing
@create 2024-08-10-下午 10:46
 */

@Provider
@GetHeader
class RequestHeaderFilter: ContainerRequestFilter {

    @Inject
    lateinit var requestHeaderData: RequestHeaderData

    override fun filter(requestContext: ContainerRequestContext?) {
        requestHeaderData.init(requestContext)
    }
}
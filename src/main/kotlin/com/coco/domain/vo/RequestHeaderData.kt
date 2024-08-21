package com.coco.domain.vo

import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.container.ContainerRequestContext

/**
@author Yu-Jing
@create 2024-08-10-下午 10:50
 */
@RequestScoped
class RequestHeaderData(
    var userAgent: String? = null,
    var referer: String? = null,
    var refererIP: String? = null,
    var host: String? = null,
    var requestUrl: String? = null,
) {
    fun init(requestContext: ContainerRequestContext?) {
        val header = requestContext?.headers
        val uriInfo = requestContext?.uriInfo

        // get client ip
        val ip = requestContext?.getHeaderString("X-Real-IP")
            ?: requestContext?.getHeaderString("X-Forwarded-For")

        userAgent = header?.get("User-Agent")?.firstOrNull()
        referer =  header?.get("Referer")?.firstOrNull()
        refererIP = ip
        host = header?.get("Host")?.firstOrNull()
        requestUrl = uriInfo?.requestUri.toString()

    }
}
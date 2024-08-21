package com.coco.application.ctrl

import com.coco.application.cqrs.DefaultActionExecutor
import com.coco.application.cqrs.query.base.QueryValidationException
import com.coco.application.cqrs.query.getUserLinkStat.GetUserLinkStatQuery
import com.coco.application.cqrs.query.getUserLinkStat.GetUserLinkStatResult
import com.coco.application.cqrs.query.getUserShortLinkInfo.GetUserShortLinkInfoQuery
import com.coco.application.cqrs.query.getUserShortLinkInfo.GetUserShortLinkInfoResult
import com.coco.application.middleware.auth.JwtRequest
import com.coco.application.middleware.auth.Logged
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam

/**
@author Yu-Jing
@create 2024-08-15-下午 02:36
 */

@Path("/user")
class UserLinkInfoCtrl @Inject constructor(
    private val executor: DefaultActionExecutor,
    private val jwt: JwtRequest,
){

    @GET
    @Path("/link-info")
    @Logged
    fun getUserShortLinkInfo(): Uni<GetUserShortLinkInfoResult?> {
        val query = GetUserShortLinkInfoQuery(jwt)
        return executor.validateQuery(query).chain { result ->
            val isValid = result.isValid
            val message = result.message
            if (isValid) {
                executor.executeQuery(query, result)
            } else {
                throw QueryValidationException("${query::class.java.name} validator fail; cause by : ${message}")
            }
        }
    }

    @GET
    @Path("/link-stat")
    @Logged
    fun getUserShortLinkStat(@QueryParam("shortLink") shortLink: String): Uni<GetUserLinkStatResult?> {
        val query = GetUserLinkStatQuery(shortLink)
        return executor.executeQuery(query)
    }
}
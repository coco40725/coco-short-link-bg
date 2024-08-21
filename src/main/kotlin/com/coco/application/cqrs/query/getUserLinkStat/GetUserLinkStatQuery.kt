package com.coco.application.cqrs.query.getUserLinkStat

import com.coco.application.cqrs.query.base.Query
import com.coco.application.middleware.auth.JwtRequest
import io.smallrye.mutiny.Uni

/**
@author Yu-Jing
@create 2024-08-16-下午 05:22
 */
data class GetUserLinkStatQuery(
    var shortLink: String,
    var jwt: JwtRequest? = null,
): Query<Uni<GetUserLinkStatResult?>>

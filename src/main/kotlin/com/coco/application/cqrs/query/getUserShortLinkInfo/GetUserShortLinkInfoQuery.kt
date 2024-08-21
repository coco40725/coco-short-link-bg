package com.coco.application.cqrs.query.getUserShortLinkInfo

import com.coco.application.cqrs.query.base.Query
import com.coco.application.middleware.auth.JwtRequest
import io.smallrye.mutiny.Uni

/**
@author Yu-Jing
@create 2024-08-14-上午 11:32
 */
data class GetUserShortLinkInfoQuery(
    var jwt: JwtRequest? = null
): Query<Uni<GetUserShortLinkInfoResult?>>

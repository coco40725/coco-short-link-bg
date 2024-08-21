package com.coco.application.cqrs.query.getUserLinkStat

import com.coco.application.cqrs.query.base.QueryHandler
import com.coco.application.cqrs.query.base.QueryValidateResult
import com.coco.application.service.UserLinkStatManagementService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
@author Yu-Jing
@create 2024-08-16-下午 05:22
 */

@ApplicationScoped
class GetUserLinkStatHandler @Inject constructor(
    private val userLinkStatManagementService: UserLinkStatManagementService
): QueryHandler<Uni<GetUserLinkStatResult?>, GetUserLinkStatQuery> {
    override fun handle(
        query: GetUserLinkStatQuery,
        validateResult: QueryValidateResult?
    ): Uni<GetUserLinkStatResult?> {
        val linkStat = userLinkStatManagementService.getLinkInfoStats(query.shortLink)
        val result = GetUserLinkStatResult(
            totalCount = linkStat.totalCount,
            shortLink = linkStat.shortLink,
            referCount = linkStat.referCount,
            ipCount = linkStat.ipCount,
            userAgentCount = linkStat.userAgentCount,
            createDate = linkStat.createDate
        )
        return Uni.createFrom().item(result)
    }
}

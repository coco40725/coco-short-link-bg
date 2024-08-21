package com.coco.application.cqrs.query.getUserShortLinkInfo

import com.coco.application.cqrs.query.base.QueryHandler
import com.coco.application.cqrs.query.base.QueryValidateResult
import com.coco.application.service.UserLinkStatManagementService
import com.coco.domain.model.LinkInfo
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
@author Yu-Jing
@create 2024-08-14-上午 11:32
 */

@ApplicationScoped
class GetUserShortLinkInfoHandler @Inject constructor(
    private val userLinkStatManagementService: UserLinkStatManagementService,

): QueryHandler<Uni<GetUserShortLinkInfoResult?>, GetUserShortLinkInfoQuery> {

    override fun handle(
        query: GetUserShortLinkInfoQuery,
        validateResult: QueryValidateResult?
    ): Uni<GetUserShortLinkInfoResult?> {
        val validResult = validateResult as GetUserShortLinkInfoValidateResult
        val userInfo = validResult.user !!
        val userId = userInfo.id
        return userLinkStatManagementService.getUserLinkInfo(userId).map { infoList ->
            val (enabledLinkInfo, disabledLinkInfo) = infoList.partition { it.enabled == true }
            GetUserShortLinkInfoResult(
                userId = userId,
                enabledShortLinkInfo = enabledLinkInfo.map { toLinkInfoData(it) },
                disabledShortLinkInfo = disabledLinkInfo.map { toLinkInfoData(it) }
            )
        }
    }

    private fun toLinkInfoData(linkInfo: LinkInfo): GetUserShortLinkInfoResult.LinkInfoData {
        return GetUserShortLinkInfoResult.LinkInfoData(
            id = linkInfo.id.toString(),
            shortLink = linkInfo.shortLink,
            originalLink = linkInfo.originalLink,
            expirationDate = linkInfo.expirationDate,
            createDate = linkInfo.createDate
        )
    }
}

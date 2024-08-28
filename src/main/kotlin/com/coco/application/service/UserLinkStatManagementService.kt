package com.coco.application.service

import com.coco.domain.model.LinkInfo
import com.coco.domain.model.LinkLog
import com.coco.domain.vo.LinkStat
import com.coco.infra.bigQuery.ShortLinkBigQuery
import com.coco.infra.repo.LinkInfoRepo
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-下午 08:12
 */

@ApplicationScoped
class UserLinkStatManagementService @Inject constructor(
    private val linkInfoRepo: LinkInfoRepo,
    private val shortLinkBigQuery: ShortLinkBigQuery
) {

    fun getUserLinkInfo(userId: String): Uni<List<LinkInfo>> {
        return linkInfoRepo.getManyByUserId(null, userId)
    }

    fun getQueryResult(shortLink: String) {
        shortLinkBigQuery.getDataByShortLink(shortLink)
    }

    fun getLinkInfoStats(shortLink: String): LinkStat {
        val logs = shortLinkBigQuery.getDataByShortLink(shortLink)
        val totalCount = logs.size
        val referCountMap = mutableMapOf<String, Int>()
        val ipCountMap = mutableMapOf<String, Int>()
        val userAgentCountMap = mutableMapOf<String, Int>()

        logs.forEach { log ->
            val refer = log.referer
            val ip = log.refererIP
            val userAgent = log.userAgent
            refer?.let { it ->
                referCountMap[it] = referCountMap.getOrDefault(refer, 0) + 1
            }
            ip?.let { it ->
                ipCountMap[it] = ipCountMap.getOrDefault(ip, 0) + 1
            }
            userAgent?.let { it ->
                userAgentCountMap[it] = userAgentCountMap.getOrDefault(userAgent, 0) + 1
            }
        }

        return LinkStat(
            shortLink = shortLink,
            totalCount = totalCount,
            referCount = referCountMap,
            ipCount = ipCountMap,
            userAgentCount = userAgentCountMap,
            createDate = Date()
        )
    }

}
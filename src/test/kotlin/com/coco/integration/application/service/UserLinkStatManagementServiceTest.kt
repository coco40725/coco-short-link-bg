package com.coco.integration.application.service

import com.coco.application.service.UserLinkStatManagementService
import com.coco.domain.model.LinkLog
import com.coco.infra.bigQuery.ShortLinkBigQuery
import com.mongodb.assertions.Assertions.assertTrue
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-05-下午 05:09
 */
@QuarkusTest
class UserLinkStatManagementServiceTest {
    private val shortLinkBigQuery = mockk<ShortLinkBigQuery>()

    @BeforeEach
    fun beforeEach() {
        QuarkusMock.installMockForType(shortLinkBigQuery, ShortLinkBigQuery::class.java)
    }

    @Inject
    lateinit var userLinkStatManagementService: UserLinkStatManagementService

    @Test
    fun `test zero log return stat data`(){
        every { shortLinkBigQuery.getDataByShortLink(any()) } returns emptyList()
        val result = userLinkStatManagementService.getLinkInfoStats("shortLink")
        assertTrue(result.shortLink == "shortLink")
        assertTrue(result.totalCount == 0)
        assertTrue(result.referCount.isEmpty())
        assertTrue(result.ipCount.isEmpty())
        assertTrue(result.userAgentCount.isEmpty())
    }

    @Test
    fun `test multiple logs return stat data`(){
        val logs = listOf(
            LinkLog(
                shortLink = "shortLink",
                referer = "referer1",
                refererIP = "ip1",
                userAgent = "userAgent1"
            ),
            LinkLog(
                shortLink = "shortLink",
                referer = "referer2",
                refererIP = "ip2",
                userAgent = "userAgent2"
            ),
            LinkLog(
                shortLink = "shortLink",
                referer = "referer1",
                refererIP = "ip1",
                userAgent = "userAgent1"
            )
        )
        val referCountMap = mutableMapOf("referer1" to 2, "referer2" to 1)
        val ipCountMap = mutableMapOf("ip1" to 2, "ip2" to 1)
        val userAgentCountMap = mutableMapOf("userAgent1" to 2, "userAgent2" to 1)
        every { shortLinkBigQuery.getDataByShortLink(any()) } returns logs
        val result = userLinkStatManagementService.getLinkInfoStats("shortLink")
        assertTrue(result.shortLink == "shortLink")
        assertTrue(result.totalCount == logs.size)
        assertTrue(result.referCount == referCountMap)
        assertTrue(result.ipCount == ipCountMap)
        assertTrue(result.userAgentCount == userAgentCountMap)

    }
}
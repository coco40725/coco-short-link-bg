package com.coco.unit.domain.service

import com.coco.domain.service.linkInfo.LinkInfoSvc
import com.coco.domain.service.linkInfo.ShortUrlGenerator
import com.coco.infra.config.WebConfig
import com.mongodb.assertions.Assertions.assertFalse
import com.mongodb.assertions.Assertions.assertTrue
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest

import org.junit.jupiter.api.Test
import java.util.*

/**
@author Yu-Jing
@create 2024-08-28-下午 10:50
 */

@QuarkusTest
class LinkInfoSvcTest {

    private val webConfig = mockk<WebConfig>()
    private val shortUrlGenerator = mockk<ShortUrlGenerator>()
    private val linkInfoSvc = LinkInfoSvc(shortUrlGenerator, webConfig)

    @Test
    fun `test isShortLinkFormatValid with valid short link`() {
        every { webConfig.websiteDomain() } returns "https://www.example.com"
        val shortLink = "https://www.example.com/abc123_-"
        assertTrue(linkInfoSvc.isShortLinkFormatValid(shortLink))
    }

    @Test
    fun `test isShortLinkFormatValid with invalid short link containing special characters`() {
        every { webConfig.websiteDomain() } returns "https://www.example.com"
        val shortLink = "https://www.example.com/abc!@#"
        assertFalse(linkInfoSvc.isShortLinkFormatValid(shortLink))
    }

    @Test
    fun `test isShortLinkFormatValid with invalid short link exceeding 20 characters`() {
        every { webConfig.websiteDomain() } returns "https://www.example.com"
        val shortLink = "https://www.example.com/abc123_-abc123_-abc123_-"
        assertFalse(linkInfoSvc.isShortLinkFormatValid(shortLink))
    }

    @Test
    fun `test isOriginalLinkFormatValid with valid original link`() {
        val originalLink = "https://www.example.com"
        assertTrue(linkInfoSvc.isOriginalLinkFormatValid(originalLink))
    }

    @Test
    fun `test isOriginalLinkFormatValid with invalid original link exceeding 500 characters`() {
        val originalLink = "https://www.example.com".repeat(100)
        assertFalse(linkInfoSvc.isOriginalLinkFormatValid(originalLink))
    }

    @Test
    fun `test isOriginalLinkFormatValid with invalid original link`() {
        val originalLink = ""
        assertFalse(linkInfoSvc.isOriginalLinkFormatValid(originalLink))
    }

    @Test
    fun `test isExpirationDateValid with valid expiration date`() {
        val expirationDate = Date(System.currentTimeMillis() + 1000)
        assertTrue(linkInfoSvc.isExpirationDateValid(expirationDate))
    }

    @Test
    fun `test isExpirationDateValid with invalid expiration date`() {
        val expirationDate = Date(System.currentTimeMillis() - 100000)
        assertFalse(linkInfoSvc.isExpirationDateValid(expirationDate))
    }

}

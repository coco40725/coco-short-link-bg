package com.coco.unit.domain.service

import com.coco.domain.service.linkInfo.impl.Base62ShortUrlGenerator
import com.coco.infra.config.WebConfig
import com.mongodb.assertions.Assertions.assertTrue
import com.mongodb.assertions.Assertions.fail
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.URL

/**
@author Yu-Jing
@create 2024-08-28-下午 10:50
 */
@QuarkusTest
class Base62ShortUrlGeneratorTest {

    private val webConfig = mockk<WebConfig>()

    @Test
    fun `test generateShortUrl generate 0 size return empty`() {
        every { webConfig.websiteDomain() } returns "http://localhost:8080"
        val size = 0
        val generator = Base62ShortUrlGenerator(webConfig)
        val shortUrl = generator.generateShortUrl(size)
        assertTrue(shortUrl.isEmpty())
    }

    @Test
    fun `test generateShortUrl generate -1 size return empty`(){
        every { webConfig.websiteDomain() } returns "http://localhost:8080"
        val size = -1
        val generator = Base62ShortUrlGenerator(webConfig)
        val shortUrl = generator.generateShortUrl(size)
        assertTrue(shortUrl.isEmpty())
    }

    @Test
    fun `test generateShortUrl generate valid unique urls with specific size`(){
        every { webConfig.websiteDomain() } returns "http://localhost:8080"
        val size = 10000
        val randomValueSize = 7
        val generator = Base62ShortUrlGenerator(webConfig)
        val shortUrl = generator.generateShortUrl(size)

        assertTrue(shortUrl.size == size)
        assertTrue(shortUrl.toSet().size == size)

        shortUrl.forEach { it ->
            try {
                val url = URI.create(it)
                val randomValue = it.split( webConfig.websiteDomain() + "/")[1]
                assertTrue(randomValue.length == randomValueSize)
            } catch (e: Exception) {
                fail("Invalid URL generated: $it")
            }

        }
    }
}
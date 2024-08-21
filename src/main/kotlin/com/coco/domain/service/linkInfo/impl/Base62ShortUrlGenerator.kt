package com.coco.domain.service.linkInfo.impl

import com.coco.domain.service.linkInfo.ShortUrlGenerator
import com.coco.infra.config.WebConfig
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import kotlin.random.Random

/**
@author Yu-Jing
@create 2024-08-12-上午 09:31
 */

/**
 * generate a unique short url which is 7 characters long
 * 1. generate a random number between 0 and 3521614606207
 * 2. convert the number to base62
 */
@Named("base62")
@ApplicationScoped
class Base62ShortUrlGenerator @Inject constructor(
    private val webConfig: WebConfig
): ShortUrlGenerator {
    private val maxNumber = 3521614606207
    override fun generateShortUrl(size: Int): List<String> {
        val shortUrls = mutableListOf<String>()
        val domain = webConfig.websiteDomain()
        for (i in 0 until size) {
            val randomNum = Random.nextLong(0, maxNumber)
            val randomStr = base10ToBase62(randomNum)
            val shortLink = "$domain/$randomStr"
            shortUrls.add(shortLink)
        }
        return shortUrls
    }

    private fun base10ToBase62(num: Long): String {
        val base62Chars = ('0'..'9') + ('A'..'Z') + ('a'..'z')
        if (num == 0L) return "0000000"
        val sb = StringBuilder()
        var n = num
        while (n > 0) {
            val remainder = (n % 62).toInt()
            sb.append(base62Chars[remainder])
            n /= 62
        }
        return sb.reverse().toString().padStart(7, '0')
    }
}


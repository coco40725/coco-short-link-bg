package com.coco.domain.service.linkInfo

import com.coco.infra.config.WebConfig
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import java.net.URI
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-下午 10:11
 */

@ApplicationScoped
class LinkInfoSvc @Inject constructor(
    @Named("base62") private val shortUrlGenerator: ShortUrlGenerator,
    private val webConfig: WebConfig,
){
    fun isShortLinkFormatValid(shortLink: String): Boolean {
        var isValid = true
        val removeDomainShortLink = shortLink.replace("${webConfig.websiteDomain()}/", "")
        // rule 1: short link should only contain 0-9, a-z, A-Z, -, _
        if (!removeDomainShortLink.matches(Regex("^[0-9a-zA-Z_-]*$"))) {
            isValid = false
        }

        // rule 2: short link should not exceed 20 characters
        if (removeDomainShortLink.length > 20) {
            isValid = false
        }
        return isValid
    }

    fun isOriginalLinkFormatValid(originalLink: String): Boolean {
        var isValid = true
        // rule 1: original link should not be empty
        if (originalLink.isEmpty()) {
            isValid = false
        }

        // rule 2: original link should less than 500  characters
        if (originalLink.length > 500 ) {
            isValid = false
        }

        // rule 3: original link should be a valid  url
        if (!isUrlValid(originalLink)) {
            isValid = false
        }
        return isValid
    }

    fun isExpirationDateValid(expirationDate: Date): Boolean {
        return expirationDate.after(Date())
    }

    fun generateShortUrl(size: Int): List<String> {
        return shortUrlGenerator.generateShortUrl(size)
    }

    private fun isUrlValid(url: String): Boolean {
        return try {
            URI.create(url)
            true
        } catch (e: Exception) {
            false
        }
    }
}
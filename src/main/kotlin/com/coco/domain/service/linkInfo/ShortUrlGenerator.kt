package com.coco.domain.service.linkInfo

/**
@author Yu-Jing
@create 2024-08-11-下午 10:18
 */

interface ShortUrlGenerator {
    fun generateShortUrl(size: Int): List<String>
}
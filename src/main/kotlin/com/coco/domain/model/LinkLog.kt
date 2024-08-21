package com.coco.domain.model


import org.bson.types.ObjectId
import java.util.*

/**
@author Yu-Jing
@create 2024-08-10-下午 05:16
 */
data class LinkLog(
    var id: String? = null,
    var shortLink: String? = null,
    var refererIP: String? = null,
    var userAgent: String? = null,
    var referer: String? = null,
    var createDate: Date? = null,
)

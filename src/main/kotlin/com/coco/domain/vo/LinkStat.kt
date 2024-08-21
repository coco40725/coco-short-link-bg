package com.coco.domain.vo

import org.bson.types.ObjectId
import java.util.*

/**
@author Yu-Jing
@create 2024-08-10-下午 05:13
 */
data class LinkStat(
    var totalCount: Int,
    var shortLink: String,
    var referCount: Map<String, Int>,
    var ipCount: Map<String, Int>,
    var userAgentCount: Map<String, Int>,
    var createDate: Date,
)

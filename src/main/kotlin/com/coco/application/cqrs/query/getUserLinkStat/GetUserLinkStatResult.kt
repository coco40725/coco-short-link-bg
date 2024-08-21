package com.coco.application.cqrs.query.getUserLinkStat

import java.util.*

/**
@author Yu-Jing
@create 2024-08-16-下午 05:22
 */
data class GetUserLinkStatResult(
    var totalCount: Int,
    var shortLink: String,
    var referCount: Map<String, Int>,
    var ipCount: Map<String, Int>,
    var userAgentCount: Map<String, Int>,
    var createDate: Date,
)

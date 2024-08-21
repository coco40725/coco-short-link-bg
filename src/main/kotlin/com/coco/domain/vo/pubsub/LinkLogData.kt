package com.coco.domain.vo.pubsub

import com.coco.domain.vo.RequestHeaderData

/**
@author Yu-Jing
@create 2024-08-11-上午 10:01
 */
data class LinkLogData(
    var shortLink: String = "",
    var requestHeader: RequestHeaderData? = null
)

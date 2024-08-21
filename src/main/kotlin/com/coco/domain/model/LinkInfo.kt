package com.coco.domain.model

import org.bson.types.ObjectId
import java.util.*

/**
@author Yu-Jing
@create 2024-08-10-下午 04:50
 */
data class LinkInfo(
    var id: ObjectId? = null,
    var shortLink: String? = null,
    var userId: String? = null,
    var originalLink: String? = null,
    var expirationDate: Date? = null,
    var lastUpdateDate: Date? = null,
    var createDate: Date? = null,
    var enabled: Boolean? = null,
){

}
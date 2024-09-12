package com.coco.application.cqrs.command.addLinkInfo

import org.bson.types.ObjectId
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-上午 11:04
 */
data class AddLinkInfoResult(
    var id: String? = null,
    var shortLink: String? = null,
    var userId: String? = null,
    var originalLink: String? = null,
    var createDate: Date? = null,
    var expirationDate: Date? = null
)

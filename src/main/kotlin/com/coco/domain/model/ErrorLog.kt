package com.coco.domain.model

import org.bson.Document
import java.util.*

/**
@author Yu-Jing
@create 2024-08-27-下午 08:17
 */
data class ErrorLog(
    var functionName: String,
    var param: List<Document>,
    var createDate: Date,
)

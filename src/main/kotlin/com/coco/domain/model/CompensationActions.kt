package com.coco.domain.model

import io.smallrye.mutiny.Uni
import org.bson.Document

/**
@author Yu-Jing
@create 2024-08-27-下午 05:50
 */
data class CompensationActions(
    var functionName: String,
    var params: List<Document>,
    var action: () -> Uni<*>
)

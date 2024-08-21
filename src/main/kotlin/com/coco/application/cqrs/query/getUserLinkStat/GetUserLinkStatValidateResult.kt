package com.coco.application.cqrs.query.getUserLinkStat

import com.coco.application.cqrs.query.base.QueryValidateResult
import com.coco.domain.model.User

/**
@author Yu-Jing
@create 2024-08-15-下午 04:32
 */
data class GetUserLinkStatValidateResult(
    override var isValid: Boolean = true,
    override var message: List<String>? = null,
    var user: User? = null

): QueryValidateResult(isValid, message)

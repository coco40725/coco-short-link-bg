package com.coco.application.cqrs.command.addLinkInfo

import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.domain.model.User

/**
@author Yu-Jing
@create 2024-08-15-下午 04:32
 */
data class AddLinkInfoValidateResult(
    var user: User? = null

): CommandValidateResult()

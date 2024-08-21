package com.coco.application.cqrs.command.disabledLinkInfo

import com.coco.application.cqrs.command.base.Command
import com.coco.application.middleware.auth.JwtRequest
import io.smallrye.mutiny.Uni

/**
@author Yu-Jing
@create 2024-08-15-下午 05:35
 */
data class DisabledLinkInfoCommand(
    var id: String,
    var jwt: JwtRequest? = null
): Command<Uni<Boolean>>
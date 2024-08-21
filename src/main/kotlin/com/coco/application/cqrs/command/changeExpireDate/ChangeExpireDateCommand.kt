package com.coco.application.cqrs.command.changeExpireDate

import com.coco.application.cqrs.command.base.Command
import com.coco.application.middleware.auth.JwtRequest
import io.smallrye.mutiny.Uni
import java.util.*

/**
@author Yu-Jing
@create 2024-08-16-上午 09:40
 */
data class ChangeExpireDateCommand(
    var id: String,
    var expireDate: Date? = null,
    var jwt: JwtRequest? = null,
): Command<Uni<Boolean>>

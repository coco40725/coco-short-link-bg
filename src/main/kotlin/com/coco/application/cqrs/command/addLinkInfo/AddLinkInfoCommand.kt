package com.coco.application.cqrs.command.addLinkInfo

import com.coco.application.cqrs.command.base.Command
import com.coco.application.middleware.auth.JwtRequest
import io.smallrye.mutiny.Uni
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-上午 11:04
 */
data class AddLinkInfoCommand(
    var jwt: JwtRequest? = null,
    var shortLink: String? = null,
    var originalLink: String,
    var expirationDate: Date? = null,
): Command<Uni<AddLinkInfoResult?>>

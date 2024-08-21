package com.coco.application.cqrs.command.redirectToOriginalLink

import com.coco.application.cqrs.command.base.Command
import com.coco.domain.vo.RequestHeaderData
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.core.Response

/**
@author Yu-Jing
@create 2024-08-11-上午 10:52
 */
data class RedirectToOriginalLinkCommand(
    var shortLink: String,
    var headerData: RequestHeaderData
): Command<Uni<Response>>

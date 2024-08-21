package com.coco.application.cqrs.command.redirectToOriginalLink

import com.coco.application.cqrs.command.base.CommandHandler
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.service.LinkManagementService
import com.coco.domain.model.LinkLog
import com.coco.domain.vo.pubsub.LinkLogData
import com.coco.infra.pubsub.LinkLogPubSubSvc
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.bson.types.ObjectId
import java.net.URI
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-上午 10:52
 */

@ApplicationScoped
class RedirectToOriginalLinkHandler @Inject constructor(
    private val linkManageSvc: LinkManagementService,
    private val linkLogPubSubSvc: LinkLogPubSubSvc


): CommandHandler<Uni<Response>, RedirectToOriginalLinkCommand> {

    override fun handle(command: RedirectToOriginalLinkCommand, validateResult: CommandValidateResult?): Uni<Response> {
        val shortLink = command.shortLink
        val headerData = command.headerData
        return linkManageSvc.getOriginalLink(shortLink).map { originLink ->
            if (originLink != null) {
                val now = Date().time * 1000 // biqQuery 是微秒
                val bigQueryData = LinkLog(
                    id = ObjectId().toString(),
                    shortLink = shortLink,
                    refererIP = headerData.refererIP,
                    userAgent = headerData.userAgent,
                    referer = headerData.referer,
                    createDate = Date(now)
                )

                linkLogPubSubSvc.publish(bigQueryData)
                Response.status(Response.Status.MOVED_PERMANENTLY)
                    .location(URI.create(originLink))
                    .header("Cache-Control", "max-age=5")
                    .build()
            } else {
                Response.status(Response.Status.NOT_FOUND).build()
            }
        }
    }

}

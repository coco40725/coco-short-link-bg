package com.coco.application.cqrs.command.addLinkInfo

import com.coco.application.cqrs.command.base.CommandHandler
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.service.LinkManagementService
import com.coco.domain.model.LinkInfo
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-上午 11:04
 */
@ApplicationScoped
class AddLinkInfoHandler @Inject constructor(
    private val linkManageSvc: LinkManagementService

): CommandHandler<Uni<AddLinkInfoResult?>, AddLinkInfoCommand> {
    override fun handle(command: AddLinkInfoCommand, validateResult: CommandValidateResult?): Uni<AddLinkInfoResult?> {
        val now = Date()
        val validResult = if ( validateResult != null ) {
            validateResult as AddLinkInfoValidateResult
        } else {
            null
        }

        val userId = if (validResult != null) {
            validateResult as AddLinkInfoValidateResult
            validResult.user?.id ?: "guest"
        } else {
            "guest"
        }

        val createShortLinkUni = if (command.shortLink == null) {
            linkManageSvc.getShortLinkFromWhiteList()
        } else {
            Uni.createFrom().item(command.shortLink)
        }.memoize().indefinitely()


        val addLinkUni = createShortLinkUni
            .chain { shortLink ->
                val logInfo = LinkInfo(
                    shortLink = shortLink,
                    userId = userId,
                    originalLink = command.originalLink,
                    expirationDate = command.expirationDate,
                    lastUpdateDate = now,
                    createDate = now,
                    enabled = true
                )
                linkManageSvc.addLinkInfoLog(logInfo) }

            .map { insertedLinkInfo ->
                AddLinkInfoResult(
                    id = insertedLinkInfo?.id.toString(),
                    shortLink = insertedLinkInfo?.shortLink,
                    userId = insertedLinkInfo?.userId.toString(),
                    originalLink = insertedLinkInfo?.originalLink,
                    createDate = insertedLinkInfo?.createDate,
                    expirationDate = insertedLinkInfo?.expirationDate,
                )
            }
        return addLinkUni
    }
}

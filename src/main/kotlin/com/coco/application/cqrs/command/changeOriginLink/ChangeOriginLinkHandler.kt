package com.coco.application.cqrs.command.changeOriginLink

import com.coco.application.cqrs.command.base.CommandHandler
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.service.LinkManagementService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
@author Yu-Jing
@create 2024-08-15-下午 05:35
 */

@ApplicationScoped
class ChangeOriginLinkHandler @Inject constructor(
    private val linkManagementService: LinkManagementService

): CommandHandler<Uni<Boolean>, ChangeOriginLinkCommand> {
    override fun handle(command: ChangeOriginLinkCommand, validateResult: CommandValidateResult?): Uni<Boolean> {
        return linkManagementService.changeOriginLink(command.id, command.originLink).map { it != null }

    }
}
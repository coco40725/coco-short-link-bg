package com.coco.application.cqrs.command.disabledLinkInfo

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
class DisabledLinkInfoHandler @Inject constructor(
    private val linkManagementService: LinkManagementService

): CommandHandler<Uni<Boolean>, DisabledLinkInfoCommand> {
    override fun handle(command: DisabledLinkInfoCommand, validateResult: CommandValidateResult?): Uni<Boolean> {
        return linkManagementService.disableLinkInfo(command.id).map { it != null }
    }
}

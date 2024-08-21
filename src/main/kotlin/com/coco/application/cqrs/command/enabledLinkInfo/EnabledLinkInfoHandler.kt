package com.coco.application.cqrs.command.enabledLinkInfo

import com.coco.application.cqrs.command.base.CommandHandler
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.service.LinkManagementService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
@author Yu-Jing
@create 2024-08-15-下午 05:35
 */

@ApplicationScoped
class EnabledLinkInfoHandler(
    private val linkManagementService: LinkManagementService
): CommandHandler<Uni<Boolean>, EnabledLinkInfoCommand> {
    override fun handle(command: EnabledLinkInfoCommand, validateResult: CommandValidateResult?): Uni<Boolean> {
        return linkManagementService.enabledLinkInfo(command.id)
    }
}
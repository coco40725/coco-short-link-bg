package com.coco.application.cqrs.command.changeExpireDate

import com.coco.application.cqrs.command.base.CommandHandler
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.service.LinkManagementService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
@author Yu-Jing
@create 2024-08-16-上午 09:40
 */

@ApplicationScoped
class ChangeExpireDateHandler @Inject constructor(
    private val linkManagementService: LinkManagementService
): CommandHandler<Uni<Boolean>,ChangeExpireDateCommand> {
    override fun handle(command: ChangeExpireDateCommand, validateResult: CommandValidateResult?): Uni<Boolean> {
        return linkManagementService.changeExpireDate(command.id, command.expireDate)
    }

}

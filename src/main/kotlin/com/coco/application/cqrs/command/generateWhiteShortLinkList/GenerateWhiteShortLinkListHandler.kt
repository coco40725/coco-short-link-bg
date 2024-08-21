package com.coco.application.cqrs.command.generateWhiteShortLinkList

import com.coco.application.cqrs.command.base.Command
import com.coco.application.cqrs.command.base.CommandHandler
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.service.LinkManagementService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
@author Yu-Jing
@create 2024-08-12-上午 11:27
 */

@ApplicationScoped
class GenerateWhiteShortLinkListHandler @Inject constructor(
    private val linkManageSvc: LinkManagementService,

): CommandHandler<Uni<Long>, GenerateWhiteShortLinkListCommand> {
    private val thresholdWhiteListSize = 1000
    override fun handle(command: GenerateWhiteShortLinkListCommand, validateResult: CommandValidateResult?): Uni<Long> {
        return linkManageSvc.getWhiteShortLinks().chain { it ->
            if (it.size < thresholdWhiteListSize) {
                linkManageSvc.generateWhiteShortLinks(1000)
            } else {
                Uni.createFrom().item(it.size.toLong())
            }
        }
    }
}
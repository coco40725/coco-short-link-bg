package com.coco.application.cqrs.command.addLinkInfo

import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.cqrs.command.base.CommandValidator
import com.coco.application.service.LinkManagementService
import com.coco.domain.service.linkInfo.LinkInfoSvc
import com.coco.infra.client.VerifyTokenClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named

/**
@author Yu-Jing
@create 2024-08-11-下午 03:22
 */
@ApplicationScoped
class AddLinkInfoValidate @Inject constructor(
    private val linkInfoSvc: LinkInfoSvc,
    private val linkManagementService: LinkManagementService,
    @Named("grpc") private val verifyTokenClient: VerifyTokenClient
): CommandValidator<AddLinkInfoCommand> {
    override fun validateCommand(command: AddLinkInfoCommand): Uni<CommandValidateResult> {
        var isValid = true
        val message = mutableListOf<String>()


        // if shortLink is not null, check ths validity of shortLink
        val shortLink = command.shortLink
        if (shortLink != null && !linkInfoSvc.isShortLinkFormatValid(shortLink)) {
            isValid = false
            message.add("Short link is invalid")
        }

        // original link should not be empty
        val originalLink = command.originalLink
        if (!linkInfoSvc.isOriginalLinkFormatValid(originalLink)) {
            isValid = false
            message.add("Original link should not be empty")
        }

        // if expiration date is not null, check if date is after now
        val expirationDate = command.expirationDate
        if (expirationDate != null && !linkInfoSvc.isExpirationDateValid(expirationDate)) {
            isValid = false
            message.add("Expiration date should be after today")
        }
        if (!isValid) return Uni.createFrom().item(AddLinkInfoValidateResult(isValid, message))


        // if shortLink is not null then should not be exist
        val shortLinkExistUni = if (shortLink != null) {
            linkManagementService.checkShortLinkIsExist(shortLink)
        } else {
            Uni.createFrom().item(false)
        }



        // rule3: verify token
        val jwt = command.jwt
        val token = jwt?.token
        val tokenUni = if (token != null) {
            verifyTokenClient.verifyToken(token)
        } else {
            Uni.createFrom().nullItem()
        }

        return Uni.combine().all().unis(
            shortLinkExistUni,
            tokenUni
        ).with { linkExist, payload ->
            if (linkExist) {
                isValid = false
                message.add("short link is exist")
            }
            if (payload == null && token != null) {
                isValid = false
                message.add("token is invalid")
                AddLinkInfoValidateResult(isValid, message)
            }
            AddLinkInfoValidateResult(isValid, message, payload)
        }
    }

}
package com.coco.application.cqrs.command.addLinkInfo

import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.exception.CommandValidationException
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
    private val className = AddLinkInfoValidate::class.java.simpleName

    override fun validateCommand(command: AddLinkInfoCommand): Uni<CommandValidateResult> {

        // if shortLink is not null, check ths validity of shortLink
        val shortLink = command.shortLink
        if (shortLink != null && !linkInfoSvc.isShortLinkFormatValid(shortLink)) {
            return Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.SHORT_LINK_INVALID.name))
        }

        // original link should not be empty
        val originalLink = command.originalLink
        if (!linkInfoSvc.isOriginalLinkFormatValid(originalLink)) {
            return Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.ORIGINAL_LINK_INVALID.name))

        }

        // if expiration date is not null, check if date is after now
        val expirationDate = command.expirationDate
        if (expirationDate != null && !linkInfoSvc.isExpirationDateValid(expirationDate)) {
            return Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.EXPIRATION_DATE_INVALID.name))
        }


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
        ).withUni { linkExist, payload ->
            if (linkExist) {
                Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.SHORT_LINK_EXIST.name))
            } else if (payload == null && token != null) {
                Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.TOKEN_INVALID.name))
            } else {
                Uni.createFrom().item(AddLinkInfoValidateResult(payload))
            }
        }
    }

}

enum class ValidateMessage {
    SHORT_LINK_INVALID,
    ORIGINAL_LINK_INVALID,
    EXPIRATION_DATE_INVALID,
    SHORT_LINK_EXIST,
    TOKEN_INVALID
}
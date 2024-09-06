package com.coco.application.cqrs.command.enabledLinkInfo

import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.cqrs.command.base.CommandValidator
import com.coco.application.exception.CommandValidationException
import com.coco.application.service.LinkManagementService
import com.coco.infra.client.VerifyTokenClient
import com.coco.infra.restClient.VerifyTokenRestClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import org.bson.types.ObjectId
import org.eclipse.microprofile.rest.client.inject.RestClient

/**
@author Yu-Jing
@create 2024-08-15-下午 05:46
 */
@ApplicationScoped
class EnabledLinkInfoValidate @Inject constructor(
    @Named("grpc") private val verifyTokenClient: VerifyTokenClient,
    private val linkManagementService: LinkManagementService

): CommandValidator<EnabledLinkInfoCommand> {

    private val className = EnabledLinkInfoValidate::class.java.simpleName

    override fun validateCommand(command: EnabledLinkInfoCommand): Uni<CommandValidateResult> {
        // rule: id should not be null and should be a valid ObjectId
        val id = command.id
        if (!ObjectId.isValid(id)) {
            return Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.ID_INVALID.name))
        }


        //  rule: jwt should not be null
        val token = command.jwt?.token
        if (token.isNullOrBlank()) {
            return Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.TOKEN_INVALID.name))
        }

        // rule: expireDate should be null or after now
        val checkExpireUni = linkManagementService.checkShortLinkIsExpired(id)

        // rule: verify token
        val verifyTokenUni = verifyTokenClient.verifyToken(token)


        return Uni.combine().all().unis(
            checkExpireUni,
            verifyTokenUni
        ).withUni { expire, payload ->
            if (expire != true) {
                Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.SHORT_LINK_EXPIRED.name))
            } else if (payload == null ) {
                Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.TOKEN_INVALID.name))

            } else {
                Uni.createFrom().item(EnabledLinkValidateResult(payload))
            }
        }
    }
}

enum class ValidateMessage {
    ID_INVALID,
    TOKEN_INVALID,
    SHORT_LINK_EXPIRED
}
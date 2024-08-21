package com.coco.application.cqrs.command.enabledLinkInfo

import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.cqrs.command.base.CommandValidator
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
    override fun validateCommand(command: EnabledLinkInfoCommand): Uni<CommandValidateResult> {
        var isValid = true
        val message = mutableListOf<String>()

        // rule: id should not be null and should be a valid ObjectId
        val id = command.id
        if (!ObjectId.isValid(id)) {
            isValid = false
            message.add("id is invalid")
        }


        //  rule: jwt should not be null
        val jwt = command.jwt
        if (jwt == null) {
            isValid = false
            message.add("jwt is invalid")
            return Uni.createFrom().item(CommandValidateResult(isValid, message))
        }

        // rule: expireDate should be null or after now
        val checkExpireUni = linkManagementService.checkShortLinkIsExpired(id)

        // rule: verify token
        val token = jwt.token
        val verifyTokenUni = if (token != null) {
            verifyTokenClient.verifyToken(token)
        } else {
            Uni.createFrom().nullItem()
        }

        return Uni.combine().all().unis(
            checkExpireUni,
            verifyTokenUni
        ).with { expire, payload ->
            if (expire != true) {
                isValid = false
                message.add("short link is expired")
            }
            if (payload == null && token != null) {
                isValid = false
                message.add("token is invalid")
            }
            EnabledLinkValidateResult(isValid, message, payload)
        }
    }
}
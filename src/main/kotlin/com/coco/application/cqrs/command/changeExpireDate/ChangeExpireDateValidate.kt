package com.coco.application.cqrs.command.changeExpireDate

import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.cqrs.command.base.CommandValidator
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
@create 2024-08-16-上午 09:49
 */

@ApplicationScoped
class ChangeExpireDateValidate @Inject constructor(
    @Named("grpc") private val verifyTokenClient: VerifyTokenClient
): CommandValidator<ChangeExpireDateCommand> {
    override fun validateCommand(command: ChangeExpireDateCommand): Uni<CommandValidateResult> {
        var isValid = true
        val message = mutableListOf<String>()

        // rule1: id should not be null and should be a valid ObjectId
        val id = command.id
        if (!ObjectId.isValid(id)) {
            isValid = false
            message.add("id is invalid")
        }

        // rule2: jwt should not be null
        val jwt = command.jwt
        if (jwt == null) {
            isValid = false
            message.add("jwt is invalid")
            return Uni.createFrom().item(CommandValidateResult(isValid, message))
        }

        // rule3: verify token
        val token = jwt.token
        return if (token != null) {
            verifyTokenClient.verifyToken(token).map { payload ->
                if (payload == null) {
                    isValid = false
                    message.add("token is invalid")
                    ChangeExpireDateValidateResult(isValid, message)

                } else {
                    ChangeExpireDateValidateResult(isValid, message, payload)
                }

            }
        } else {
            Uni.createFrom().item(ChangeExpireDateValidateResult(isValid, message))
        }
    }
}
package com.coco.application.cqrs.command.changeExpireDate

import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.cqrs.command.base.CommandValidator
import com.coco.application.exception.CommandValidationException
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
    private val className = ChangeExpireDateValidate::class.java.simpleName
    override fun validateCommand(command: ChangeExpireDateCommand): Uni<CommandValidateResult> {

        // rule1: id should not be null and should be a valid ObjectId
        val id = command.id
        if (!ObjectId.isValid(id)) {
            return Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.ID_INVALID.name))
        }

        // rule2: jwt should not be null
        val token = command.jwt?.token
        if (token.isNullOrBlank()) {
            return Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.TOKEN_INVALID.name))
        }

        // rule3: verify token
        return  verifyTokenClient.verifyToken(token).chain { payload ->
            if (payload == null) {
                Uni.createFrom().failure(CommandValidationException(className, ValidateMessage.TOKEN_INVALID.name))
            } else {
                Uni.createFrom().item(ChangeExpireDateValidateResult(payload))
            }
        }
    }
}

enum class ValidateMessage {
    ID_INVALID,
    TOKEN_INVALID
}
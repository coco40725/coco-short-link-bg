package com.coco.application.cqrs.query.getUserShortLinkInfo

import com.coco.application.cqrs.query.base.QueryValidateResult
import com.coco.application.exception.QueryValidationException
import com.coco.application.cqrs.query.base.QueryValidator
import com.coco.infra.client.VerifyTokenClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named

/**
@author Yu-Jing
@create 2024-08-15-下午 04:23
 */

@ApplicationScoped
class GetUserShortLinkInfoValidate @Inject constructor(
    @Named("grpc") private val verifyTokenClient: VerifyTokenClient
): QueryValidator<GetUserShortLinkInfoQuery> {
    private val className = GetUserShortLinkInfoValidate::class.java.simpleName
    override fun validateQuery(query: GetUserShortLinkInfoQuery): Uni<QueryValidateResult> {
        val token = query.jwt?.token

        // rule1: jwt must not be null
        if (token.isNullOrBlank()) {
            return Uni.createFrom().failure(QueryValidationException(className, ValidateMessage.TOKEN_INVALID.name))
        }

        return verifyTokenClient.verifyToken(token).chain { payload ->
            if (payload == null) {
                Uni.createFrom().failure(QueryValidationException(className, ValidateMessage.TOKEN_INVALID.name))

            } else {
                Uni.createFrom().item(GetUserShortLinkInfoValidateResult( payload))
            }
        }
    }
}

enum class ValidateMessage {
    TOKEN_INVALID
}
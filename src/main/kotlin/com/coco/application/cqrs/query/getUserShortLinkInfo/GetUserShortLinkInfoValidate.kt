package com.coco.application.cqrs.query.getUserShortLinkInfo

import com.coco.application.cqrs.query.base.QueryValidateResult
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
    override fun validateQuery(query: GetUserShortLinkInfoQuery): Uni<QueryValidateResult> {
        var isValid = true
        val message = mutableListOf<String>()
        val jwt = query.jwt
        // rule1: jwt must not be null
        if (jwt == null) {
            isValid = false
            message.add("jwt must not be null")
            return Uni.createFrom().item(GetUserShortLinkInfoValidateResult(isValid, message))
        }


        // rule2: token must not be null
        if (jwt.token.isNullOrEmpty()) {
            isValid = false
            message.add("token must not be null or empty")
            return Uni.createFrom().item(GetUserShortLinkInfoValidateResult(isValid, message))
        }

        // rule3: verify token
        val token = jwt.token !!
        return verifyTokenClient.verifyToken(token).map { payload ->
            if (payload == null) {
                isValid = false
                message.add("token is invalid")
                GetUserShortLinkInfoValidateResult(isValid, message)

            } else {
                GetUserShortLinkInfoValidateResult(isValid, message, payload)
            }
        }
    }
}
package com.coco.application.cqrs.query.getUserLinkStat

import com.coco.application.cqrs.query.base.QueryValidateResult
import com.coco.application.cqrs.query.base.QueryValidator
import com.coco.infra.client.VerifyTokenClient
import com.coco.infra.grpc.VerifyTokenGrpc
import com.coco.infra.restClient.VerifyTokenRestClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import org.eclipse.microprofile.rest.client.inject.RestClient

/**
@author Yu-Jing
@create 2024-08-15-下午 04:23
 */

@ApplicationScoped
class GetUserLinkStatValidate @Inject constructor(
    @Named("grpc") private val verifyTokenClient: VerifyTokenClient
): QueryValidator<GetUserLinkStatQuery> {
    override fun validateQuery(query: GetUserLinkStatQuery): Uni<QueryValidateResult> {
        var isValid = true
        val message = mutableListOf<String>()

        val link = query.shortLink
        // rule: link must not be null
        if (link.isEmpty()) {
            isValid = false
            message.add("shortLink must not be empty")
        }

        val jwt = query.jwt
        // rule: jwt must not be null
        if (jwt == null) {
            isValid = false
            message.add("jwt must not be null")
        }

        // rule: token must not be null
        if (jwt?.token.isNullOrEmpty()) {
            isValid = false
            message.add("token must not be null or empty")
            return Uni.createFrom().item(GetUserLinkStatValidateResult(isValid, message))
        }

        // rule: verify token
        val token = jwt?.token !!
        return verifyTokenClient.verifyToken(token).map { payload ->
            if (payload == null) {
                isValid = false
                message.add("token is invalid")
                GetUserLinkStatValidateResult(isValid, message)

            } else {
                GetUserLinkStatValidateResult(isValid, message, payload)
            }

        }
    }
}
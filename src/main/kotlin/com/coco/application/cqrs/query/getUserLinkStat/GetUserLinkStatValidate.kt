package com.coco.application.cqrs.query.getUserLinkStat

import com.coco.application.cqrs.query.base.QueryValidateResult
import com.coco.application.cqrs.query.base.QueryValidator
import com.coco.application.exception.QueryValidationException
import com.coco.domain.service.linkInfo.LinkInfoSvc
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
    @Named("grpc") private val verifyTokenClient: VerifyTokenClient,
    private val linkInfoSvc: LinkInfoSvc
): QueryValidator<GetUserLinkStatQuery> {
    private val className = GetUserLinkStatValidate::class.java.simpleName
    override fun validateQuery(query: GetUserLinkStatQuery): Uni<QueryValidateResult> {
        val link = query.shortLink
        // rule: link must not be null
        if (!linkInfoSvc.isShortLinkFormatValid(link)) {
            return Uni.createFrom().failure(QueryValidationException(className, ValidateMessage.SHORT_LINK_INVALID.name))
        }

        val token = query.jwt?.token
        // rule: jwt must not be null
        if (token.isNullOrBlank()) {
            return Uni.createFrom().failure(QueryValidationException(className, ValidateMessage.TOKEN_INVALID.name))
        }


        // rule: verify token
        return verifyTokenClient.verifyToken(token).chain { payload ->
            if (payload == null) {
                Uni.createFrom().failure(QueryValidationException(className, ValidateMessage.TOKEN_INVALID.name))

            } else {
                Uni.createFrom().item(GetUserLinkStatValidateResult(payload))
            }
        }
    }
}

enum class ValidateMessage {
    TOKEN_INVALID,
    SHORT_LINK_INVALID
}
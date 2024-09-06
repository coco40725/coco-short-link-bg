package com.coco.integration.application.cqrs.query.getUserShortLinkInfo

import com.coco.application.cqrs.query.getUserShortLinkInfo.GetUserShortLinkInfoQuery
import com.coco.application.cqrs.query.getUserShortLinkInfo.GetUserShortLinkInfoValidate
import com.coco.application.cqrs.query.getUserShortLinkInfo.GetUserShortLinkInfoValidateResult
import com.coco.application.exception.QueryValidationException
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.client.VerifyTokenClient
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-05-下午 07:46
 */
@QuarkusTest
class GetUserShortLinkInfoValidateTest {
    private val verifyTokenClient = mockk<VerifyTokenClient>()
    private val getUserShortLinkInfoValidate = GetUserShortLinkInfoValidate(verifyTokenClient)

    @Test
    fun `test valid query return result` (){
        val jwt = JwtRequest()
        jwt.token = "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val query = GetUserShortLinkInfoQuery(jwt)
        getUserShortLinkInfoValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .assertCompleted()
            .assertItem(GetUserShortLinkInfoValidateResult(user))
    }

    @Test
    fun `test null jwt return exception with TOKEN_INVALID` (){
        val jwt = JwtRequest()
        jwt.token = "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val query = GetUserShortLinkInfoQuery(null)
        getUserShortLinkInfoValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(QueryValidationException::class.java)
    }

    @Test
    fun `test null token return exception with TOKEN_INVALID` (){
        val jwt = JwtRequest()
        jwt.token = null
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val query = GetUserShortLinkInfoQuery(null)
        getUserShortLinkInfoValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(QueryValidationException::class.java)
    }

    @Test
    fun `test invalid token return exception with TOKEN_INVALID` (){
        val jwt = JwtRequest()
        jwt.token = "token"

        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val query = GetUserShortLinkInfoQuery(null)
        getUserShortLinkInfoValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(QueryValidationException::class.java)
    }

}
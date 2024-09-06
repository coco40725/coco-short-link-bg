package com.coco.integration.application.cqrs.query.getUserLinkStat

import com.coco.application.cqrs.query.getUserLinkStat.GetUserLinkStatQuery
import com.coco.application.cqrs.query.getUserLinkStat.GetUserLinkStatValidate
import com.coco.application.cqrs.query.getUserLinkStat.GetUserLinkStatValidateResult
import com.coco.application.exception.QueryValidationException
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.client.VerifyTokenClient
import com.coco.infra.grpc.VerifyTokenGrpc
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-05-下午 07:25
 */

@QuarkusTest
class GetUserLinkStatValidateTest {
    private val verifyTokenClient = mockk<VerifyTokenGrpc>()

    @Inject
    lateinit var getUserLinkStatValidate: GetUserLinkStatValidate

    @BeforeEach
    fun setUp() {
        QuarkusMock.installMockForType(verifyTokenClient, VerifyTokenGrpc::class.java)
    }


    @Test
    fun `test valid query return result` (){
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val jwt = JwtRequest()
        jwt.token = "token"
        val query = GetUserLinkStatQuery("shortLink", jwt)
        getUserLinkStatValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .assertCompleted()
            .assertItem(GetUserLinkStatValidateResult(user))
    }

    @Test
    fun `test blank short link return exception with SHORT_LINK_INVALID` (){
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val query = GetUserLinkStatQuery("  ", JwtRequest())
        getUserLinkStatValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(QueryValidationException::class.java)
    }

    @Test
    fun `test null jwt link return exception with TOKEN_INVALID` (){
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val query = GetUserLinkStatQuery("shortlink", null)
        getUserLinkStatValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(QueryValidationException::class.java)
    }

    @Test
    fun `test null token link return exception with TOKEN_INVALID` (){
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val jwt = JwtRequest()
        jwt.token = null
        val query = GetUserLinkStatQuery("shortlink", jwt)
        getUserLinkStatValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(QueryValidationException::class.java)
    }

    @Test
    fun `test invalid jwt link return exception with TOKEN_INVALID` (){
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val query = GetUserLinkStatQuery("shortlink", JwtRequest())
        getUserLinkStatValidate.validateQuery(query)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(QueryValidationException::class.java)
    }
}
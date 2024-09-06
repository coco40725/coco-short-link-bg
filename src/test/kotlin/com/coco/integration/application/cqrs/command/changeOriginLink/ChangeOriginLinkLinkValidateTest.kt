package com.coco.integration.application.cqrs.command.changeOriginLink

import com.coco.application.cqrs.command.changeOriginLink.ChangeOriginLinkCommand
import com.coco.application.cqrs.command.changeOriginLink.ChangeOriginLinkLinkValidate
import com.coco.application.cqrs.command.changeOriginLink.ChangeOriginLinkValidateResult
import com.coco.application.exception.CommandValidationException
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.grpc.VerifyTokenGrpc
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-05-下午 10:40
 */
@QuarkusTest
class ChangeOriginLinkLinkValidateTest {
    private val verifyTokenClient = mockk<VerifyTokenGrpc>()

    @Inject
    lateinit var changeOriginLinkLinkValidate: ChangeOriginLinkLinkValidate
    @BeforeEach
    fun setUp(){
        QuarkusMock.installMockForType(verifyTokenClient, VerifyTokenGrpc::class.java)
    }

    @Test
    fun `test valid command return result`(){
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

        val command = ChangeOriginLinkCommand(
            id = ObjectId().toString(),
            jwt = jwt,
            originLink = "https://www.google.com"
        )

        changeOriginLinkLinkValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertItem(ChangeOriginLinkValidateResult(user))
    }

    @Test
    fun `test invalid objectId for id return exception`(){
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

        val command = ChangeOriginLinkCommand(
            id = "invalidId",
            jwt = jwt,
            originLink = "https://www.google.com"
        )

        changeOriginLinkLinkValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test invalid original link for id return exception`(){
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

        val command = ChangeOriginLinkCommand(
            id = ObjectId().toString(),
            jwt = jwt,
            originLink = "invalidLink"
        )

        changeOriginLinkLinkValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test null jwt for id return exception`(){
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()

        val command = ChangeOriginLinkCommand(
            id = ObjectId().toString(),
            jwt = null,
            originLink = "https://www.google.com"
        )

        changeOriginLinkLinkValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test null token for id return exception`(){
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

        val command = ChangeOriginLinkCommand(
            id = ObjectId().toString(),
            jwt = jwt,
            originLink = "https://www.google.com"
        )

        changeOriginLinkLinkValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test invalid token for id return exception`(){
        val jwt = JwtRequest()
        jwt.token = "token"

        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()

        val command = ChangeOriginLinkCommand(
            id = ObjectId().toString(),
            jwt = jwt,
            originLink = "https://www.google.com"
        )

        changeOriginLinkLinkValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }
}
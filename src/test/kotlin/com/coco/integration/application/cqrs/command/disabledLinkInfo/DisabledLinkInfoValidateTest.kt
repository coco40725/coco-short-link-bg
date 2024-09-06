package com.coco.integration.application.cqrs.command.disabledLinkInfo

import com.coco.application.cqrs.command.disabledLinkInfo.DisabledLinkInfoCommand
import com.coco.application.cqrs.command.disabledLinkInfo.DisabledLinkInfoValidate
import com.coco.application.cqrs.command.disabledLinkInfo.DisabledLinkValidateResult
import com.coco.application.exception.CommandValidationException
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.client.VerifyTokenClient
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-05-下午 10:48
 */

@QuarkusTest
class DisabledLinkInfoValidateTest {
    private val verifyTokenClient = mockk<VerifyTokenClient>()
    private val disabledLinkInfoValidate = DisabledLinkInfoValidate(verifyTokenClient)

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
        val command = DisabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = jwt
        )
        disabledLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertItem(DisabledLinkValidateResult(user))
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
        val command = DisabledLinkInfoCommand(
            id = "invalidId",
            jwt = jwt
        )
        disabledLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test null jwt for id return exception`(){
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

        val command = DisabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = null
        )

        disabledLinkInfoValidate.validateCommand(command)
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

        val command = DisabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = jwt
        )

        disabledLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test invalid token for id return exception`(){
        val jwt = JwtRequest()
        jwt.token = "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()

        val command = DisabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = jwt
        )

        disabledLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }
}
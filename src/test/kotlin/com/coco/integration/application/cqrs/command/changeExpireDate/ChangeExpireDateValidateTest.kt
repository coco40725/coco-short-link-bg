package com.coco.integration.application.cqrs.command.changeExpireDate

import com.coco.application.cqrs.command.changeExpireDate.ChangeExpireDateCommand
import com.coco.application.cqrs.command.changeExpireDate.ChangeExpireDateValidate
import com.coco.application.cqrs.command.changeExpireDate.ChangeExpireDateValidateResult
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
@author Yu-Jing
@create 2024-09-05-下午 10:33
 */

@QuarkusTest
class ChangeExpireDateValidateTest {
    private val verifyTokenClient = mockk<VerifyTokenClient>()
    private val changeExpireDateValidate = ChangeExpireDateValidate(verifyTokenClient)


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

        val command = ChangeExpireDateCommand(
            id = ObjectId().toString(),
            jwt = jwt,
            expireDate = Date()
        )
        changeExpireDateValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertItem(ChangeExpireDateValidateResult(user))
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

        val command = ChangeExpireDateCommand(
            id = "invalidId",
            jwt = jwt,
            expireDate = Date()
        )
        changeExpireDateValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test null jwt for id return exception`(){
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()

        val command = ChangeExpireDateCommand(
            id = "invalidId",
            jwt = null,
            expireDate = Date()
        )
        changeExpireDateValidate.validateCommand(command)
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

        val command = ChangeExpireDateCommand(
            id = "invalidId",
            jwt = jwt,
            expireDate = Date()
        )
        changeExpireDateValidate.validateCommand(command)
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

        val command = ChangeExpireDateCommand(
            id = "invalidId",
            jwt = jwt,
            expireDate = Date()
        )
        changeExpireDateValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }
}
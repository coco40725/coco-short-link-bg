package com.coco.integration.application.cqrs.command.enabledLinkInfo

import com.coco.application.cqrs.command.disabledLinkInfo.DisabledLinkInfoCommand
import com.coco.application.cqrs.command.enabledLinkInfo.EnabledLinkInfoCommand
import com.coco.application.cqrs.command.enabledLinkInfo.EnabledLinkInfoValidate
import com.coco.application.cqrs.command.enabledLinkInfo.EnabledLinkValidateResult
import com.coco.application.exception.CommandValidationException
import com.coco.application.middleware.auth.JwtRequest
import com.coco.application.service.LinkManagementService
import com.coco.domain.model.User
import com.coco.infra.client.VerifyTokenClient
import com.coco.integration.infra.repo.SetupData
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-05-下午 10:54
 */
@QuarkusTest
class EnabledLinkInfoValidateTest {
    private val verifyTokenClient = mockk<VerifyTokenClient>()
    private val linkInfoManagementService = mockk<LinkManagementService>()
    private val enabledLinkInfoValidate = EnabledLinkInfoValidate(verifyTokenClient, linkInfoManagementService)

    @Inject
    lateinit var setupData: SetupData

    @BeforeEach
    fun setUp(){
        setupData.beforeEach()
    }
    @AfterEach
    fun tearDown(){
        setupData.cleanEach()
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
        every { linkInfoManagementService.checkShortLinkIsExpired(any()) } returns Uni.createFrom().item(true)
        val command = EnabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = jwt
        )
        enabledLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertItem(EnabledLinkValidateResult(user))
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
        every { linkInfoManagementService.checkShortLinkIsExpired(any()) } returns Uni.createFrom().item(true)

        val command = EnabledLinkInfoCommand(
            id = "invalidId",
            jwt = jwt
        )
        enabledLinkInfoValidate.validateCommand(command)
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
        every { linkInfoManagementService.checkShortLinkIsExpired(any()) } returns Uni.createFrom().item(true)

        val command = EnabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = null
        )

        enabledLinkInfoValidate.validateCommand(command)
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
        every { linkInfoManagementService.checkShortLinkIsExpired(any()) } returns Uni.createFrom().item(true)

        val command = EnabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = jwt
        )

        enabledLinkInfoValidate.validateCommand(command)
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
        every { linkInfoManagementService.checkShortLinkIsExpired(any()) } returns Uni.createFrom().item(true)

        val command = EnabledLinkInfoCommand(
            id = ObjectId().toString(),
            jwt = jwt
        )

        enabledLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }
}
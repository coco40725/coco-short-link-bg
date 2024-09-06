package com.coco.integration.application.cqrs.command.addLinkInfo

import com.coco.application.cqrs.command.addLinkInfo.AddLinkInfoCommand
import com.coco.application.cqrs.command.addLinkInfo.AddLinkInfoValidate
import com.coco.application.cqrs.command.addLinkInfo.AddLinkInfoValidateResult
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.exception.CommandValidationException
import com.coco.application.middleware.auth.JwtRequest
import com.coco.application.service.LinkManagementService
import com.coco.domain.model.User
import com.coco.domain.service.linkInfo.LinkInfoSvc
import com.coco.infra.client.VerifyTokenClient
import com.coco.infra.config.WebConfig
import com.coco.infra.grpc.VerifyTokenGrpc
import com.coco.integration.infra.repo.SetupData
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
@author Yu-Jing
@create 2024-09-05-下午 07:55
 */
@QuarkusTest
class AddLinkInfoValidateTest {

    private val verifyTokenClient = mockk<VerifyTokenGrpc>()

    @Inject
    lateinit var addLinkInfoValidate: AddLinkInfoValidate

    @BeforeEach
    fun setUp() {
        QuarkusMock.installMockForType(verifyTokenClient, VerifyTokenGrpc::class.java)
        setupData.beforeEach()
    }

    @AfterEach
    fun clean() {
        setupData.cleanEach()
    }


    @Inject
    lateinit var setupData: SetupData

    @Inject
    lateinit var linkInfoSvc: LinkInfoSvc

    @Inject
    lateinit var linkManagementService: LinkManagementService

    @Inject
    lateinit var webConfig: WebConfig



    @Test
    fun `test valid command with null short link return result`(){
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
        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = jwt
        )

        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertItem(AddLinkInfoValidateResult(user))
    }

    @Test
    fun `test valid command with non-null short link return result`(){
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
        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}/ttest-123",
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = jwt
        )

        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertItem(AddLinkInfoValidateResult(user))
    }

    @Test
    fun `test non-null invalid shortLink return exception`(){
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
        val command = AddLinkInfoCommand(
            shortLink = "x@@@@#####%%%%",
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = jwt
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test already exist enabled shortLink return exception`(){
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
        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}/enable-1",
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = jwt
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test already exist disabled shortLink return exception`(){
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
        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}/disable-1",
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = jwt
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test invalid original link return exception`(){
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
        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "sdsd5656",
            expirationDate = null,
            jwt = jwt
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test invalid expireDate return exception`(){
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
        val invalidExpireDate = Date(Date().time - 1000 * 60 * 60)
        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.google.com",
            expirationDate = invalidExpireDate,
            jwt = jwt
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }

    @Test
    fun `test null jwt link return result`(){
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = null
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .assertCompleted()
            .assertItem(AddLinkInfoValidateResult(null))
    }

    @Test
    fun `test null token link return result`(){
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val jwt = JwtRequest()
        jwt.token = null
        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = jwt
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .assertCompleted()
            .assertItem(AddLinkInfoValidateResult(null))
    }

    @Test
    fun `test invalid jwt link return exception with TOKEN_INVALID`(){
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val jwt = JwtRequest()
        jwt.token = "token"
        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.google.com",
            expirationDate = null,
            jwt = jwt
        )
        addLinkInfoValidate.validateCommand(command)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(CommandValidationException::class.java)
    }
}
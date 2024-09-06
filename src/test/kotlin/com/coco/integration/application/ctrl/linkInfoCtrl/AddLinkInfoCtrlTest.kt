package com.coco.integration.application.ctrl.linkInfoCtrl

import com.coco.application.cqrs.command.addLinkInfo.AddLinkInfoCommand
import com.coco.application.cqrs.command.addLinkInfo.ValidateMessage
import com.coco.application.exception.ApplicationException
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.config.WebConfig
import com.coco.infra.constant.RedisConstant
import com.coco.infra.exception.RepoException
import com.coco.infra.grpc.VerifyTokenGrpc
import com.coco.infra.pubsub.LinkLogPubSubSvc
import com.coco.infra.repo.LinkInfoExpireTTLRepo
import com.coco.infra.repo.LinkInfoRepo
import com.coco.infra.repo.RedisRepo
import com.coco.integration.infra.repo.SetupData
import io.mockk.every
import io.mockk.mockk
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
@author Yu-Jing
@create 2024-09-06-下午 12:00
 */

@QuarkusTest
class AddLinkInfoCtrlTest {
    @Inject
    lateinit var setupData: SetupData

    @Inject
    lateinit var redisDS: ReactiveRedisDataSource

    @Inject
    lateinit var webConfig: WebConfig

    private val verifyTokenClient = mockk<VerifyTokenGrpc>()
    private val jwt = mockk<JwtRequest>()

    @BeforeEach
    fun beforeEach() {
        setupData.beforeEach()
        QuarkusMock.installMockForType(verifyTokenClient, VerifyTokenGrpc::class.java)
        QuarkusMock.installMockForType(jwt, JwtRequest::class.java)
    }

    @AfterEach
    fun afterEach() {
        setupData.cleanEach()
    }


    @Test
    fun `test valid command return 200`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}/short-link-3",
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )
        given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.OK.statusCode)

    }

    @Test
    fun `test invalid shortLink return 400 and SHORT_LINK_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}/@@@@@@@",
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )
        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .response()
            .body
            .asString()

        assertTrue(body == ValidateMessage.SHORT_LINK_INVALID.name)
    }

    @Test
    fun `test already exist enabled shortLink return 400 and SHORT_LINK_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}enable-1",
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )
        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .response()
            .body
            .asString()

        assertTrue(body == ValidateMessage.SHORT_LINK_INVALID.name)
    }

    @Test
    fun `test already exist disabled shortLink return 400 and SHORT_LINK_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}disable-1",
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )
        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .response()
            .body
            .asString()

        assertTrue(body == ValidateMessage.SHORT_LINK_INVALID.name)
    }

    @Test
    fun `test invalid original link return 400 and ORIGINAL_LINK_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )

        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "XXXXXXX",
            expirationDate = null
        )
        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .response()
            .body
            .asString()

        assertTrue(body == ValidateMessage.ORIGINAL_LINK_INVALID.name)
    }

    @Test
    fun `test invalid expireDate return 400 and EXPIRATION_DATE_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.youtube.com/",
            expirationDate = Date(Date().time - 1000 * 60 * 60 * 24)
        )
        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .response()
            .body
            .asString()

        assertTrue(body == ValidateMessage.EXPIRATION_DATE_INVALID.name)
    }

    @Test
    fun `test invalid token return 400 and TOKEN_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()

        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )
        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .response()
            .body
            .asString()
        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }

    @Test
    fun `test empty white list return exception with 500`(){
        // clean white list
        val keyCmd = redisDS.key()
        keyCmd.del(RedisConstant.WHITE_SHORT_LINK_KEY)
            .await().indefinitely()

        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )
        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
            .extract()
            .response()
            .body
            .asString()
        println(body)
    }

    @Test
    fun `test linkInfoRepo insert fail return exception with 500`(){
        val linkInfoRepo = mockk<LinkInfoRepo>()
        QuarkusMock.installMockForType(linkInfoRepo, LinkInfoRepo::class.java)
        every { linkInfoRepo.insertOne(null, any()) } returns Uni.createFrom().failure(RepoException("class name", "fun name", "mock insert error"))

        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )

        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
            .extract()
            .response()
            .body
            .asString()
        println(body)
    }

    @Test
    fun `test LinkInfoExpireTTLRepo create fail return exception with 500` (){
        val linkInfoExpireTTLRepo = mockk<LinkInfoExpireTTLRepo>()
        QuarkusMock.installMockForType(linkInfoExpireTTLRepo, LinkInfoExpireTTLRepo::class.java)
        every { linkInfoExpireTTLRepo.createOrUpdate(null, any(), any(), any()) } returns Uni.createFrom().failure(RepoException("class name", "fun name", "mock insert error"))

        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)

        val command = AddLinkInfoCommand(
            shortLink = null,
            originalLink = "https://www.youtube.com/",
            expirationDate = Date(Date().time + 1000 * 60 * 60 * 24)
        )

        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
            .extract()
            .response()
            .body
            .asString()
        println(body)
    }
    @Test
    fun `test redisRepo insert fail return exception with 500`(){
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.setHash(any(), any(), any()) } returns Uni.createFrom().failure(RepoException("class name", "fun name", "mock insert error"))

        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)


        val command = AddLinkInfoCommand(
            shortLink = "${webConfig.websiteDomain()}/short-link-3",
            originalLink = "https://www.youtube.com/",
            expirationDate = null
        )

        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .post("/add-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
            .extract()
            .response()
            .body
            .asString()
        println(body)
    }
}
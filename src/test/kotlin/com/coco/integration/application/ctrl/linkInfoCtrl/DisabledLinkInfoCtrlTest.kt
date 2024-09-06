package com.coco.integration.application.ctrl.linkInfoCtrl

import com.coco.application.cqrs.command.disabledLinkInfo.ValidateMessage
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.exception.RepoException
import com.coco.infra.grpc.VerifyTokenGrpc
import com.coco.infra.repo.LinkInfoRepo
import com.coco.infra.repo.RedisRepo
import com.coco.integration.infra.repo.SetupData
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-06-下午 12:59
 */
@QuarkusTest
class DisabledLinkInfoCtrlTest {
    @Inject
    lateinit var setupData: SetupData
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
    fun `test valid command with enabled link return 200`(){
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
        given()
            .`when`()
            .queryParams("id", "66c70b72907bc04fd8e9b498")
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    @Test
    fun `test valid command with disable link return 500`(){
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
        given()
            .`when`()
            .queryParams("id", "66c70b62907bc04fd8e9b447")
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

    @Test
    fun `test invalid id return 400 and ID_INVALID`(){
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
        val body = given()
            .`when`()
            .queryParams("id", "invalid")
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        assertTrue(body == ValidateMessage.ID_INVALID.name)
    }


    @Test
    fun `test null token return 400 and TOKEN_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns null
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().item(user)
        val body = given()
            .`when`()
            .queryParams("id", "66c70b62907bc04fd8e9b447")
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        println(body)
        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }

    @Test
    fun `test invalid token return 400 and TOKEN_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val body = given()
            .`when`()
            .queryParams("id", "66c70b62907bc04fd8e9b447")
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }

    @Test
    fun `test disable not-exist link return 500`(){
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
        val nonExistId = "66c70b62907bc04fd8e9b490"
       given()
            .`when`()
            .queryParams("id", nonExistId)
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

    @Test
    fun `test linkInfoRepo update fail return 500`(){
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
        val linkInfoRepo = mockk<LinkInfoRepo>()
        QuarkusMock.installMockForType(linkInfoRepo, LinkInfoRepo::class.java)
        every { linkInfoRepo.insertOne(null, any()) } returns Uni.createFrom().failure(RepoException("class name", "fun name", "mock insert error"))
        given()
            .`when`()
            .queryParams("id", "66c70b72907bc04fd8e9b498")
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

    @Test
    fun `test redisRepo update fail return 500`(){
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
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.setHash(any(), any(), any()) } returns Uni.createFrom().failure(RepoException("class name", "fun name", "mock insert error"))
        given()
            .`when`()
            .queryParams("id", "66c70b72907bc04fd8e9b498")
            .patch("/disable-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

}
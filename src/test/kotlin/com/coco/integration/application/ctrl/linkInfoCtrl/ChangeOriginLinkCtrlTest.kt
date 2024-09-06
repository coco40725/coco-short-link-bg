package com.coco.integration.application.ctrl.linkInfoCtrl

import com.coco.application.cqrs.command.changeOriginLink.ChangeOriginLinkCommand
import com.coco.application.cqrs.command.changeOriginLink.ValidateMessage
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.exception.RepoException
import com.coco.infra.grpc.VerifyTokenGrpc
import com.coco.infra.repo.LinkInfoRepo
import com.coco.integration.infra.repo.SetupData
import io.mockk.every
import io.mockk.mockk
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

/**
@author Yu-Jing
@create 2024-09-06-下午 02:21
 */
@QuarkusTest
class ChangeOriginLinkCtrlTest {
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
    fun `test changeOriginLink with valid command return 200`(){
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

        val command = ChangeOriginLinkCommand(
            "66c70b62907bc04fd8e9b555",
            "https://www.youtube.com/watch?v=jP7VWky-2Ks")

        given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .patch("/change-origin-link-info")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    @Test
    fun `test changeOriginLink with non-exist id return 500`(){
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

        val command = ChangeOriginLinkCommand(
            "66c70b62907bc04fd8e9b000",
            "https://www.youtube.com/watch?v=jP7VWky-2Ks")

        given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .patch("/change-origin-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }



    @Test
    fun `test changeOriginLink with invalid id return 400 and ID_INVALID`(){
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
        val command = ChangeOriginLinkCommand(
            "invalid",
            "https://www.youtube.com/watch?v=jP7VWky-2Ks")

        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .patch("/change-origin-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        assertTrue(body == ValidateMessage.ID_INVALID.name)
    }


    @Test
    fun `test changeOriginLink with invalid original link return 400 and ORIGIN_LINK_INVALID`(){
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
        val command = ChangeOriginLinkCommand(
            "66c70b62907bc04fd8e9b555",
            "xxxxx")

        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .patch("/change-origin-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()

        assertTrue(body == ValidateMessage.ORIGIN_LINK_INVALID.name)
    }


    @Test
    fun `test changeOriginLink with null token return 400 and TOKEN_INVALID`(){
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
        val command = ChangeOriginLinkCommand(
            "66c70b62907bc04fd8e9b555",
            "https://www.youtube.com/watch?v=jP7VWky-2Ks")

        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .patch("/change-origin-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()

        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }

    @Test
    fun `test changeOriginLink with invalid token return 400 and TOKEN_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"

        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val command = ChangeOriginLinkCommand(
            "66c70b62907bc04fd8e9b555",
            "https://www.youtube.com/watch?v=jP7VWky-2Ks")

        val body = given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .patch("/change-origin-link-info")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()

        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
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
        val command = ChangeOriginLinkCommand(
            "66c70b62907bc04fd8e9b555",
            "https://www.youtube.com/watch?v=jP7VWky-2Ks")

        given()
            .`when`()
            .body(command)
            .contentType(ContentType.JSON)
            .patch("/change-origin-link-info")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

}
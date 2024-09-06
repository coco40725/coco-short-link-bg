package com.coco.integration.application.ctrl.linkInfoCtrl

import com.coco.application.cqrs.command.changeExpireDate.ChangeExpireDateCommand
import com.coco.application.cqrs.command.changeExpireDate.ValidateMessage
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.grpc.VerifyTokenGrpc
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
import java.util.*

/**
@author Yu-Jing
@create 2024-09-06-下午 02:43
 */
@QuarkusTest
class ChangeExpireDateCtrlTest {
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
    fun `test changeExpireDate with exist id return 200`(){
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
        val command = ChangeExpireDateCommand(
            "66c70b62907bc04fd8e9b555",
            Date(Date().time + 1000 * 60 * 60 * 24 * 7),
        )

        given()
            .contentType(ContentType.JSON)
            .body(command)
            .`when`()
            .patch("/change-expire-date")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    @Test
    fun `test changeExpireDate with non-exist id return 500`(){
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
        val command = ChangeExpireDateCommand(
            "66c70b62907bc04fd8e9b000",
            Date(Date().time + 1000 * 60 * 60 * 24 * 7),
        )

        given()
            .contentType(ContentType.JSON)
            .body(command)
            .`when`()
            .patch("/change-expire-date")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
    }

    @Test
    fun `test changeExpireDate with invalid id return 400 and ID_INVALID`(){
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
        val command = ChangeExpireDateCommand(
            "invalid",
            Date(Date().time + 1000 * 60 * 60 * 24 * 7),
        )

        val body = given()
            .contentType(ContentType.JSON)
            .body(command)
            .`when`()
            .patch("/change-expire-date")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()

        assertTrue(body == ValidateMessage.ID_INVALID.name)
    }


    @Test
    fun `test changeExpireDate with null token return 400 and TOKEN_INVALID`(){
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
        val command = ChangeExpireDateCommand(
            "66c70b62907bc04fd8e9b555",
            Date(Date().time + 1000 * 60 * 60 * 24 * 7),
        )

        val body = given()
            .contentType(ContentType.JSON)
            .body(command)
            .`when`()
            .patch("/change-expire-date")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }

    @Test
    fun `test changeExpireDate with invalid token return 400 and TOKEN_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        val user = User(
            id = "userId",
            name = "name",
            email = "email",
            password = "password",
            emailVerify = true
        )
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val command = ChangeExpireDateCommand(
            "66c70b62907bc04fd8e9b555",
            Date(Date().time + 1000 * 60 * 60 * 24 * 7),
        )

        val body = given()
            .contentType(ContentType.JSON)
            .body(command)
            .`when`()
            .patch("/change-expire-date")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }
}
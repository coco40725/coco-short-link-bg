package com.coco.integration.application.ctrl.userLinkInfoCtrl

import com.coco.application.cqrs.query.getUserLinkStat.ValidateMessage
import com.coco.application.middleware.auth.JwtRequest
import com.coco.domain.model.User
import com.coco.infra.config.WebConfig
import com.coco.infra.grpc.VerifyTokenGrpc
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
@create 2024-09-06-下午 02:56
 */
@QuarkusTest
class GetUserShortLinkStatCtrlTest {
    @Inject
    lateinit var setupData: SetupData

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
    fun `test valid query return 200`() {
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
            .queryParams("shortLink", "${webConfig.websiteDomain()}/enable-1")
            .get("/user/link-stat")
            .then()
            .statusCode(Response.Status.OK.statusCode)
    }

    @Test
    fun `test invalid shortLink return 400`() {
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
            .queryParams("shortLink", "${webConfig.websiteDomain()}/enable-1@@@@@@@")
            .get("/user/link-stat")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        println("body: $body")
        assertTrue(body == ValidateMessage.SHORT_LINK_INVALID.name)
    }

    @Test
    fun `test null token return 400 with TOKEN_INVALID`(){
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
            .queryParams("shortLink", "${webConfig.websiteDomain()}/enable-1")
            .get("/user/link-stat")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()

        println("body: $body")
        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }

    @Test
    fun `test invalid token return 400 with TOKEN_INVALID`(){
        every { jwt.init(any()) } returns Unit
        every { jwt.token } returns "token"
        every { verifyTokenClient.verifyToken(any()) } returns Uni.createFrom().nullItem()
        val body = given()
            .`when`()
            .queryParams("shortLink", "${webConfig.websiteDomain()}/enable-1")
            .get("/user/link-stat")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.statusCode)
            .extract()
            .body()
            .asString()
        println("body: $body")
        assertTrue(body == ValidateMessage.TOKEN_INVALID.name)
    }
}
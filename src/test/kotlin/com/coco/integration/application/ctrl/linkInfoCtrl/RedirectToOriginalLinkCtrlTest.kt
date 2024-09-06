package com.coco.integration.application.ctrl.linkInfoCtrl

import com.coco.infra.pubsub.LinkLogPubSubSvc
import com.coco.integration.infra.repo.SetupData
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-09-06-上午 11:59
 */

@QuarkusTest
class RedirectToOriginalLinkCtrlTest {

    @Inject
    lateinit var setupData: SetupData

    private val linkLogPubSubSvc = mockk<LinkLogPubSubSvc>()


    @BeforeEach
    fun beforeEach() {
        setupData.beforeEach()
        every { linkLogPubSubSvc.publish(any()) } returns Unit
        QuarkusMock.installMockForType(linkLogPubSubSvc, LinkLogPubSubSvc::class.java)
    }

    @AfterEach
    fun afterEach() {
        setupData.cleanEach()
    }
    @Test
    fun `test exist link return 301`(){
        given()
            .redirects().follow(false)  // 禁用自動跟隨重定向
            .pathParam("shortLink", "enable-1")
            .`when`().get("/{shortLink}/")
            .then()
            .statusCode(Response.Status.MOVED_PERMANENTLY.statusCode)
    }

    @Test
    fun `test non-exist link return 404`(){
        given()
            .pathParam("shortLink", "/not-exist")
            .`when`().get("/{shortLink}")
            .then()
            .statusCode(404)
    }
}
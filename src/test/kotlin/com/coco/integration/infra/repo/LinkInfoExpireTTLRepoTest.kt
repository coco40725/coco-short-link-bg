package com.coco.integration.infra.repo

import com.coco.infra.repo.LinkInfoExpireTTLRepo
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.bson.Document
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
@author Yu-Jing
@create 2024-08-29-下午 05:33
 */
@QuarkusTest
class LinkInfoExpireTTLRepoTest {

    @Inject
    lateinit var linkInfoExpireTTLRepo: LinkInfoExpireTTLRepo

    @Inject
    lateinit var setupData: SetupData

    @BeforeEach
    fun setUp() {
        setupData.beforeEach()
    }

    @AfterEach
    fun cleanUp() {
        setupData.cleanEach()
    }


    @Test
    fun `test findOne success`(){
        val linkInfoId = ObjectId("6161f4b3b3b3b3b3b3b3b3b3")
        val shortLink = "https://example.com/short-link/1"

        val doc = Document.parse("""
            {
                "_id" : "{\"linkInfoId\": \"6161f4b3b3b3b3b3b3b3b3b3\", \"shortLink\": \"https://example.com/short-link/1\"}",
                "expireDate" : ISODate("2024-08-30T18:24:39Z")
            }
        """.trimIndent())

        val subscriber = linkInfoExpireTTLRepo
            .findOne(null, linkInfoId, shortLink)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())

        subscriber
            .awaitItem()
            .assertCompleted()
            .assertItem(doc)
    }

    @Test
    fun `test findOne not found return null`(){
        val linkInfoId = ObjectId("6161f4b3b3b3b3b3b3b3b3b3")
        val shortLink = "https://example.com/short-link/3"
        val subscriber = linkInfoExpireTTLRepo
            .findOne(null, linkInfoId, shortLink)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())

        subscriber
            .awaitItem()
            .assertCompleted()
            .assertItem(null)
    }

    @Test
    fun `test createOrUpdate for create` (){
        val linkInfoId = ObjectId("6161f4b3b3b3b3b3b3b3b3b3")
        val shortLink = "https://example.com/short-link/1-2"
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val expireDate = df.parse("2024-09-25T17:00:00.000Z")


        val updateItem = linkInfoExpireTTLRepo.createOrUpdate(null, linkInfoId, shortLink, expireDate)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item


        linkInfoExpireTTLRepo.findOne(null, linkInfoId, shortLink)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(updateItem)

    }

    @Test
    fun `test createOrUpdate for update` (){
        val linkInfoId = ObjectId("6161f4b3b3b3b3b3b3b3b3b3")
        val shortLink = "https://example.com/short-link/1"
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val expireDate = df.parse("2024-09-25T17:00:00.000Z")


        val updateItem = linkInfoExpireTTLRepo.createOrUpdate(null, linkInfoId, shortLink, expireDate)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item

        linkInfoExpireTTLRepo.findOne(null, linkInfoId, shortLink)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(updateItem)
    }
}
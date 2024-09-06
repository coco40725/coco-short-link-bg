package com.coco.integration.application.service

import com.coco.application.exception.ApplicationException
import com.coco.application.service.CompensationService
import com.coco.application.service.LinkManagementService
import com.coco.domain.model.LinkInfo
import com.coco.domain.service.linkInfo.LinkInfoSvc
import com.coco.infra.config.MongoConfig
import com.coco.infra.listener.LinkInfoExpireTTLListener
import com.coco.infra.repo.LinkInfoExpireTTLRepo
import com.coco.infra.repo.LinkInfoRepo
import com.coco.infra.repo.RedisRepo
import com.coco.integration.infra.repo.SetupData
import com.mongodb.assertions.Assertions.assertFalse
import com.mongodb.assertions.Assertions.assertTrue
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.ClientSession
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.bson.Document
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.reactivestreams.FlowAdapters
import java.util.*

/**
@author Yu-Jing
@create 2024-08-30-下午 12:03
 */

@QuarkusTest
class LinkManagementServiceTest {


    @Inject
    lateinit var linkManagementService: LinkManagementService

    @Inject
    lateinit var mongoClient: ReactiveMongoClient

    @Inject
    lateinit var redisDS: ReactiveRedisDataSource

    @Inject
    lateinit var setupData: SetupData

    @Inject
    lateinit var mongoConfig: MongoConfig

    @BeforeEach
    fun setup(){
        setupData.beforeEach()
    }

    @AfterEach
    fun clean(){
        setupData.cleanEach()
    }

    @Test
    fun `test addLinkInfoLog with expireDate success return LinkInfo`(){
        val now = Date()
        val info = LinkInfo(
            shortLink = "https://example.com/short-link/1",
            userId = "user-1",
            originalLink = "https://example.com/original-link/1",
            expirationDate = Date(now.time + 1000 * 60 * 60 * 24 * 7), // 7 days
            lastUpdateDate = now,
            createDate = now,
            enabled = true
        )

        val result = linkManagementService.addLinkInfoLog(info).await().indefinitely()

        // check if result is not null
        assertTrue(result != null)

        // check if MongoDB LinkInfo data is exist
        val linkInfoData = mongoClient.getDatabase(mongoConfig.database())
            .getCollection("LinkInfo")
            .find(Filters.eq("shortLink", info.shortLink))
            .collect().first()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item
        assertTrue(linkInfoData != null)

        // check if MongoDB TTL data is exist
        val modifiedId = mapOf("linkInfoId" to result!!.id.toString(), "shortLink" to info.shortLink)
        val json = Document(modifiedId).toJson()
        val expireData = mongoClient.getDatabase(mongoConfig.database())
            .getCollection("LinkInfoExpireTTL")
            .find(Filters.eq("_id", json))
            .collect().first()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item
        assertTrue(expireData != null)



        // check if Redis data is exist
        val hashCmd = redisDS.hash(String::class.java)
        val redisData = hashCmd.hget(info.shortLink, "originalLink")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item
        assertTrue(redisData == info.originalLink)

    }


    @Test
    fun `test addLinkInfoLog without expireDate success return LinkInfo`(){
        val now = Date()
        val info = LinkInfo(
            shortLink = "https://example.com/short-link/1",
            userId = "user-1",
            originalLink = "https://example.com/original-link/1",
            expirationDate = null,
            lastUpdateDate = now,
            createDate = now,
            enabled = true
        )

        val result = linkManagementService.addLinkInfoLog(info).await().indefinitely()

        // check if result is not null
        assertTrue(result != null)

        // check if MongoDB LinkInfo data is exist
        val linkInfoData = mongoClient.getDatabase(mongoConfig.database())
            .getCollection("LinkInfo")
            .find(Filters.eq("shortLink", info.shortLink))
            .collect().first()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item
        assertTrue(linkInfoData != null)



        // check if MongoDB TTL data is not exist
        val modifiedId = mapOf("linkInfoId" to result!!.id.toString(), "shortLink" to info.shortLink)
        val json = Document(modifiedId).toJson()

        val expireData = mongoClient.getDatabase(mongoConfig.database())
            .getCollection("LinkInfoExpireTTL")
            .find(Filters.eq("_id", json))
            .collect().first()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item

        assertTrue(expireData == null)


        // check if Redis data is exist
        val hashCmd = redisDS.hash(String::class.java)
        val redisData = hashCmd.hget(info.shortLink, "originalLink")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item

        assertTrue(redisData == info.originalLink)

    }


    @Test
    fun `test disableLinkInfo success return true`(){
        val id = "66c70b72907bc04fd8e9b498"
        val result = linkManagementService.disableLinkInfo(id).await().indefinitely()
        assertTrue(result)
        val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
            .getCollection("LinkInfo")
            .find(Filters.eq("_id", ObjectId(id)))
            .collect().first()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item
        assertFalse(linkInfoItem.getBoolean("enabled"))

    }

    @Test
    fun `test disableLinkInfo fail if id not exist, throw exception`(){
        val id = "66c70b72907bc04fd8e9b496"
        linkManagementService.disableLinkInfo(id)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(ApplicationException::class.java)
    }

    @Test
    fun `test disableLinkInfo fail if  redisRepo delKey fail with Uni then rollback and throw exception`(){
        val id = "66c70b72907bc04fd8e9b498"
        // mock  redisRepo.delKey
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.delKey(any()) } returns Uni.createFrom().failure(RuntimeException("mock exception"))

        try {
            val result = linkManagementService.disableLinkInfo(id).await().indefinitely()
        } catch (e: Exception){
            Thread.sleep(3000)
            // check if result linkInfo rollback to true
            val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
                .getCollection("LinkInfo")
                .find(Filters.eq("_id", ObjectId(id)))
                .collect().first()
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .item
            assertTrue(linkInfoItem.getBoolean("enabled"))
        }

    }

    @Test
    fun `test disableLinkInfo fail if  redisRepo delKey fail then rollback  and throw exception`(){
        val id = "66c70b72907bc04fd8e9b498"
        // mock  redisRepo.delKey
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.delKey(any()) } throws RuntimeException("mock exception")

        try {
            val result = linkManagementService.disableLinkInfo(id).await().indefinitely()
        } catch (e: Exception){
            Thread.sleep(3000)
            // check if result linkInfo rollback to true
            val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
                .getCollection("LinkInfo")
                .find(Filters.eq("_id", ObjectId(id)))
                .collect().first()
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .item
            assertTrue(linkInfoItem.getBoolean("enabled"))
        }

    }

    @Test
    fun `test enabledLinkInfo success return true`(){
        val id = "66c70b62907bc04fd8e9b447"
        val result = linkManagementService.enabledLinkInfo(id).await().indefinitely()
        assertTrue(result)
        val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
            .getCollection("LinkInfo")
            .find(Filters.eq("_id", ObjectId(id)))
            .collect().first()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item
        assertTrue(linkInfoItem.getBoolean("enabled"))

    }

    @Test
    fun `test enabledLinkInfo partial success,if redisRepo setHash fail with Uni, throw exception`(){
        val id = "66c70b62907bc04fd8e9b447"
        // mock redisRepo.delKey
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.setHash(any(), any(), any()) } returns Uni.createFrom().failure(RuntimeException("mock exception"))

        try {
            val result = linkManagementService.enabledLinkInfo(id).await().indefinitely()
        } catch (e: Exception) {
            Thread.sleep(3000)
            val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
                .getCollection("LinkInfo")
                .find(Filters.eq("_id", ObjectId(id)))
                .collect().first()
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .item
            assertTrue(linkInfoItem.getBoolean("enabled"))
        }


    }

    @Test
    fun `test enabledLinkInfo partial success,if redisRepo setHash fail with throw exception`(){
        val id = "66c70b62907bc04fd8e9b447"
        // mock redisRepo.delKey
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.setHash(any(), any(), any()) } throws RuntimeException("mock exception")

        try {
            val result = linkManagementService.enabledLinkInfo(id).await().indefinitely()
        } catch (e: Exception){
            Thread.sleep(3000)
            val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
                .getCollection("LinkInfo")
                .find(Filters.eq("_id", ObjectId(id)))
                .collect().first()
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .item
            assertTrue(linkInfoItem.getBoolean("enabled"))
        }


    }

    @Test
    fun `test enabledLinkInfo fail if id not exist, throw exception`(){
        val id = "66c70b72907bc04fd8e9b455"
        linkManagementService.disableLinkInfo(id)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(ApplicationException::class.java)
    }


    @Test
    fun `test changeOriginLink success`(){
        val id = "66c70b62907bc04fd8e9b497"
        val newOriginalLink = "https://juejin.cn/post/7187979210391027767/new"
        val result = linkManagementService.changeOriginLink(id, newOriginalLink).await().indefinitely()
        assertTrue(result)

        val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
            .getCollection("LinkInfo")
            .find(Filters.eq("_id", ObjectId(id)))
            .collect().first()
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .item

        assertTrue(linkInfoItem.getString("originalLink").equals(newOriginalLink))

    }

    @Test
    fun `test changeOriginLink fail if id not exist, throw exception`(){
        val id = "66c70b72907bc04fd8e9b444"
        val newOriginalLink = "https://juejin.cn/post/7187979210391027767/new"
        linkManagementService.changeOriginLink(id, newOriginalLink)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(ApplicationException::class.java)
    }

    @Test
    fun `test changeOriginLink fail if redisRepo updateHash fail, then rollback and throw exception`(){
        val id = "66c70b62907bc04fd8e9b497"
        val originalLink = "https://juejin.cn/post/7187979210391027767"
        val newOriginalLink = "https://juejin.cn/post/7187979210391027767/new"
        // mock redisRepo.updateHash
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.updateHash(any(), any(), any()) } throws RuntimeException("mock exception")

        try {
            val result = linkManagementService.changeOriginLink(id, newOriginalLink).await().indefinitely()
        }catch (e: Exception){
            Thread.sleep(3000)
            // check if result linkInfo rollback to true
            val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
                .getCollection("LinkInfo")
                .find(Filters.eq("_id", ObjectId(id)))
                .collect().first()
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .item
            assertTrue(linkInfoItem.getString("originalLink").equals(originalLink))
        }
    }

    @Test
    fun `test changeOriginLink fail if redisRepo updateHash fail with Uni, then rollback and throw exception`(){
        val id = "66c70b62907bc04fd8e9b497"
        val originalLink = "https://juejin.cn/post/7187979210391027767"
        val newOriginalLink = "https://juejin.cn/post/7187979210391027767/new"
        // mock redisRepo.updateHash
        val redisRepo = mockk<RedisRepo>()
        QuarkusMock.installMockForType(redisRepo, RedisRepo::class.java)
        every { redisRepo.updateHash(any(), any(), any()) } returns Uni.createFrom().failure(RuntimeException("mock exception"))

        try {
            val result = linkManagementService.changeOriginLink(id, newOriginalLink).await().indefinitely()
        }catch (e: Exception){
            Thread.sleep(3000)
            // check if result linkInfo rollback to true
            val linkInfoItem = mongoClient.getDatabase(mongoConfig.database())
                .getCollection("LinkInfo")
                .find(Filters.eq("_id", ObjectId(id)))
                .collect().first()
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .item
            assertTrue(linkInfoItem.getString("originalLink").equals(originalLink))
        }
    }

}
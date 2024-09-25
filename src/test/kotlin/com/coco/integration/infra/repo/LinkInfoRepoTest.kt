package com.coco.integration.infra.repo

import com.coco.domain.model.LinkInfo
import com.coco.infra.exception.RepoException
import com.coco.infra.repo.LinkInfoRepo
import com.mongodb.client.result.InsertOneResult
import io.mockk.every
import io.mockk.mockk
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.bson.Document
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

/**
@author Yu-Jing
@create 2024-08-29-下午 09:08
 */
@QuarkusTest
class LinkInfoRepoTest {

    @Inject
    lateinit var linkInfoRepo: LinkInfoRepo

    @Inject
    lateinit var setupData: SetupData

    @BeforeEach
    fun setUp() {
        setupData.beforeEach()
    }

    @AfterEach
    fun clean() {
        setupData.cleanEach()
    }



    @Test
    fun `test toDocument with null info`() {
        val info = null
        val result = linkInfoRepo.toDocument(info)
        assertNull(result)
    }

    @Test
    fun `test toDocument with info`() {
        val id = ObjectId()
        val now = Date()
        val info = LinkInfo(
            id = id,
            shortLink = "shortLink",
            userId = "userId",
            originalLink = "originalLink",
            expirationDate = null,
            lastUpdateDate = null,
            createDate = now,
            enabled = true
        )
        val result = linkInfoRepo.toDocument(info)
        assertEquals(id.toString(), result?.get("_id").toString())
        assertEquals("shortLink", result?.getString("shortLink"))
        assertEquals("userId", result?.getString("userId"))
        assertEquals("originalLink", result?.getString("originalLink"))
        assertNull(result?.get("expirationDate"))
        assertEquals(now, result?.getDate("createDate"))
        assertNull(result?.get("lastUpdateDate"))
        assertEquals(true, result?.getBoolean("enabled"))
    }

    @Test
    fun `test getOneByEnableShortLink with not exist shortLink return null`() {
        val shortLink = "xxx"
        val result = linkInfoRepo.getOneByEnableShortLink(null, shortLink)
            .await()
            .indefinitely()
        assertNull(result)
    }

    @Test
    fun `test getOneByEnableShortLink with exist enabled shortLink return data`(){

        val info = LinkInfo(
            id = ObjectId("66c70b72907bc04fd8e9b498"),
            shortLink = "https://short.coco-dev.com/fish-123",
            userId = "4af92391-a6b9-4e7d-b020-3c875b752e39",
            originalLink =  "https://smallrye.io/smallrye-mutiny/2.0.0/reference/migrating-to-mutiny-2/",
            expirationDate = null,
            lastUpdateDate =  Date.from(Instant.parse("2024-08-29T10:33:21Z")),
            createDate = Date.from(Instant.parse("2024-08-22T09:57:06Z")),
            enabled = true
        )
        val shortLink = "https://short.coco-dev.com/fish-123"

        val result = linkInfoRepo.getOneByEnableShortLink(null, shortLink)
            .await()
            .indefinitely()
        assertEquals(info, result)
    }

    @Test
    fun `test getOneByEnableShortLink with exist disabled shortLink return null`(){

        val shortLink = "https://short.coco-dev.com/oA2UjxD-1111"

        val result = linkInfoRepo.getOneByEnableShortLink(null, shortLink)
            .await()
            .indefinitely()
        assertNull(result)
    }

    @Test
    fun `test getOneByShortLink with not exist shortLink return null`() {
        val shortLink = "https://short.coco-dev.com/123"

        val result = linkInfoRepo.getOneByShortLink(null, shortLink)
            .await()
            .indefinitely()
        assertNull(result)
    }

    @Test
    fun `test getOneById with not exist id return null`() {
        val id = "66c70b62907bc04fd8e9b499"

        val result = linkInfoRepo.getOneById(null, id)
            .await()
            .indefinitely()
        assertNull(result)
    }

    @Test
    fun `test getOneById with exist id return data`(){
        val id = "66c70b62907bc04fd8e9b497"
        val info = LinkInfo(
            id = ObjectId("66c70b62907bc04fd8e9b497"),
            shortLink = "https://short.coco-dev.com/oA2UjxD",
            userId = "4af92391-a6b9-4e7d-b020-3c875b752e38",
            originalLink =  "https://juejin.cn/post/7187979210391027767",
            expirationDate = Date.from(Instant.parse("2024-08-25T12:14:43Z")),
            lastUpdateDate =  Date.from(Instant.parse("2024-08-25T12:15:24Z")),
            createDate = Date.from(Instant.parse("2024-08-22T09:56:50Z")),
            enabled = true
        )
        val result = linkInfoRepo.getOneById(null, id)
            .await()
            .indefinitely()
        assertEquals(info, result)
    }

    @Test
    fun `test checkShortLinksExist with not exist shortLinks return empty list`() {
        val shortLinks = listOf("xxx", "yyy")

        val result = linkInfoRepo.checkShortLinksExist(null, shortLinks)
            .await()
            .indefinitely()
        assertEquals(0, result.size)
    }

    @Test
    fun `test checkShortLinksExist with exist shortLinks return data`(){
        val shortLinks = listOf("https://short.coco-dev.com/oA2UjxD", "https://short.coco-dev.com/fish-123")

        val result = linkInfoRepo.checkShortLinksExist(null, shortLinks)
            .await()
            .indefinitely()
        assertEquals(shortLinks.toSet(), result.toSet())
    }

    @Test
    fun `test getManyByUserId with not exist userId return empty list`() {
        val userId = "xxx"

        val result = linkInfoRepo.getManyByUserId(null, userId)
            .await()
            .indefinitely()
        assertEquals(0, result.size)
    }

    @Test
    fun `test getManyByUserId with exist userId return data`(){
        val userId = "4af92391-a6b9-4e7d-b020-3c875b752e38"
        val info1 = LinkInfo(
            id = ObjectId("66c70b62907bc04fd8e9b497"),
            shortLink = "https://short.coco-dev.com/oA2UjxD",
            userId = "4af92391-a6b9-4e7d-b020-3c875b752e38",
            originalLink =  "https://juejin.cn/post/7187979210391027767",
            expirationDate = Date.from(Instant.parse("2024-08-25T12:14:43Z")),
            lastUpdateDate =  Date.from(Instant.parse("2024-08-25T12:15:24Z")),
            createDate = Date.from(Instant.parse("2024-08-22T09:56:50Z")),
            enabled = true
        )
        val result = linkInfoRepo.getManyByUserId(null, userId)
            .await()
            .indefinitely()
        assertEquals(listOf(info1), result)
    }

    @Test
    fun `test insertOne with info`() {
        val now = Date()
        val id = ObjectId()
        val info = LinkInfo(
            id = id,
            shortLink = "shortLink",
            userId = "userId",
            originalLink = "originalLink",
            expirationDate = null,
            lastUpdateDate = null,
            createDate = now,
            enabled = true
        )
        val insertedItem = linkInfoRepo.insertOne(null, info)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .item

        val result = linkInfoRepo.getOneById(null, id.toString())
            .await()
            .indefinitely()
        assertEquals(info, result)
    }


    @Test
    fun `test removeExpireDate if linkInfoId exist then return true`(){
        val id = ObjectId("66c70b62907bc04fd8e9b497")
        val result = linkInfoRepo.removeExpireDate(null, id)
            .await()
            .indefinitely()
        assertEquals(true, result)
    }

    @Test
    fun `test removeExpireDate if linkInfoId not exist then return RepoException`(){
        val id = ObjectId("66c70b62907bc04fd8e9b499")
        linkInfoRepo.removeExpireDate(null, id)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(RepoException::class.java)
    }

}
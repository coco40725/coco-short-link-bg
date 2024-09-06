package com.coco.integration.infra.repo

import com.coco.infra.config.MongoConfig
import com.coco.infra.config.WebConfig
import com.coco.infra.constant.RedisConstant
import com.coco.infra.util.Log
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.Document

/**
@author Yu-Jing
@create 2024-08-29-下午 05:38
 */
@ApplicationScoped
class SetupData @Inject constructor(
    private val mongoClient: ReactiveMongoClient,
    private val redisDS: ReactiveRedisDataSource,
    private val mongoConfig: MongoConfig,
    private val webConfig: WebConfig
){
    /**
     * 初始化單元測試用的 DB
     */
    fun beforeEach() {
        println("[TEST] *** Before All ***")
        setLinkInfoExpireTTLData()
        setLinkInfoData()
        setShortLinkRedisData()
    }


    /**
     * 清理單元測試用的 DB
     */
    fun cleanEach(){
        println("[TEST] *** Clean All ***")
        cleanLinkInfoExpireTTLData()
        cleanLinkInfoData()
        cleanShortLinkRedisData()
    }

    private fun setLinkInfoExpireTTLData(){
        val col = mongoClient
            .getDatabase(mongoConfig.database())
            .getCollection("LinkInfoExpireTTL")

        col.insertMany(listOf(Document.parse("""
            {
                "_id" : "{\"linkInfoId\": \"6161f4b3b3b3b3b3b3b3b3b3\", \"shortLink\": \"https://example.com/short-link/1\"}",
                "expireDate" : ISODate("2024-08-30T18:24:39Z")
            }

        """.trimIndent()), Document.parse("""
            {
                "_id" : "{\"linkInfoId\": \"66cdef975f04fa20a85ad6a8\", \"shortLink\": \"https://example.com/short-link/2\"}",
                "expireDate" : ISODate("2024-08-30T18:24:39Z")
            }

        """.trimIndent())))
            .await()
            .indefinitely()

    }
    private fun cleanLinkInfoExpireTTLData(){
        val col = mongoClient
            .getDatabase(mongoConfig.database())
            .getCollection("LinkInfoExpireTTL")

        col.drop()
            .await()
            .indefinitely()
    }


    private fun setLinkInfoData(){
        val col = mongoClient
            .getDatabase(mongoConfig.database())
            .getCollection("LinkInfo")

        col.insertMany(listOf(Document.parse("""
            {
                "_id" : ObjectId("66c70b62907bc04fd8e9b497"),
                "shortLink" : "https://short.coco-dev.com/oA2UjxD",
                "userId" : "4af92391-a6b9-4e7d-b020-3c875b752e38",
                "originalLink" : "https://juejin.cn/post/7187979210391027767",
                "lastUpdateDate" : ISODate("2024-08-25T12:15:24Z"),
                "createDate" : ISODate("2024-08-22T09:56:50Z"),
                "enabled" : true,
                "expirationDate" : ISODate("2024-08-25T12:14:43Z")
            }

        """.trimIndent()),
            Document.parse("""
            {
                "_id" : ObjectId("66c70b72907bc04fd8e9b498"),
                "shortLink" : "https://short.coco-dev.com/fish-123",
                "userId" : "4af92391-a6b9-4e7d-b020-3c875b752e39",
                "originalLink" : "https://smallrye.io/smallrye-mutiny/2.0.0/reference/migrating-to-mutiny-2/",
                "lastUpdateDate" : ISODate("2024-08-29T10:33:21Z"),
                "createDate" : ISODate("2024-08-22T09:57:06Z"),
                "enabled" : true
            }

        """.trimIndent()),
            Document.parse("""
            {
                "_id" : ObjectId("66c70b62907bc04fd8e9b447"),
                "shortLink" : "https://short.coco-dev.com/oA2UjxD-1111",
                "userId" : "4af92391-a6b9-4e7d-b020-3c875b752e37",
                "originalLink" : "https://juejin.cn/post/7187979210391027767",
                "lastUpdateDate" : ISODate("2024-08-25T12:15:24Z"),
                "createDate" : ISODate("2024-08-22T09:56:50Z"),
                "enabled" : false,
                "expirationDate" : ISODate("2024-08-25T12:14:43Z")
            }
            """.trimIndent()),
            Document.parse("""
               {
                "_id" : ObjectId("66c70b62907bc04fd8e9b555"),
                "shortLink" : "${webConfig.websiteDomain()}/disable-1",
                "userId" : "user-123",
                "originalLink" : "https://juejin.cn/post/7187979210391027767",
                "lastUpdateDate" : ISODate("2024-08-25T12:15:24Z"),
                "createDate" : ISODate("2024-08-22T09:56:50Z"),
                "enabled" : false
            } 
            """.trimIndent()),
            Document.parse("""
            {
                "_id" : ObjectId("66c70b62907bc04fd8e9b566"),
                "shortLink" : "${webConfig.websiteDomain()}/enable-1",
                "userId" : "user-123",
                "originalLink" : "https://juejin.cn/post/7187979210391027767",
                "lastUpdateDate" : ISODate("2024-08-25T12:15:24Z"),
                "createDate" : ISODate("2024-08-22T09:56:50Z"),
                "enabled" : true
            }  
            """.trimIndent())
        )).await().indefinitely()
    }
    private fun cleanLinkInfoData(){
        val col = mongoClient
            .getDatabase(mongoConfig.database())
            .getCollection("LinkInfo")

        col.drop()
            .await()
            .indefinitely()
    }

    private fun setShortLinkRedisData(){
        val hashCmd = redisDS.hash(String::class.java)
        hashCmd.hset("https://short.coco-dev.com/oA2UjxD", "originalLink", "https://short.coco-dev.com/oA2UjxD/original")
            .await().indefinitely()
        hashCmd.hset("https://short.coco-dev.com/fish-123", "originalLink", "https://short.coco-dev.com/fish-123/original")
            .await().indefinitely()
        hashCmd.hset("${webConfig.websiteDomain()}/enable-1", "originalLink", "https://quarkus.io/blog/mocking/")
            .await().indefinitely()
        hashCmd.hset("${webConfig.websiteDomain()}/disable-1", "originalLink", "https://quarkus.io/blog/mocking/")
            .await().indefinitely()


        val listCmd = redisDS.list(String::class.java)
        listCmd.lpush(RedisConstant.WHITE_SHORT_LINK_KEY, "https://short.coco-dev.com/zzz1234", "https://short.coco-dev.com/aaa1234")
            .await().indefinitely()
    }

    private fun cleanShortLinkRedisData(){
        val keyCmd = redisDS.key()
        val allKeys = keyCmd.keys("*")
            .await().indefinitely()

        allKeys.forEach { key ->
            keyCmd.del(key)
                .await().indefinitely()
        }
    }
}
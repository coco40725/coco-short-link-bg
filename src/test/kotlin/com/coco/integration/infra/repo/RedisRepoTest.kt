package com.coco.integration.infra.repo

import com.coco.infra.constant.RedisConstant
import com.coco.infra.exception.RepoException
import com.coco.infra.repo.RedisRepo
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-08-30-上午 09:55
 */

@QuarkusTest
class RedisRepoTest {

    @Inject
    lateinit var redisRepo: RedisRepo

    @Inject
    lateinit var setupData: SetupData

    @BeforeEach
    fun setUp(){
        setupData.beforeEach()
    }

    @AfterEach
    fun clean(){
        setupData.cleanEach()
    }

    @Test
    fun `test getHash with exist key return original link`(){
        redisRepo.getHash("https://short.coco-dev.com/oA2UjxD", RedisConstant.ORIGINAL_LINK_FIELD)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem("https://short.coco-dev.com/oA2UjxD/original")
    }

    @Test
    fun `test getHash with not exist key return null`(){
        redisRepo.getHash("https://short.coco-dev.com/xxxxxx", RedisConstant.ORIGINAL_LINK_FIELD)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(null)
    }


    @Test
    fun `test setHash with not exist key return true`(){
        redisRepo.setHash("xxxx", RedisConstant.ORIGINAL_LINK_FIELD, "uuu")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(true)
    }

    @Test
    fun `test setHash with exist key return RepoException`(){
        redisRepo.setHash("https://short.coco-dev.com/oA2UjxD", RedisConstant.ORIGINAL_LINK_FIELD, "xx")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(RepoException::class.java)
    }

    @Test
    fun `test updateHash with exist key return true`(){
        redisRepo.updateHash("https://short.coco-dev.com/oA2UjxD", "originalLink", "xx")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(true)
    }

    @Test
    fun `test updateHash with not exist key return RepoException`(){
        redisRepo.updateHash("xxxx", RedisConstant.ORIGINAL_LINK_FIELD, "xx")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(RepoException::class.java)
    }

    @Test
    fun `test updateHash with not exist field return RepoException`(){
        redisRepo.updateHash("https://short.coco-dev.com/oA2UjxD", "xxxx", "xx")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(RepoException::class.java)
    }


    @Test
    fun `test delKey with exist key return true`(){
        redisRepo.delKey("https://short.coco-dev.com/oA2UjxD")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(true)
    }

    @Test
    fun `test delKey with not exist key return RepoException`(){
        redisRepo.delKey("xxx")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitFailure()
            .assertFailedWith(RepoException::class.java)
    }

    @Test
    fun `test getList with exist key return list`(){
        redisRepo.getList(RedisConstant.WHITE_SHORT_LINK_KEY)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(listOf("https://short.coco-dev.com/aaa1234", "https://short.coco-dev.com/zzz1234"))
    }

    @Test
    fun `test getList with not exist key return empty`(){
        redisRepo.getList("xxx")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(listOf())
    }

    @Test
    fun `test popFirstElementFromList with exist key, and not empty list return first element`(){
        redisRepo.popFirstElementFromList(RedisConstant.WHITE_SHORT_LINK_KEY)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem("https://short.coco-dev.com/aaa1234")
    }

    @Test
    fun `test popFirstElementFromList with  exist key but empty list return null`(){
        redisRepo.popFirstElementFromList("emptyList")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(null)
    }

    @Test
    fun `test popFirstElementFromList with not exist key return null`(){
        redisRepo.popFirstElementFromList("emptyList")
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(null)
    }

    @Test
    fun `test addElementToList with exist key return list size`(){
        redisRepo.addElementToList(RedisConstant.WHITE_SHORT_LINK_KEY, listOf("https://short.coco-dev.com/abc"))
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(3)

    }

    @Test
    fun `test addElementToList with not exist key return list size`(){
        redisRepo.addElementToList("xxx", listOf("https://short.coco-dev.com/abc"))
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(1)
    }
}
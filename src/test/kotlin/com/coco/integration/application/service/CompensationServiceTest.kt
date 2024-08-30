package com.coco.integration.application.service

import com.coco.application.service.CompensationService
import com.coco.domain.model.CompensationActions
import com.coco.infra.config.MongoConfig
import com.coco.integration.infra.repo.SetupData
import com.mongodb.assertions.Assertions.assertTrue
import com.mongodb.client.model.Filters
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

/**
@author Yu-Jing
@create 2024-08-30-上午 11:31
 */

@QuarkusTest
class CompensationServiceTest {


    @Inject
    lateinit var compensationService: CompensationService

    @Inject
    lateinit var mongoClient: ReactiveMongoClient

    @Inject
    lateinit var setupData: SetupData

    @Inject
    lateinit var mongoConfig: MongoConfig

    @AfterEach
    fun clean(){
        setupData.cleanEach()
    }

    @Test
    fun `test executeCompensation execute function with order and all function are executed successfully`(){
        val list = mutableListOf<String>()
        val actions = listOf(
            CompensationActions("function1", listOf(), {
                list.add("function1")
                Uni.createFrom().item("function1")
            }),
            CompensationActions("function2", listOf(), {
                list.add("function2")
                Uni.createFrom().item("function2")
            }),
            CompensationActions("function3", listOf(), {
                list.add("function3")
                Uni.createFrom().item("function3")
            })
        )
        compensationService.executeCompensation(actions).await().indefinitely()
        assertTrue(list == listOf("function1", "function2", "function3"))

    }

    @Test
    fun `test executeCompensation with one function failed then write log to ErrorLog`(){
        val list = mutableListOf<String>()
        val actions = listOf(
            CompensationActions("function1", listOf(), {
                list.add("function1")
                Uni.createFrom().item("function1")
            }),
            CompensationActions("function2", listOf(), {
                Uni.createFrom().failure<String>(RuntimeException("function2 failed"))
            }),
            CompensationActions("function3", listOf(), {
                list.add("function3")
                Uni.createFrom().item("function3")
            }),
        )

        compensationService.executeCompensation(actions).await().indefinitely()


        assertTrue(list == listOf("function1", "function3"))

        // check error log
        val col = mongoClient
            .getDatabase(mongoConfig.database())
            .getCollection("ErrorLogs")


        col.find(Filters.eq("functionName", "function2"))
            .collect().first()
            .invoke { it ->
                assertTrue(it != null)
            }
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()



    }


}
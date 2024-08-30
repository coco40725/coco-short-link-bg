package com.coco.integration.infra.repo

import com.coco.domain.model.ErrorLog
import com.coco.infra.repo.ErrorLogRepo
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import java.util.Date

/**
@author Yu-Jing
@create 2024-08-29-下午 04:51
 */
@QuarkusTest
class ErrorLogRepoTest {

    @Inject
    lateinit var errorLogRepo: ErrorLogRepo

    @Test
    fun `test addOneErrorLog `(){
        val errorLog = ErrorLog("test", listOf(), Date())

        errorLogRepo.addOneErrorLog(errorLog)
            .subscribe()
            .withSubscriber(UniAssertSubscriber.create())
            .awaitItem()
            .assertCompleted()
            .assertItem(true)
    }

}
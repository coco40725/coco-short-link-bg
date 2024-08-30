package com.coco.infra.repo

import com.coco.domain.model.ErrorLog
import com.coco.infra.config.MongoConfig
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
@author Yu-Jing
@create 2024-08-27-下午 08:18
 */

@ApplicationScoped
class ErrorLogRepo @Inject constructor(
    private val mongoClient: ReactiveMongoClient,
    private val mongoConfig: MongoConfig
) {
    private val collection = mongoClient
        .getDatabase(mongoConfig.database())
        .getCollection("ErrorLogs", ErrorLog::class.java)



    fun addOneErrorLog(errorLog: ErrorLog): Uni<Boolean> {
        return collection.insertOne(errorLog).map { it.wasAcknowledged() }
    }
}
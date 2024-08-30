package com.coco.infra.repo

import com.coco.domain.model.LinkLog
import com.coco.infra.config.MongoConfig
import com.mongodb.ReadPreference
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.Document

/**
@author Yu-Jing
@create 2024-08-12-下午 04:52
 */
@ApplicationScoped
class LinkLogRepo @Inject constructor(
    private val mongoClient: ReactiveMongoClient,
    private val mongoConfig: MongoConfig
){

    private val readCol = mongoClient
        .getDatabase(mongoConfig.database())
        .getCollection("LinkLog")
        .withReadPreference(ReadPreference.secondaryPreferred())

    private val writeCol = mongoClient
        .getDatabase(mongoConfig.database())
        .getCollection("LinkLog")

    private fun toDocument(log: LinkLog?): Document? {
        if (log == null) return null

        // if null then not add to map
        val mapData = mutableMapOf<String, Any?>()
        log.id?.let { mapData["_id"] = it }
        log.shortLink?.let { mapData["shortLink"] = it }
        log.refererIP?.let { mapData["refererIP"] = it }
        log.userAgent?.let { mapData["userAgent"] = it }
        log.referer?.let { mapData["referer"] = it }
        log.createDate?.let { mapData["createDate"] = it }

        return Document(mapData)
    }

    private fun toObject(document: Document?): LinkLog? {
        if (document == null) return null
        return LinkLog(
            id = document.getString("_id"),
            shortLink = document.getString("shortLink"),
            refererIP = document.getString("refererIP"),
            userAgent = document.getString("userAgent"),
            referer = document.getString("referer"),
            createDate = document.getDate("createDate")
        )
    }
    fun insetOne(log: LinkLog): Uni<LinkLog?> {
        val document = toDocument(log)

        return writeCol.insertOne(document).map { it ->
            if (it.wasAcknowledged()) {
                log.id = document?.getString("_id")
                log
            } else {
                null
            }
        }
    }
}
package com.coco.infra.repo

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.model.*
import com.mongodb.reactivestreams.client.ClientSession
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.Document
import org.bson.types.ObjectId
import java.util.*
import java.util.concurrent.TimeUnit

/**
@author Yu-Jing
@create 2024-08-11-下午 04:28
 */
@ApplicationScoped
class LinkInfoExpireTTLRepo @Inject constructor(
    private val mongoClient: ReactiveMongoClient,
) {
    private val writeCol = mongoClient
        .getDatabase("short-link-db")
        .getCollection("LinkInfoExpireTTL")

    private val readCol = mongoClient
        .getDatabase("short-link-db")
        .getCollection("LinkInfoExpireTTL")


    private val expireTTLExpire = IndexOptions().name("expire")
        .expireAfter(0L, TimeUnit.SECONDS)
        .background(true)

    @PostConstruct
    fun init(){
        writeCol.createIndex(Indexes.ascending("expireDate"), expireTTLExpire).subscribe().with { _ -> }
    }

    fun findOne(session: ClientSession? = null, linkInfoId: ObjectId, shortLink: String): Uni<Document> {
        val modifiedId = mapOf("linkInfoId" to linkInfoId.toString(), "shortLink" to shortLink)
        val json = Document(modifiedId).toJson()
        return readCol.find(session, Filters.eq("_id", json)).collect().first()

    }

    fun deleteOrUpdateOne(session: ClientSession? = null, linkInfoId: ObjectId, shortLink: String, expireDate: Date): Uni<Document> {
        val updates = listOf(
            Updates.set("expireDate", expireDate)
        )

        val modifiedId = mapOf("linkInfoId" to linkInfoId.toString(), "shortLink" to shortLink)
        val json = Document(modifiedId).toJson()
        return if (session != null) {
            writeCol.findOneAndUpdate(
                session,
                Filters.eq("_id", json),
                updates,
                FindOneAndUpdateOptions()
                    .returnDocument(ReturnDocument.AFTER)
                    .upsert(false)
            )
        } else {
            writeCol.findOneAndUpdate(
                Filters.eq("_id", json),
                updates,
                FindOneAndUpdateOptions()
                    .returnDocument(ReturnDocument.AFTER)
                    .upsert(false)
            )
        }
    }

    fun createOrUpdate(session: ClientSession? = null, linkInfoId: ObjectId, shortLink: String, expireDate: Date): Uni<Document> {
        val updates = listOf(
            Updates.set("expireDate", expireDate)
        )

        val modifiedId = mapOf("linkInfoId" to linkInfoId.toString(), "shortLink" to shortLink)
        val json = Document(modifiedId).toJson()
        return if (session != null) {
            writeCol.findOneAndUpdate(
                session,
                Filters.eq("_id", json),
                updates,
                FindOneAndUpdateOptions()
                    .returnDocument(ReturnDocument.AFTER)
                    .upsert(true)
            )
        } else {
            writeCol.findOneAndUpdate(
                Filters.eq("_id", json),
                updates,
                FindOneAndUpdateOptions()
                    .returnDocument(ReturnDocument.AFTER)
                    .upsert(true)
            )
        }
    }
}
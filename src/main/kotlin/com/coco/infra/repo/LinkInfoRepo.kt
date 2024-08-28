package com.coco.infra.repo

import com.coco.domain.model.LinkInfo
import com.coco.infra.exception.RepoException
import com.mongodb.ReadPreference
import com.mongodb.client.model.*
import com.mongodb.reactivestreams.client.ClientSession
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import java.util.*


/**
@author Yu-Jing
@create 2024-08-10-下午 04:58
 */
@ApplicationScoped
class LinkInfoRepo @Inject constructor(
    private val mongoClient: ReactiveMongoClient,
){
    private val readCol = mongoClient
        .getDatabase("short-link-db")
        .getCollection("LinkInfo")
        .withReadPreference(ReadPreference.primary())

    private val writeCol = mongoClient
        .getDatabase("short-link-db")
        .getCollection("LinkInfo")

    private val shortLinkIndex = IndexOptions().name("shortLink")
        .unique(true)
        .background(true)

    private val className = this::class.simpleName

    @PostConstruct
    fun init(){
        writeCol.createIndex(Indexes.ascending("shortLink"), shortLinkIndex).subscribe().with { _ -> }
    }

    private fun createUpdates(info: LinkInfo): List<Bson> {
        val updates = mutableListOf<Bson>()
        info.originalLink?.let { updates.add(Updates.set("originalLink", it)) }
        info.expirationDate?.let { updates.add(Updates.set("expirationDate", it)) }
        info.enabled?.let { updates.add(Updates.set("enabled", it)) }

        updates.add(Updates.set("lastUpdateDate", Date()))
        return updates
    }

     fun toDocument(info: LinkInfo?): Document?{
        if (info == null) return null
        val mapData = mutableMapOf<String, Any>()
        info.id?.let { mapData["_id"] = it }
        info.shortLink?.let { mapData["shortLink"] = it }
        info.userId?.let { mapData["userId"] = it }
        info.originalLink?.let { mapData["originalLink"] = it }
        info.expirationDate?.let { mapData["expirationDate"] = it }
        info.lastUpdateDate?.let { mapData["lastUpdateDate"] = it }
        info.createDate?.let { mapData["createDate"] = it }
        info.enabled?.let { mapData["enabled"] = it }

        return Document(mapData)
    }

    private fun toObject(document: Document?): LinkInfo? {
        if (document == null) return null
        return LinkInfo(
            id = document.getObjectId("_id"),
            shortLink = document.getString("shortLink"),
            userId = document.getString("userId"),
            originalLink = document.getString("originalLink"),
            expirationDate = document.getDate("expirationDate"),
            lastUpdateDate = document.getDate("lastUpdateDate"),
            createDate = document.getDate("createDate"),
            enabled = document.getBoolean("enabled")
        )
    }


    fun getOneByEnableShortLink(session: ClientSession? = null , shortLink: String): Uni<LinkInfo?> {
        val colOperation = if (session != null) {
            readCol.find(
                session,
                Filters.and(
                    Filters.eq("shortLink", shortLink),
                    Filters.eq("enabled", true)
                )
            )
        } else {
            readCol.find(
                Filters.and(
                    Filters.eq("shortLink", shortLink),
                    Filters.eq("enabled", true)
                )
            )
        }
        return colOperation.collect().asList().map { toObject(it.firstOrNull()) }
    }

    fun getOneByShortLink(session: ClientSession? = null, shortLink: String): Uni<LinkInfo?> {
        val colOperation = if (session != null) {
            readCol.find(
                session,
                Filters.eq("shortLink", shortLink)
            )
        } else {
            readCol.find(
                Filters.eq("shortLink", shortLink)
            )
        }

        return colOperation.collect().asList().map { toObject(it.firstOrNull()) }
    }


    fun getOneById(session: ClientSession? = null, id: String): Uni<LinkInfo?> {
        val colOperation = if (session != null) {
            readCol.find(
                session,
                Filters.eq("_id", ObjectId(id)))
        } else {
            readCol.find(
                Filters.eq("_id", ObjectId(id)))
        }
        return colOperation.collect().first().map { toObject(it) }
    }

    fun checkShortLinksExist(session: ClientSession? = null, shortLinks: List<String>): Uni<List<String>> {
        val colOperation = if (session != null) {
            readCol.find(
                session,
                Filters.`in`("shortLink", shortLinks)
            )
        } else {
            readCol.find(
                Filters.`in`("shortLink", shortLinks)
            )
        }

        return colOperation.collect().asList().map { existDoc ->
            val existShortLinks = existDoc.map { it.getString("shortLink") }
            existShortLinks
        }
    }

    fun getManyByUserId(session: ClientSession? = null, userId: String): Uni<List<LinkInfo>> {
        val colOperation = if (session != null) {
            readCol.find(
                session,
                Filters.eq("userId", userId)
            )
        } else {
            readCol.find(
                Filters.eq("userId", userId)
            )
        }
        return colOperation.collect().asList().map { docList  ->
            docList.mapNotNull { toObject(it) }
        }

    }

    fun insertOne(session: ClientSession? = null, info: LinkInfo): Uni<LinkInfo> {
        val document = toDocument(info)
        val colOperation = if (session != null) {
            writeCol.insertOne(session, document)
        } else {
            writeCol.insertOne(document)
        }

        return colOperation.chain { it ->
            if (it.wasAcknowledged()) {
                val insertId = document?.getObjectId("_id")
                info.id = insertId
                Uni.createFrom().item(info)
            } else {
                Uni.createFrom().failure(RepoException(
                    className,
                    this::insertOne.name,
                    "Failed to insert linkInfo: $info"
                ))
            }
        }
    }


    fun deleteOne(session: ClientSession? = null, id: ObjectId): Uni<ObjectId> {
        val colOperation = if (session != null) {
            writeCol.deleteOne(session, Filters.eq("_id", id))
        } else {
            writeCol.deleteOne(Filters.eq("_id", id))
        }
        return colOperation
            .chain { it ->
                if (it.wasAcknowledged()) {
                    Uni.createFrom().item(id)
                } else {
                    Uni.createFrom().failure(RepoException(
                        className,
                        this::insertOne.name,
                        "Failed to delete linkInfo_id: $id"
                    ))
                }
            }
    }

    fun updateOne(session: ClientSession? = null,linkInfo: LinkInfo): Uni<LinkInfo> {
        val id = linkInfo.id
        val update = createUpdates(linkInfo)
        val colOperation = if (session != null) {
            writeCol.findOneAndUpdate(
                session,
                Filters.eq("_id", id),
                update,
                FindOneAndUpdateOptions()
                    .upsert(false)
                    .returnDocument(ReturnDocument.AFTER)
            )
        } else {
            writeCol.findOneAndUpdate(
                Filters.eq("_id", id),
                update,
                FindOneAndUpdateOptions()
                    .upsert(false)
                    .returnDocument(ReturnDocument.AFTER)
            )
        }

        return colOperation.chain { it ->
            if (it == null) {
                Uni.createFrom().failure(RepoException(
                    className,
                    this::updateOne.name,
                    "Failed to update linkInfo: $linkInfo"
                ))
            } else {
                Uni.createFrom().item(toObject(it))
            }

        }
    }

    fun removeExpireDate(session: ClientSession? = null, id: ObjectId): Uni<Boolean> {
        val update = Updates.unset("expirationDate")
        val colOperation = if (session != null) {
            writeCol.updateOne(
                session,
                Filters.eq("_id", id),
                update
            )
        } else {
            writeCol.updateOne(
                Filters.eq("_id", id),
                update
            )
        }
        return colOperation.chain { it ->
            if (!it.wasAcknowledged()) {
                Uni.createFrom().failure(RepoException(
                    className,
                    this::removeExpireDate.name,
                    "Failed to remove expireDate: $id"
                ))
            } else {
                Uni.createFrom().item(true)
            }
        }
    }
}
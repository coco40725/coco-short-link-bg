package com.coco.infra.listener

import com.coco.domain.model.LinkInfo
import com.coco.infra.config.MongoConfig
import com.coco.infra.repo.LinkInfoRepo
import com.coco.infra.repo.RedisRepo
import com.mongodb.client.model.changestream.OperationType
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.runtime.Startup
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-下午 04:17
 */


@ApplicationScoped
@Startup
class LinkInfoExpireTTLListener @Inject constructor(
    private val mongoClient: ReactiveMongoClient,
    private val linkInfoRepo: LinkInfoRepo,
    private val redisRepo: RedisRepo,
    private val mongoConfig: MongoConfig
){

    private val col = mongoClient
        .getDatabase(mongoConfig.database())
        .getCollection("LinkInfoExpireTTL")


    @PostConstruct
    fun startListening(){
        col.watch().subscribe().with { changeStreamDocument ->
            // if delete
            val operationType = changeStreamDocument.operationType
            if (operationType == OperationType.DELETE){
                val deleteLinkInfoId = changeStreamDocument.documentKey?.getString("_id")?.value.toString()
                updateLinkInfoAndCache(deleteLinkInfoId).subscribe().with { _ -> }
            }
        }
    }

    private fun updateLinkInfoAndCache(jsonId: String): Uni<Unit> {
        val document = Document.parse(jsonId)
        val linkInfoId = document.getString("linkInfoId")
        val shortLink = document.getString("shortLink")

        // check the linkInfo is expired
        val checkExpireUni = linkInfoRepo.getOneById(null, linkInfoId).map {
            it?.expirationDate != null && it.expirationDate!!.before(Date())
        }.memoize().indefinitely()
        
        // update linkInfo enabled to false
        val updatedLinkInfo = LinkInfo(
            id = ObjectId(linkInfoId),
            enabled = false,
            lastUpdateDate = Date()
        )
        val disableLinkInfoUni= linkInfoRepo.updateOne(null, updatedLinkInfo)

        // delete cache
        val deleteCacheUni = redisRepo.delKey(shortLink)

        return checkExpireUni.chain { isExpired ->
            if (isExpired) {
                Uni.combine().all().unis(disableLinkInfoUni, deleteCacheUni).with { _, _ -> }
            } else {
                Uni.createFrom().nullItem()
            }
        }
    }


}

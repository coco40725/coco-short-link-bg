package com.coco.application.service

import com.coco.application.exception.ApplicationException
import com.coco.domain.model.CompensationActions
import com.coco.domain.model.ErrorLog
import com.coco.domain.model.LinkInfo
import com.coco.domain.service.linkInfo.LinkInfoSvc
import com.coco.infra.constant.RedisConstant
import com.coco.infra.repo.ErrorLogRepo
import com.coco.infra.repo.LinkInfoExpireTTLRepo
import com.coco.infra.repo.LinkInfoRepo
import com.coco.infra.repo.RedisRepo
import com.coco.infra.util.Log
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.Document
import org.bson.types.ObjectId
import org.reactivestreams.FlowAdapters
import java.util.*

/**
@author Yu-Jing
@create 2024-08-11-下午 08:10
 */

@ApplicationScoped
class LinkManagementService @Inject constructor(
    private val linkInfoRepo: LinkInfoRepo,
    private val redisRepo: RedisRepo,
    private val linkInfoExpireTTLRepo: LinkInfoExpireTTLRepo,
    private val linkInfoSvc: LinkInfoSvc,
    private val mongoClient: ReactiveMongoClient,
    private val compensationService: CompensationService
) {
    private val whiteShortLinkKey = RedisConstant.WHITE_SHORT_LINK_KEY
    private val originalLinkField = RedisConstant.ORIGINAL_LINK_FIELD

    fun getOriginalLink(shortLink: String): Uni<String?> {
        // check redis first
        return redisRepo.getHash(shortLink, originalLinkField).chain { it ->
            Log.i(LinkManagementService::class, "get from redis: $it")
            if (it != null) {
                Uni.createFrom().item(it)
            } else {
                getFromDBAndSetInCache(shortLink)
            }
        }
    }


    private fun getFromDBAndSetInCache(shortLink: String): Uni<String?> {
        return linkInfoRepo.getOneByEnableShortLink(null, shortLink).chain { info ->
            Log.i(LinkManagementService::class, "get from db: $info")
            if (info?.originalLink != null) {
                redisRepo.setHash(shortLink, originalLinkField, info.originalLink !!)
                    .map { result -> if (result) info.originalLink else null }
            } else {
                Uni.createFrom().nullItem()
            }
        }

    }


    /**
     *  1. mongodb transaction: insert link info + insert ttl (if needed)
     *  2. redis
     */
    fun addLinkInfoLog(info: LinkInfo): Uni<LinkInfo?> {
        info.id = ObjectId()
        val addInMongodbUni = mongoClient.startSession().chain { session ->
            session.startTransaction()
            try {
                linkInfoRepo.insertOne(session, info).chain { insertedInfo ->
                    when (insertedInfo.expirationDate) {
                        null -> Uni.createFrom().item(insertedInfo)
                        else -> linkInfoExpireTTLRepo.createOrUpdate(null, insertedInfo.id!!, insertedInfo.shortLink!!, insertedInfo.expirationDate!!).map { insertedInfo  }
                    }
                }.chain { _ ->
                    val publisher = FlowAdapters.toFlowPublisher(session.commitTransaction())
                    Uni.createFrom().publisher(publisher).map { info }
                }
            } catch (e: Exception) {
                val publisher = FlowAdapters.toFlowPublisher(session.abortTransaction())
                Uni.createFrom().publisher(publisher).map { null }
            }
        }

        val redisUni = redisRepo.setHash(info.shortLink!!,  originalLinkField, info.originalLink!!)

        return addInMongodbUni.chain { it ->
            if (it == null) {
                Uni.createFrom().failure(ApplicationException(
                    className = this::class.simpleName,
                    funName = this::addLinkInfoLog.name,
                    message = "add link info failed")
                )
            } else {
                redisUni.map { info }
            }
        }
    }



    fun disableLinkInfo(id: String): Uni<Boolean> {
        val disabledInfo = LinkInfo(
            id = ObjectId(id),
            shortLink = null,
            userId = null,
            originalLink = null,
            expirationDate = null,
            lastUpdateDate = Date(),
            createDate = null,
            enabled = false
        )

        val compensationActions = mutableListOf<CompensationActions>()


        // get original info
        val getOriginalUni =  linkInfoRepo.getOneById(null, id).memoize().indefinitely()

        // mongodb
        val mongodbUni = getOriginalUni.chain { originalInfo ->
            compensationActions.add(CompensationActions(
                functionName = "linkInfoRepo.updateOne",
                params = listOf(linkInfoRepo.toDocument(originalInfo)!!),
                action = { linkInfoRepo.updateOne(null, originalInfo !!) }
            ))
            linkInfoRepo.updateOne(null, disabledInfo)
        }

        // redis
        val redisUni = getOriginalUni.chain { originalInfo ->
            redisRepo.delKey(disabledInfo.shortLink!!).map { it ->
                compensationActions.add(CompensationActions(
                    functionName = "redisRepo.setHash",
                    params = listOf(Document(mapOf("shortLink" to disabledInfo.shortLink!!)), Document(mapOf("originalLinkField" to originalLinkField))),
                    action = { redisRepo.setHash(originalInfo?.shortLink!!, originalLinkField, originalInfo?.originalLink !!) }
                ))
                it > 0
            }
        }

        return mongodbUni.chain { _ ->
            redisUni
        }.onFailure().recoverWithItem { _ ->
            compensationService.executeCompensation(compensationActions)
            false
        }
    }


    fun enabledLinkInfo(id: String): Uni<Boolean> {
        val enabledInfo = LinkInfo(
            id = ObjectId(id),
            shortLink = null,
            userId = null,
            originalLink = null,
            expirationDate = null,
            lastUpdateDate = Date(),
            createDate = null,
            enabled = true
        )

        // get original info
        val getOriginalUni =  linkInfoRepo.getOneById(null, id).memoize().indefinitely()

        // mongodb
        val mongodbUni = mongoClient.startSession().chain { session ->
            session.startTransaction()
            try {
                linkInfoRepo.updateOne(null, enabledInfo).chain { updatedInfo ->
                    when (updatedInfo.expirationDate) {
                        null -> Uni.createFrom().item(updatedInfo)
                        else -> linkInfoExpireTTLRepo.createOrUpdate(null, updatedInfo.id!!, updatedInfo.shortLink!!, updatedInfo.expirationDate !!).map { updatedInfo }
                    }
                }.chain { updatedInfo ->
                    val publisher = FlowAdapters.toFlowPublisher(session.commitTransaction())
                    Uni.createFrom().publisher(publisher).map { updatedInfo }
                }
            } catch (e: Exception) {
                val publisher = FlowAdapters.toFlowPublisher(session.abortTransaction())
                Uni.createFrom().publisher(publisher).map { null }
            }
        }

        // redis
        val redisUni =  getOriginalUni.chain { originalInfo ->
            redisRepo.setHash(originalInfo?.shortLink!!,  originalLinkField, originalInfo?.originalLink!!).map { it }
        }

        return mongodbUni.chain { result ->
            if (result == null) {
                Uni.createFrom().failure(ApplicationException(
                    className = this::class.simpleName,
                    funName = this::addLinkInfoLog.name,
                    message = "enable link info failed")
                )
            } else {
                redisUni
            }
        }

    }

    fun changeOriginLink(id: String, originLink: String): Uni<Boolean> {
        val updatedInfo = LinkInfo(
            id = ObjectId(id),
            shortLink = null,
            userId = null,
            originalLink = originLink,
            expirationDate = null,
            lastUpdateDate = Date(),
            createDate = null,
            enabled = null
        )
        val compensationActions = mutableListOf<CompensationActions>()

        // get original info
        val getOriginalUni =  linkInfoRepo.getOneById(null, id).memoize().indefinitely()

        // mongodb
        val mongodbUni = getOriginalUni.chain { originalInfo ->
            compensationActions.add(CompensationActions(
                functionName = "linkInfoRepo.updateOne",
                params = listOf(linkInfoRepo.toDocument(originalInfo)!!),
                action = { linkInfoRepo.updateOne(null, originalInfo !!) }
            ))
            linkInfoRepo.updateOne(null, updatedInfo)
        }

        // redis
        val redisUni = getOriginalUni.chain { originalInfo ->
            redisRepo.updateHash(originalInfo?.shortLink!!,  originalLinkField, updatedInfo.originalLink!!).map {
                compensationActions.add(CompensationActions(
                    functionName = "redisRepo.updateHash",
                    params = listOf(Document(mapOf("shortLink" to originalInfo.shortLink!!)), Document(mapOf("originalLinkField" to originalLinkField))),
                    action = { redisRepo.updateHash(originalInfo.shortLink!!, originalLinkField, originalInfo.originalLink !!) }
                ))
            }
        }

        return mongodbUni.chain { _ ->
            redisUni
        }.onFailure().recoverWithItem { _ ->
            compensationService.executeCompensation(compensationActions)
            false
        }
    }

    fun changeExpireDate(id: String, expireDate: Date?): Uni<Boolean> {
        return if (expireDate == null) {
            linkInfoRepo.removeExpireDate(null, ObjectId(id))
        } else {
            val updatedInfo = LinkInfo(
                id = ObjectId(id),
                shortLink = null,
                userId = null,
                originalLink = null,
                expirationDate = expireDate,
                lastUpdateDate = Date(),
                createDate = null,
                enabled = null
            )

            val mongodbUni = mongoClient.startSession().chain { session ->
                session.startTransaction()
                try {
                    linkInfoRepo.updateOne(session, updatedInfo).chain { updatedInfo ->
                        linkInfoExpireTTLRepo.createOrUpdate(null, updatedInfo.id!!, updatedInfo.shortLink!!, updatedInfo.expirationDate!!)
                    }.chain { _ ->
                        val publisher = FlowAdapters.toFlowPublisher(session.commitTransaction())
                        Uni.createFrom().publisher(publisher).map { true }
                    }

                } catch (e: Exception) {
                    val publisher = FlowAdapters.toFlowPublisher(session.abortTransaction())
                    Uni.createFrom().publisher(publisher).map { false }
                }

            }

            mongodbUni
        }

    }

    fun updateLinkInfo(info: LinkInfo): Uni<LinkInfo?> {
        return mongoClient.startSession().chain { session ->
            session.startTransaction()
            try {
                linkInfoRepo.updateOne(null, info).chain { updatedInfo ->
                    when (info.expirationDate) {
                        null -> Uni.createFrom().item(updatedInfo)
                        else -> linkInfoExpireTTLRepo.createOrUpdate(null, updatedInfo.id!!, updatedInfo.shortLink!!, updatedInfo.expirationDate!!).map { updatedInfo  }
                    }
                }.chain { updatedInfo ->
                    val publisher = FlowAdapters.toFlowPublisher(session.commitTransaction())
                    Uni.createFrom().publisher(publisher).map { updatedInfo }
                }
            } catch (e: Exception){
                val publisher = FlowAdapters.toFlowPublisher(session.abortTransaction())
                Uni.createFrom().publisher(publisher).map { null }
            }
        }
    }

    fun checkShortLinkIsExist(shortLink: String): Uni<Boolean> {
        return linkInfoRepo.getOneByShortLink(null, shortLink).map { it != null }

    }
    fun checkShortLinkIsExpired(id: String): Uni<Boolean> {
        return linkInfoRepo.getOneById(null, id).chain { it ->
            if (it == null) {
                Uni.createFrom().failure(ApplicationException(
                    className = this::class.simpleName,
                    funName = this::checkShortLinkIsExpired.name,
                    message = "short link is not exist")
                )
            } else if (it.expirationDate == null) {
                Uni.createFrom().item(true)
            } else {
                Uni.createFrom().item(it.expirationDate!!.after(Date()))
            }
        }
    }


    fun getShortLinkFromWhiteList(): Uni<String> {
        return redisRepo.popFirstElementFromList(whiteShortLinkKey)
    }

    fun getWhiteShortLinks(): Uni<List<String>> {
        return redisRepo.getList(whiteShortLinkKey)
    }

    fun generateWhiteShortLinks(size: Int): Uni<Long> {
         val whiteList = linkInfoSvc.generateShortUrl(size * 2)
         return linkInfoRepo.checkShortLinksExist(null, whiteList).chain { existShortLinks ->
             val availableShortLinks = whiteList.filter { !existShortLinks.contains(it) }.toMutableList()
             if (availableShortLinks.size < size) {
                while (availableShortLinks.size < size) {
                    val additionWhiteList = linkInfoSvc.generateShortUrl(size * 2)
                    val additionAvailableList = additionWhiteList.filter { !existShortLinks.contains(it) }
                    availableShortLinks.addAll(additionAvailableList)
                }
             }
             val selectedShortLinks = availableShortLinks.subList(0, size)
             redisRepo.addElementToList(whiteShortLinkKey,  selectedShortLinks)
         }
    }
}
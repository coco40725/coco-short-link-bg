package com.coco.application.service

import com.coco.application.exception.ApplicationException
import com.coco.domain.model.LinkInfo
import com.coco.domain.service.linkInfo.LinkInfoSvc
import com.coco.infra.constant.RedisConstant
import com.coco.infra.exception.RepoException
import com.coco.infra.repo.LinkInfoExpireTTLRepo
import com.coco.infra.repo.LinkInfoRepo
import com.coco.infra.repo.RedisRepo
import com.coco.infra.util.Log
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.types.ObjectId
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
    private val linkInfoSvc: LinkInfoSvc
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
        return linkInfoRepo.getOneByEnableShortLink(shortLink).chain { info ->
            Log.i(LinkManagementService::class, "get from db: $info")
            if (info?.originalLink != null) {
                redisRepo.setHash(shortLink, originalLinkField, info.originalLink !!)
                    .map { result -> if (result) info.originalLink else null }
            } else {
                Uni.createFrom().nullItem()
            }
        }

    }



    fun addLinkInfoLog(info: LinkInfo): Uni<LinkInfo?> {
        val redisUni = redisRepo.setHash(info.shortLink!!,  originalLinkField, info.originalLink!!)
        val linkInfoUni = linkInfoRepo.insertOne(info).chain { insertedInfo ->
            if (insertedInfo != null) {
                when (insertedInfo.expirationDate) {
                    null -> Uni.createFrom().item(insertedInfo)
                    else -> linkInfoExpireTTLRepo.createOrUpdate(insertedInfo.id!!, insertedInfo.shortLink!!, insertedInfo.expirationDate!!).map { insertedInfo  }
                }
            } else {
                Uni.createFrom().nullItem()
            }
        }

        return Uni.combine().all().unis(redisUni, linkInfoUni)
            .with { _, dbResult -> dbResult }
            .onFailure(RepoException::class.java).call { _ ->
                redisRepo.delHash(info.shortLink!!, originalLinkField)
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

        return linkInfoRepo.updateOne(disabledInfo).chain { updatedInfo ->
            val shortLink = updatedInfo.shortLink !!
            redisRepo.delHash(shortLink, originalLinkField).map { it > 0 }
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

        return linkInfoRepo.updateOne(enabledInfo).chain { updatedInfo ->
            if (updatedInfo != null) {
                val redisEnabledUni = redisRepo.setHash(updatedInfo.shortLink!!,  originalLinkField, updatedInfo.originalLink!!).map { it }
                val ttlUni = when (updatedInfo.expirationDate) {
                    null -> Uni.createFrom().item(true)
                    else -> linkInfoExpireTTLRepo.createOrUpdate(updatedInfo.id!!, updatedInfo.shortLink!!, updatedInfo.expirationDate !!).map { it != null }
                }

                Uni.combine().all().unis(redisEnabledUni, ttlUni).with { redisResult, ttlResult ->
                    redisResult && ttlResult
                }
            } else {
                Uni.createFrom().item(false)
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
        return linkInfoRepo.updateOne(updatedInfo).chain { updatedInfo ->
            redisRepo.updateHash(updatedInfo.shortLink!!,  originalLinkField, updatedInfo.originalLink!!).map { it }
        }
    }

    fun changeExpireDate(id: String, expireDate: Date?): Uni<Boolean> {
        return if (expireDate == null) {
            linkInfoRepo.removeExpireDate(ObjectId(id))
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

            linkInfoRepo.updateOne(updatedInfo).chain { updatedInfo ->
                if (updatedInfo != null) {
                    linkInfoExpireTTLRepo.createOrUpdate(updatedInfo.id!!, updatedInfo.shortLink!!, updatedInfo.expirationDate!!).map { it != null }
                } else {
                    Uni.createFrom().item(false)
                }
            }
        }

    }

    fun updateLinkInfo(info: LinkInfo): Uni<LinkInfo?> {
        return linkInfoRepo.updateOne(info).chain { updatedInfo ->
            if (updatedInfo != null) {
                when (info.expirationDate) {
                    null -> Uni.createFrom().item(updatedInfo)
                    else -> linkInfoExpireTTLRepo.createOrUpdate(updatedInfo.id!!, updatedInfo.shortLink!!, updatedInfo.expirationDate!!).map { updatedInfo  }
                }
            } else {
                Uni.createFrom().nullItem()
            }
        }
    }

    fun checkShortLinkIsExist(shortLink: String): Uni<Boolean> {
        return linkInfoRepo.getOneByShortLink(shortLink).map { it != null }

    }
    fun checkShortLinkIsExpired(id: String): Uni<Boolean> {
        return linkInfoRepo.getOneById(id).chain { it ->
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
         return linkInfoRepo.checkShortLinksExist(whiteList).chain { existShortLinks ->
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
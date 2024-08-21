package com.coco.infra.repo

import com.coco.infra.repo.exception.RedisCacheDeleteFailedException
import com.coco.infra.repo.exception.RedisCachePopFailedException
import com.coco.infra.repo.exception.RedisCacheSetFailedException
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.quarkus.redis.datasource.hash.ReactiveHashCommands
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands
import io.quarkus.redis.datasource.list.ReactiveListCommands
import io.quarkus.redis.datasource.value.ReactiveValueCommands
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import kotlin.jvm.Throws


/**
@author Yu-Jing
@create 2024-08-10-下午 07:47
 */

@ApplicationScoped
class RedisRepo @Inject constructor(
   val redisDS: ReactiveRedisDataSource
){
    private val keys: ReactiveKeyCommands<String>? = null
    private lateinit var strCmd: ReactiveValueCommands<String, String>
    private lateinit var hashCmd: ReactiveHashCommands<String, String, String>
    private lateinit var listCmd: ReactiveListCommands<String, String>


    @PostConstruct
    fun init(){
        strCmd = redisDS.value(String::class.java)
        hashCmd = redisDS.hash(String::class.java)
        listCmd = redisDS.list(String::class.java)
    }


    fun getHash(key: String, field: String): Uni<String> {
        return hashCmd.hget(key, field)
    }

    fun setHash(key: String, field: String, value: String): Uni<Boolean> {
        return hashCmd.hset(key, field, value).map {
            if (!it) throw RedisCacheSetFailedException("Set cache failed. key: $key, field: $field, value: $value")
            true
        }
    }

    fun updateHash(key: String, field: String, value: String): Uni<Boolean> {
        return hashCmd.hdel(key, field).chain { _ ->
            hashCmd.hset(key, field, value).map {
                if (!it) throw RedisCacheSetFailedException("Set cache failed. key: $key, field: $field, value: $value")
                true
            }
        }
    }
    fun delHash(key: String, field: String): Uni<Int> {
        return hashCmd.hdel(key, field).map {
            if (it == 0) throw RedisCacheDeleteFailedException("Delete cache failed. key: $key, field: $field")
            it
        }
    }

    fun getList(key: String): Uni<List<String>> {
        return listCmd.lrange(key, 0, -1)
    }

    @Throws(RedisCachePopFailedException::class)
    fun popFirstElementFromList(key: String): Uni<String> {
        return listCmd.lpop(key).map {
            if (it == null) throw RedisCachePopFailedException("Pop cache failed. key: $key")
            it
        }
    }
    fun addElementToList(key: String, elements: List<String>): Uni<Long>? {
        return listCmd.lpush(key, *elements.toTypedArray())
    }
}
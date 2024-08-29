package com.coco.infra.repo

import com.coco.infra.exception.RepoException
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.quarkus.redis.datasource.hash.ReactiveHashCommands
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands
import io.quarkus.redis.datasource.list.ReactiveListCommands
import io.quarkus.redis.datasource.value.ReactiveValueCommands
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject


/**
@author Yu-Jing
@create 2024-08-10-下午 07:47
 */

@ApplicationScoped
class RedisRepo @Inject constructor(
   val redisDS: ReactiveRedisDataSource
){
    private lateinit var keyCmd: ReactiveKeyCommands<String>
    private lateinit var strCmd: ReactiveValueCommands<String, String>
    private lateinit var hashCmd: ReactiveHashCommands<String, String, String>
    private lateinit var listCmd: ReactiveListCommands<String, String>

    private val className = this::class.simpleName


    @PostConstruct
    fun init(){
        strCmd = redisDS.value(String::class.java)
        hashCmd = redisDS.hash(String::class.java)
        listCmd = redisDS.list(String::class.java)
        keyCmd = redisDS.key()
    }


    fun getHash(key: String, field: String): Uni<String> {
        return hashCmd.hget(key, field)
    }

    fun setHash(key: String, field: String, value: String): Uni<Boolean> {
        return hashCmd.hset(key, field, value).chain { it ->

            if (!it) {
                Uni.createFrom().failure(RepoException(
                    className,
                    this::setHash.name,
                    "Set cache failed. key: $key, field: $field, value: $value"
                ))
            } else {
                Uni.createFrom().item(true)
            }
        }
    }

    fun updateHash(key: String, field: String, value: String): Uni<Boolean> {
        return hashCmd.hdel(key, field).chain { _ ->
            hashCmd.hset(key, field, value).chain { it ->
                if (!it) {
                    Uni.createFrom().failure(RepoException(
                        className,
                        this::updateHash.name,
                        "Update cache failed. key: $key, field: $field, value: $value"
                    ))
                } else {
                    Uni.createFrom().item(true)
                }
            }
        }
    }
    fun delKey(key: String): Uni<Int> {
        return keyCmd.del(key).chain { it ->
            if (it == 0) {
                Uni.createFrom().failure(RepoException(
                    className,
                    this::delKey.name,
                    "Delete cache failed. key: $key"
                ))
            } else {
                Uni.createFrom().item(it)
            }
        }
    }

    fun getList(key: String): Uni<List<String>> {
        return listCmd.lrange(key, 0, -1)
    }

    fun popFirstElementFromList(key: String): Uni<String> {
        return listCmd.lpop(key).chain { it  ->

            if (it == null) {
                Uni.createFrom().failure(RepoException(
                    className,
                    this::popFirstElementFromList.name,
                    "Pop cache failed. key: $key"
                ))
            } else {
                Uni.createFrom().item(it)
            }

        }
    }
    fun addElementToList(key: String, elements: List<String>): Uni<Long>? {
        return listCmd.lpush(key, *elements.toTypedArray())
    }
}
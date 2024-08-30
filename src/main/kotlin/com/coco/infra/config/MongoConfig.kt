package com.coco.infra.config

import io.smallrye.config.ConfigMapping

/**
@author Yu-Jing
@create 2024-08-30-下午 02:10
 */
@ConfigMapping(prefix = "mongo")
interface MongoConfig {
    fun database(): String
}
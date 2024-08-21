package com.coco.infra.config

import io.smallrye.config.ConfigMapping

/**
@author Yu-Jing
@create 2024-08-10-下午 10:22
 */
@ConfigMapping(prefix = "pubsub")
interface PubSubConfig {
    fun linkInfoLogTopic(): String
    fun subLinkInfoLog(): String
}
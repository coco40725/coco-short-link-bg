package com.coco.infra.config

import io.smallrye.config.ConfigMapping

/**
@author Yu-Jing
@create 2024-08-11-下午 08:31
 */

@ConfigMapping(prefix = "web")
interface WebConfig {
    fun websiteDomain(): String
}
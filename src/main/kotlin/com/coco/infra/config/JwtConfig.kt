package com.coco.infra.config

import io.smallrye.config.ConfigMapping

/**
@author Yu-Jing
@create 2024-08-14-下午 02:55
 */

@ConfigMapping(prefix = "jwt")
interface JwtConfig {
    fun secretKey(): String
}
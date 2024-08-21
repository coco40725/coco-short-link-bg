package com.coco.infra.config

import io.smallrye.config.ConfigMapping

/**
@author Yu-Jing
@create 2024-08-16-下午 04:53
 */

@ConfigMapping(prefix = "bigquery")
interface BigQueryConfig {
    fun bigQueryDb(): String
    fun bigQueryLogTable(): String
}
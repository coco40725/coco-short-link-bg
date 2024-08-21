package com.coco.infra.repo.exception

/**
@author Yu-Jing
@create 2024-08-11-下午 05:12
 */
class RedisCacheSetFailedException(message: String): Exception(message)
class RedisCacheDeleteFailedException(message: String): Exception(message)
class RedisCachePopFailedException(message: String): Exception(message)
package com.coco.application.middleware.auth

import jakarta.enterprise.context.RequestScoped

/**
@author Yu-Jing
@create 2024-08-15-下午 12:45
 */

@RequestScoped
class JwtRequest{
    var token: String? = null

    fun init(info: Map<String, Any?>) {
        token = info["token"] as String?
    }
}
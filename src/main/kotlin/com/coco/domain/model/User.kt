package com.coco.domain.model


/**
@author Yu-Jing
@create 2024-08-10-下午 04:48
 */
data class User(
    var id: String,
    var name: String,
    var email: String,
    var password: String? = null,
    var emailVerify: Boolean? = null,
)

package com.coco.application.cqrs.query.base

/**
@author Yu-Jing
@create 2024-06-21-2:02 PM
 */
open class QueryValidateResult (
    open var isValid: Boolean = false,
    open var message: List<String>? = emptyList()
)
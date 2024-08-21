package com.coco.application.cqrs.command.base

/**
@author Yu-Jing
@create 2024-06-21-2:02 PM
 */
open class CommandValidateResult (
    open var isValid: Boolean = false,
    open var message: List<String>? = emptyList()
)
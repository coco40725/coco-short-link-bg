package com.coco.application.cqrs.command.base

/**
@author Yu-Jing
@create 2023-10-23-9:54 AM
 */

interface CommandHandler<R, C : Command<R>> {
    fun handle(command: C, validateResult: CommandValidateResult? = null): R
}

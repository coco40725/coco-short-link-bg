package com.coco.application.cqrs.command.base

import io.smallrye.mutiny.Uni

/**
@author Yu-Jing
@create 2023-11-07-4:55 PM
 */
interface CommandValidator<C : Command<*>> {
    fun validateCommand(command:  C): Uni<CommandValidateResult>
}
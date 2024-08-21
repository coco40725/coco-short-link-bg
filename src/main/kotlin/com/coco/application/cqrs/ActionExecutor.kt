package com.coco.application.cqrs

import com.coco.application.cqrs.command.base.Command
import com.coco.application.cqrs.command.base.CommandValidateResult
import com.coco.application.cqrs.query.base.Query
import com.coco.application.cqrs.query.base.QueryValidateResult
import io.smallrye.mutiny.Uni



/**
@author Yu-Jing
@create 2023-10-24-9:04 AM
 */
interface ActionExecutor {
    fun <R> executeCommand(command: Command<R>, validateResult: CommandValidateResult? = null): R
    fun <R> validateCommand(command: Command<R>): Uni<CommandValidateResult>?
    fun <R> executeQuery(query: Query<R>, validateResult: QueryValidateResult? = null): R
    fun <R> validateQuery(query: Query<R>): Uni<QueryValidateResult>?
}
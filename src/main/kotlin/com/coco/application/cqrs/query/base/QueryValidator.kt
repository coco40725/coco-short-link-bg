package com.coco.application.cqrs.query.base

import io.smallrye.mutiny.Uni

/**
@author Yu-Jing
@create 2024-05-30-1:37 PM
 */
interface QueryValidator<Q : Query<*>> {
    fun validateQuery(query: Q): Uni<QueryValidateResult>
}
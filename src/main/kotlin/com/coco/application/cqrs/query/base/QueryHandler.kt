package com.coco.application.cqrs.query.base


/**
@author Yu-Jing
@create 2023-10-23-9:55 AM
 */
interface QueryHandler<R, Q: Query<R>> {
    fun handle(query: Q, validateResult: QueryValidateResult? = null): R
}
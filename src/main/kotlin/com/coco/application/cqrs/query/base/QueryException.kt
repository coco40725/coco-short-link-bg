package com.coco.application.cqrs.query.base

/**
@author Yu-Jing
@create 2023-10-25-11:37 AM
 */
class QueryHandlerCastException(message: String): Exception(message) {}
class NoSuchQueryException(message: String): Exception(message) {}
class QueryValidationException(message: String): Exception(message) {}
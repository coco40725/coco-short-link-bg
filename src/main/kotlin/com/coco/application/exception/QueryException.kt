package com.coco.application.exception

import io.micrometer.core.instrument.Metrics

/**
@author Yu-Jing
@create 2023-10-25-11:37 AM
 */

abstract class QueryException(message: String): Exception(message) {

    companion object {
        fun incrementExceptionCounter(exceptionType: String, className: String?, message: String) {
            Metrics.counter("ExceptionCounter",
                "ExceptionBaseType", "QueryException",
                "exceptionType", exceptionType,
                "className", className ?: "Unknown",
                "message", message
            ).increment()
        }
    }
}

class QueryHandlerCastException(className: String?, message: String): QueryException(message) {
    init {
        incrementExceptionCounter("QueryHandlerCastException", className, message)
    }
}
class NoSuchQueryException(className: String?, message: String): QueryException(message) {
    init {
        incrementExceptionCounter("NoSuchQueryException", className, message)
    }
}
class QueryValidationException(className: String?, message: String): QueryException(message) {
    init {
        incrementExceptionCounter("QueryValidationException", className, message)
    }
}
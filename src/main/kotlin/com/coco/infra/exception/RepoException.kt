package com.coco.infra.exception

import io.micrometer.core.instrument.Metrics

/**
@author Yu-Jing
@create 2024-08-24-下午 05:56
 */


abstract class RepoExceptionBase(message: String): Exception(message){
    companion object {
        fun incrementExceptionCounter(exceptionType: String, className: String?, funName: String?, message: String) {
            Metrics.counter("ExceptionCounter",
                "ExceptionBaseType", "RepoException",
                "exceptionType", exceptionType,
                "className", className ?: "Unknown",
                "funName", funName ?: "Unknown",
                "message", message
            ).increment()
        }
    }
}

class RepoException(className: String?, funName: String?, message: String): RepoExceptionBase(message) {
    init {
        incrementExceptionCounter("RepoException", className, funName, message)
    }
}

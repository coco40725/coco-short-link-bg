package com.coco.application.exception

import io.micrometer.core.instrument.Metrics

/**
@author Yu-Jing
@create 2024-08-24-下午 01:14
*/

abstract class ApplicationExceptionBase(message: String): Exception(message) {
    companion object {
        fun incrementExceptionCounter(exceptionType: String, className: String?, funName: String?, message: String) {
            Metrics.counter("ExceptionCounter",
                "ExceptionBaseType", "ApplicationException",
                "exceptionType", exceptionType,
                "className", className ?: "Unknown",
                "funName", funName ?: "Unknown",
                "message", message
            ).increment()
        }
    }

}

class ApplicationException(className: String?, funName: String?, message: String): ApplicationExceptionBase(message) {
    init {
        incrementExceptionCounter("ApplicationException", className, funName, message)
    }
}
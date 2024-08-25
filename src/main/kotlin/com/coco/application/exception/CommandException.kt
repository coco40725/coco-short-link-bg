package com.coco.application.exception


import io.micrometer.core.instrument.Metrics

/**
@author Yu-Jing
@create 2023-10-25-11:37 AM
 */

abstract class CommandException (message: String): Exception(message) {
    companion object {
        fun incrementExceptionCounter(exceptionType: String, className: String?, message: String) {
            Metrics.counter("ExceptionCounter",
                "ExceptionBaseType", "CommandException",
                "exceptionType", exceptionType,
                "className", className ?: "Unknown",
                "message", message
            ).increment()
        }
    }
}

class CommandHandlerCastException(className: String?, message: String): CommandException(message) {
    init {
        incrementExceptionCounter("CommandHandlerCastException", className, message)
    }
}
class NoSuchCommandException(className: String?, message: String): CommandException(message) {
    init {
        incrementExceptionCounter("NoSuchCommandException", className, message)
    }
}
class CommandValidationException(className: String?, message: String) : CommandException(message) {
    init {
        incrementExceptionCounter("CommandValidationException", className, message)
    }
}
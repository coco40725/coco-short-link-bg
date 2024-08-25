package com.coco.infra.exception

import io.micrometer.core.instrument.Metrics

/**
@author Yu-Jing
@create 2024-08-24-下午 05:41
 */
abstract class GrpcExceptionBase(message: String): Exception(message){
    companion object {
        fun incrementExceptionCounter(exceptionType: String, className: String?, funName: String?, message: String) {
            Metrics.counter("ExceptionCounter",
                "ExceptionBaseType", "GrpcException",
                "exceptionType", exceptionType,
                "className", className ?: "Unknown",
                "funName", funName ?: "Unknown",
                "message", message
            ).increment()
        }
    }
}

class GrpcConnectionException(className: String?, funName: String?, message: String): GrpcExceptionBase(message) {
    init {
        incrementExceptionCounter("GrpcConnectionException", className, funName, message)
    }
}
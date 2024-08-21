package com.coco.infra.util

import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

object Log {

    enum class LogLevel {
        DEBUG, INFO, WARN, ERROR, NONE
    }
    private val logLevel = LogLevel.INFO
    private fun log(level: LogLevel, callingClass: KClass<*>, description: String = "", message: (() -> Any)? = null) {
        try {
            if (level >= logLevel) {
                var logMessage = message?.let {
                    try {
                        when (val msg = it()) {
                            is String -> msg
                            else -> {
                                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ssZ")
                                    .setPrettyPrinting()
                                    .create()
                                gson.toJson(msg)
                            }
                        }
                    } catch (e: IllegalArgumentException) {
                        println("反序列化失敗: ${e.message}")
                        "${it()}"
                    }
                } ?: ""
                if (description.isNotEmpty()) {
                    logMessage = if (logMessage.isNotEmpty()) "$description: $logMessage" else description
                }
                println("${getCurrentDateTime()} ${callingClass.simpleName}: $logMessage")
            }
        }catch (e: Exception){
            println("Log error: ${e.message}")
        }
    }

    fun i(callingClass: KClass<*>, description:String = "", message: (() -> Any)? = null) = log(LogLevel.INFO, callingClass, description, message)
    fun d(callingClass: KClass<*>, description:String = "", message: (() -> Any)? = null) = log(LogLevel.DEBUG, callingClass, description, message)
    fun w(callingClass: KClass<*>, description:String = "", message: (() -> Any)? = null) = log(LogLevel.WARN, callingClass, description, message)
    fun e(callingClass: KClass<*>, description:String = "", message: (() -> Any)? = null) = log(LogLevel.ERROR, callingClass, description, message)


    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(Date())
    }

}

package com.coco.application.middleware.auth

import jakarta.ws.rs.NameBinding

/**
@author Yu-Jing
@create 2024-08-15-下午 12:44
 */

@NameBinding
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class Logged {
}
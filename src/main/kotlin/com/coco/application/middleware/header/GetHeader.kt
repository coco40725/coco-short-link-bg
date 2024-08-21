package com.coco.application.middleware.header

import jakarta.ws.rs.NameBinding

/**
@author Yu-Jing
@create 2024-08-11-上午 10:06
 */

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@NameBinding
annotation class GetHeader()

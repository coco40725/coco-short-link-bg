package com.coco.application.cqrs.command.base

/**
@author Yu-Jing
@create 2023-10-25-11:37 AM
 */
class CommandHandlerCastException(message: String): Exception(message) {}
class NoSuchCommandException(message: String): Exception(message) {}
class CommandValidationException(message: String): Exception(message) {}
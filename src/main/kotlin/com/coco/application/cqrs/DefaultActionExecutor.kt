package com.coco.application.cqrs

import com.coco.application.cqrs.command.base.*
import com.coco.application.cqrs.query.base.*
import io.smallrye.mutiny.Uni
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject
import java.lang.reflect.ParameterizedType

/**
@author Yu-Jing
@create 2023-10-24-9:02 AM
 */
@ApplicationScoped
class DefaultActionExecutor: ActionExecutor {

    @Inject
    lateinit var commandInstances: Instance<CommandHandler<*, *>>

    @Inject
    lateinit var queryInstances: Instance<QueryHandler<*, *>>

    @Inject
    lateinit var commandValidatorInstances: Instance<CommandValidator<*>>

    @Inject
    lateinit var queryValidatorInstances: Instance<QueryValidator<*>>

    private var commandHandlerMap: Map<Class<*>, CommandHandler<*, *>> = mutableMapOf()
    private var queryHandlerMap: Map<Class<*>, QueryHandler<*, *>> = mutableMapOf()
    private var commandValidatorMap: Map<Class<*>, CommandValidator<*>> = mutableMapOf()
    private var queryValidatorMap: Map<Class<*>, QueryValidator<*>> = mutableMapOf()


    @PostConstruct
    fun init(){
        commandHandlerMap = getInstanceMap(commandInstances)
        queryHandlerMap = getInstanceMap(queryInstances)
        commandValidatorMap = getInstanceMap(commandValidatorInstances)
        queryValidatorMap = getInstanceMap(queryValidatorInstances)
    }
    override fun <R> executeCommand(command: Command<R>, validateResult: CommandValidateResult?): R {
        val handlerValue = commandHandlerMap[command::class.java] ?: throw NoSuchCommandException("No such command in commandHandlerMap")
        try {
            val commandHandler = handlerValue as CommandHandler<R, Command<R>>
            return commandHandler.handle(command, validateResult)
        } catch (e: ClassCastException){
            throw CommandHandlerCastException("commandHandler cannot cast to CommandHandler<R, Command<R>>")
        }

    }

    override fun <R> validateCommand(command: Command<R>): Uni<CommandValidateResult> {
        val validatorValue = commandValidatorMap[command::class.java] ?: throw NoSuchCommandException("No such command in commandValidatorMap")
        try {
            val commandValidator = validatorValue as CommandValidator<Command<R>>
            return commandValidator.validateCommand(command)
        } catch (e: ClassCastException){
            throw CommandHandlerCastException("commandValidator cannot cast to CommandValidator<R, Command<R>>")
        }
    }


    override fun <R> executeQuery(query: Query<R>, validateResult: QueryValidateResult?): R {
        val handlerValue = queryHandlerMap[query::class.java] ?: throw NoSuchQueryException("No such query in queryHandlerMap")
        try {
            val queryHandler = handlerValue as QueryHandler<R, Query<R>>
            return queryHandler.handle(query, validateResult)
        } catch (e: ClassCastException){
            throw QueryHandlerCastException("queryHandler cannot cast to QueryHandler<Query<*>>")
        }
    }

    override fun <R> validateQuery(query: Query<R>): Uni<QueryValidateResult> {
        val validatorValue = queryValidatorMap[query::class.java] ?: throw NoSuchQueryException("No such command in queryValidatorMap")
        try {
            val queryValidator = validatorValue as QueryValidator<Query<R>>
            return queryValidator.validateQuery(query)
        } catch (e: ClassCastException){
            throw QueryHandlerCastException("queryValidator cannot cast to QueryValidator<Query<*>>")
        }
    }

    private fun <T> getInstanceMap(instance: Instance<T>):  Map<Class<*>, T>{
        val map = mutableMapOf< Class<*>, T>()
        instance.forEach { it ->
            // 取得其 generic type
            val clazz = it!!::class.java.superclass
            val genericType = clazz.genericInterfaces[0] as ParameterizedType


            val finalClass = if (genericType.actualTypeArguments.size > 1) {
                genericType.actualTypeArguments[1] as Class<*>
            } else {
                genericType.actualTypeArguments[0] as Class<*>
            }

            // 整理成 HashMap: key: command class, value: Handler
            map[finalClass] = it
        }
        return map
    }
}

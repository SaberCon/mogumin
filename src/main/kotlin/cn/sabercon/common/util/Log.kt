package cn.sabercon.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

val Any.log get(): Logger = LoggerFactory.getLogger(actualJavaClass(this::class))

fun actualJavaClass(clazz: KClass<*>): Class<*> {
    return if (clazz.isCompanion) clazz.java.enclosingClass else clazz.java
}

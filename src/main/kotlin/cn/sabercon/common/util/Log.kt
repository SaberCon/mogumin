package cn.sabercon.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

inline val <reified T : Any> T.log get(): Logger = logger<T>()

inline fun <reified T : Any> logger(): Logger = LoggerFactory.getLogger(actualJavaClass(T::class))

fun actualJavaClass(clazz: KClass<*>): Class<*> = if (clazz.isCompanion) clazz.java.enclosingClass else clazz.java

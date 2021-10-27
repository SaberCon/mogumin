package cn.sabercon.common.util

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

inline val <reified T : Any> T.log get() = logger<T>()

inline fun <reified T : Any> logger() = LoggerFactory.getLogger(actualJavaClass(T::class))!!

fun actualJavaClass(clazz: KClass<*>) = if (clazz.isCompanion) clazz.java.enclosingClass!! else clazz.java
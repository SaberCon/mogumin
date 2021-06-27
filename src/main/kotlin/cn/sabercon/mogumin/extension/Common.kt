package cn.sabercon.mogumin.extension

import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

// log
val Any.log get() = logger(this.javaClass)

fun <T : Any> logger(forClass: Class<T>) = LoggerFactory.getLogger(unwrapCompanionClass(forClass))!!

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> = ofClass.enclosingClass?.takeIf {
    it.kotlin.companionObject?.java == ofClass
} ?: ofClass
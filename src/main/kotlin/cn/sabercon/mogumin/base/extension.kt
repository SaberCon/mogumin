package cn.sabercon.mogumin.base

import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.reflect.full.companionObject

// log
val Any.log get() = logger(this.javaClass)

fun <T> logger(forClass: Class<T>) = LoggerFactory.getLogger(unwrapCompanionClass(forClass))!!

fun <T> unwrapCompanionClass(ofClass: Class<T>): Class<*> = ofClass.enclosingClass?.takeIf {
    it.kotlin.companionObject?.java == ofClass
} ?: ofClass


// http
suspend fun getExchange(): ServerWebExchange =
    Mono.deferContextual { Mono.just(it) }.awaitSingle()[ServerWebExchangeContextFilter.EXCHANGE_CONTEXT_ATTRIBUTE]

suspend fun getLoginUserIdOrNull() = getExchange().request.headers[USER_ID]?.getOrNull(0)?.toLongOrNull()

suspend fun getLoginUserId() = getLoginUserIdOrNull() ?: BaseCode.UNAUTHORIZED.throws()


// util
fun <T> wrapExceptionToNull(supplier: () -> T) = try {
    supplier()
} catch (e: Exception) {
    null
}

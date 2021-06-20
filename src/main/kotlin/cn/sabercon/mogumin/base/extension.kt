package cn.sabercon.mogumin.base

import cn.sabercon.mogumin.util.JwtUtils
import com.google.common.hash.Hashing
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import kotlin.reflect.full.companionObject

// log
val Any.log get() = logger(this.javaClass)

fun <T : Any> logger(forClass: Class<T>) = LoggerFactory.getLogger(unwrapCompanionClass(forClass))!!

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> = ofClass.enclosingClass?.takeIf {
    it.kotlin.companionObject?.java == ofClass
} ?: ofClass


// http
suspend fun getExchange(): ServerWebExchange =
    Mono.deferContextual { Mono.just(it) }.awaitSingle()[ServerWebExchangeContextFilter.EXCHANGE_CONTEXT_ATTRIBUTE]

suspend fun getHeader(name: String) = getExchange().request.headers[name]?.getOrNull(0)

suspend fun getQueryParam(name: String) = getExchange().request.queryParams[name]?.getOrNull(0)

suspend fun getLoginUserIdOrNull() = getHeader("token")?.let { JwtUtils.decodeToken(it) }?.subject

suspend fun getLoginUserId() = getLoginUserIdOrNull() ?: BaseCode.UNAUTHORIZED.throws()


// spring
@Component
class ContextHolder : ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    companion object {

        lateinit var context: ApplicationContext

        fun getProperty(key: String) = context.environment.getProperty(key)

        fun getProperty(key: String, defaultValue: String) = getProperty(key) ?: defaultValue

        inline fun <reified T : Any> getBean(name: String): T = context.getBean(name, T::class.java)

        inline fun <reified T : Any> getBean(): T = context.getBean()
    }
}


// util
fun <T> wrapExceptionToNull(supplier: () -> T) = try {
    supplier()
} catch (e: Exception) {
    null
}

fun assertTrue(value: Boolean, errorCode: ErrorCode = BaseCode.FAILURE, lazyMessage: () -> String = { errorCode.msg }) {
    if (!value) {
        errorCode.throws(lazyMessage())
    }
}

fun sha256(input: String) = Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString()
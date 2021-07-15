package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.base.BaseCode
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

// request
suspend fun getExchange(): ServerWebExchange =
    Mono.deferContextual { Mono.just(it) }.awaitSingle()[ServerWebExchangeContextFilter.EXCHANGE_CONTEXT_ATTRIBUTE]

suspend fun getHeader(name: String) = getExchange().request.headers[name]?.getOrNull(0)

suspend fun getQueryParam(name: String) = getExchange().request.queryParams[name]?.getOrNull(0)

suspend fun getCurrentUserIdOrNull(): String? {
    return getHeader("token")?.let { JwtUtils.decodeToken(it) }?.subject
        ?: if (!ContextHolder.isProd()) getHeader("token") else null
}

suspend fun getCurrentUserId() = getCurrentUserIdOrNull() ?: BaseCode.UNAUTHORIZED.throws()


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

        fun isProd() = context.environment.activeProfiles.contains("prod")
    }
}
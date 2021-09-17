package cn.sabercon.common.util

import cn.sabercon.common.BaseCode
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono


suspend fun getExchange(): ServerWebExchange =
    Mono.deferContextual { it.toMono() }.awaitSingle()[ServerWebExchangeContextFilter.EXCHANGE_CONTEXT_ATTRIBUTE]

suspend fun getHeader(name: String) = getExchange().request.headers[name]?.getOrNull(0)

suspend fun getQueryParam(name: String) = getExchange().request.queryParams[name]?.getOrNull(0)

suspend fun getCurrentUserIdOrNull() = getHeader("token")?.let { JwtUtils.decodeToken(it) }

suspend fun getCurrentUserId() = getCurrentUserIdOrNull() ?: BaseCode.UNAUTHORIZED.throws()


@Component
class ContextHolder : ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    companion object {

        lateinit var context: ApplicationContext

        fun getProperty(key: String, defaultValue: String = "") = context.environment.getProperty(key) ?: defaultValue

        inline fun <reified T : Any> getBean(name: String): T = context.getBean<T>(name)

        inline fun <reified T : Any> getBean(): T = context.getBean()

        fun isProd() = context.environment.activeProfiles.contains("prod")
    }
}
package cn.sabercon.common.web

import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*
import java.net.URI

suspend fun CoRouterFunctionDsl.empty(): ServerResponse {
    return noContent().buildAndAwait()
}

suspend fun CoRouterFunctionDsl.body(body: Any): ServerResponse {
    return ok().bodyValueAndAwait(body)
}

suspend inline fun <reified T : Any> CoRouterFunctionDsl.body(flow: Flow<T>): ServerResponse {
    return ok().bodyAndAwait(flow)
}

suspend fun CoRouterFunctionDsl.redirect(url: String): ServerResponse {
    return status(HttpStatus.FOUND).location(URI.create(url)).buildAndAwait()
}

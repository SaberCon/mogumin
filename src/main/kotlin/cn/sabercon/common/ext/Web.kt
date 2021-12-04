package cn.sabercon.common.ext

import cn.sabercon.common.BaseCode
import cn.sabercon.common.data.DEFAULT_SORT
import cn.sabercon.common.data.ID
import cn.sabercon.common.throw400
import cn.sabercon.common.util.ensure
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.reactive.function.server.*

fun coRouter(basePath: String, routes: (CoRouterFunctionDsl.() -> Unit)) = coRouter { path(basePath).nest(routes) }

fun CoRouterFunctionDsl.get(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = GET(pattern, f)
fun CoRouterFunctionDsl.post(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = POST(pattern, f)
fun CoRouterFunctionDsl.put(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = PUT(pattern, f)
fun CoRouterFunctionDsl.delete(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = DELETE(pattern, f)

suspend fun CoRouterFunctionDsl.success() = noContent().buildAndAwait()

suspend fun CoRouterFunctionDsl.success(body: Any?) = when (body) {
    null, Unit -> success()
    else -> ok().bodyValueAndAwait(body)
}

suspend inline fun <reified T : Any> CoRouterFunctionDsl.success(flow: Flow<T>) = ok().bodyAndAwait(flow)

suspend inline fun <reified T : Any> ServerRequest.body() = awaitBodyOrNull<T>() ?: throw400()

suspend inline fun <reified T : Any> ServerRequest.formParamOrNull(
    name: String,
    validator: (T) -> Boolean = { true }
): T? =
    awaitFormData().getFirst(name)
        ?.let { runCatching { it.parse<T>() }.getOrElse { throw400() } }
        ?.also { ensure(validator(it)) }

suspend inline fun <reified T : Any> ServerRequest.formParam(name: String, validator: (T) -> Boolean = { true }): T =
    formParamOrNull(name, validator) ?: throw400()

inline fun <reified T : Any> ServerRequest.requestParamOrNull(name: String, validator: (T) -> Boolean = { true }): T? =
    queryParamOrNull(name)
        ?.let { runCatching { it.parse<T>() }.getOrElse { throw400() } }
        ?.also { ensure(validator(it)) }

inline fun <reified T : Any> ServerRequest.requestParam(name: String, validator: (T) -> Boolean = { true }): T =
    requestParamOrNull(name, validator) ?: throw400()

inline fun <reified T : Any> ServerRequest.pathParam(name: String, validator: (T) -> Boolean = { true }): T =
    pathVariable(name)
        .let { runCatching { it.parse<T>() }.getOrElse { throw400() } }
        .also { ensure(validator(it)) }

fun ServerRequest.pageable(sort: Sort = DEFAULT_SORT) = PageRequest.of(
    (requestParamOrNull("pi") { it > 0 } ?: 1) - 1,
    requestParamOrNull("ps") { it > 0 } ?: 20,
    sort,
)

fun ServerRequest.idPathParam() = pathParam<Long>(ID) { it > 0 }

fun ServerRequest.objectIdPathParam() = pathParam<String>(ID) { it.isNotEmpty() }

fun ServerRequest.userIdOrZero() = (attributeOrNull("userId") ?: 0) as Long

fun ServerRequest.userId() = userIdOrZero().also { ensure(it > 0, BaseCode.UNAUTHORIZED) }
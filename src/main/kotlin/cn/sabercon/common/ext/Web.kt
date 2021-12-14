package cn.sabercon.common.ext

import cn.sabercon.common.BaseCode
import cn.sabercon.common.data.ID
import cn.sabercon.common.throwClientError
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.PageRequest
import org.springframework.web.reactive.function.server.*

fun coRouter(basePath: String, routes: (CoRouterFunctionDsl.() -> Unit)) = coRouter { basePath.nest(routes) }

fun CoRouterFunctionDsl.get(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = GET(pattern, f)
fun CoRouterFunctionDsl.post(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = POST(pattern, f)
fun CoRouterFunctionDsl.put(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = PUT(pattern, f)
fun CoRouterFunctionDsl.delete(pattern: String = "", f: suspend (ServerRequest) -> ServerResponse) = DELETE(pattern, f)

suspend fun CoRouterFunctionDsl.success(body: Any? = null) = when (body) {
    null, Unit -> noContent().buildAndAwait()
    else -> ok().bodyValueAndAwait(body)
}

suspend inline fun <reified T : Any> CoRouterFunctionDsl.success(flow: Flow<T>) = ok().bodyAndAwait(flow)

suspend inline fun <reified T : Any> ServerRequest.body() =
    awaitBodyOrNull<T>() ?: throwClientError("Empty request body")

suspend inline fun <reified T : Any> ServerRequest.formParamOrNull(name: String): T? =
    awaitFormData().getFirst(name)?.parseInput()

suspend inline fun <reified T : Any> ServerRequest.formParam(name: String): T =
    formParamOrNull(name) ?: throwClientError("Form param $name cannot be null")

inline fun <reified T : Any> ServerRequest.requestParamOrNull(name: String): T? = queryParamOrNull(name)?.parseInput()

inline fun <reified T : Any> ServerRequest.requestParam(name: String): T =
    requestParamOrNull(name) ?: throwClientError("Query param $name cannot be null")

inline fun <reified T : Any> ServerRequest.pathParam(name: String): T = pathVariable(name).parseInput()

fun ServerRequest.pageable() = PageRequest.of(
    (requestParamOrNull<Int>("pi")?.takeIf { it > 0 } ?: 1) - 1,
    requestParamOrNull<Int>("ps")?.takeIf { it > 0 } ?: 10,
)

inline fun <reified T : Any> String.parseInput(): T =
    runCatching { parse<T>() }.getOrElse { throwClientError("Invalid input") }

fun ServerRequest.idPathParam() = pathParam<Long>(ID)

fun ServerRequest.objectIdPathParam() = pathParam<String>(ID)

fun ServerRequest.userIdOrZero() = (attributeOrNull("userId") as Long?) ?: 0

fun ServerRequest.userId() = userIdOrZero().also { if (it <= 0) BaseCode.UNAUTHORIZED.throws() }
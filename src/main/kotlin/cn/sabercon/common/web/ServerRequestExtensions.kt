package cn.sabercon.common.web

import cn.sabercon.common.util.parse
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.reactive.function.server.*

suspend inline fun <reified T : Any> ServerRequest.body(): T {
    return awaitBodyOrNull() ?: BAD_REQUEST.throws()
}

suspend inline fun <reified T : Any> ServerRequest.formParamOrNull(name: String): T? {
    return awaitFormData().getFirst(name)?.parse()
}

suspend inline fun <reified T : Any> ServerRequest.formParam(name: String): T {
    return formParamOrNull(name) ?: BAD_REQUEST.throws()
}

inline fun <reified T : Any> ServerRequest.requestParamOrNull(name: String): T? {
    return queryParamOrNull(name)?.parse() ?: BAD_REQUEST.throws()
}

inline fun <reified T : Any> ServerRequest.requestParam(name: String): T {
    return requestParamOrNull(name) ?: BAD_REQUEST.throws()
}

inline fun <reified T : Any> ServerRequest.pathParam(name: String): T {
    return pathVariable(name).parse()
}

fun ServerRequest.userIdOrZero() = (attributeOrNull("userId") as Long?) ?: 0

fun ServerRequest.userId() = userIdOrZero().also { if (it <= 0) UNAUTHORIZED.throws() }

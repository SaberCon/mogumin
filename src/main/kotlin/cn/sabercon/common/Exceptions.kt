package cn.sabercon.common

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates.all
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.json
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.web.server.ResponseStatusException
import java.util.stream.Collectors

/**
 * Order(-2) is to give it a higher priority than the
 * [org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler]
 * which is registered at Order(-1).
 */
@Order(-2)
@Component
class GlobalExceptionHandler(
    errorAttributes: ErrorAttributes,
    resources: Resources,
    applicationContext: ApplicationContext,
    viewResolvers: ObjectProvider<ViewResolver>,
    serverCodecConfigurer: ServerCodecConfigurer,
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {

    init {
        setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()))
        setMessageWriters(serverCodecConfigurer.writers)
        setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes) = route(all()) {
        when (val error = errorAttributes.getError(it)) {
            is ServiceException -> ServerResponse
                .status(error.status)
                .json()
                .bodyValue(ErrorResponse<Nothing>(error.code, error.reason))
            is ResponseStatusException -> ServerResponse.status(error.status).build()
            else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}

interface ErrorCode {
    val code: String
    val msg: String
    val status: HttpStatus
    fun error(msg: String = this.msg) = ServiceException(code, status, msg)
    fun throws(msg: String = this.msg): Nothing = throw error(msg)
}

enum class BaseCode(override val code: String, override val msg: String, override val status: HttpStatus) : ErrorCode {
    BAD_REQUEST("400", "Bad Request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("401", "Unauthorized", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
}

class ServiceException(val code: String, status: HttpStatus, msg: String, cause: Throwable? = null) :
    ResponseStatusException(status, msg, cause)

fun throw400(): Nothing = BaseCode.BAD_REQUEST.throws()

fun throw500(): Nothing = BaseCode.INTERNAL_SERVER_ERROR.throws()
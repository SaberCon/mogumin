package cn.sabercon.common.web.config

import cn.sabercon.common.web.HttpException
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse.status
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
    webProperties: WebProperties,
    applicationContext: ApplicationContext,
    viewResolvers: ObjectProvider<ViewResolver>,
    serverCodecConfigurer: ServerCodecConfigurer,
) : AbstractErrorWebExceptionHandler(errorAttributes, webProperties.resources, applicationContext) {

    init {
        setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()))
        setMessageWriters(serverCodecConfigurer.writers)
        setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes) = RouterFunctions.route(RequestPredicates.all()) {
        when (val error = errorAttributes.getError(it)) {
            is HttpException -> status(error.status).bodyValue(mapOf("code" to error.code, "message" to error.message))
            is ResponseStatusException -> status(error.status).build()
            else -> status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}

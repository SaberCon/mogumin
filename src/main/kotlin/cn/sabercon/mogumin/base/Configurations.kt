package cn.sabercon.mogumin.base

import cn.sabercon.mogumin.extension.log
import cn.sabercon.mogumin.extension.toDate
import cn.sabercon.mogumin.extension.toDatetime
import cn.sabercon.mogumin.extension.toTime
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.Parameter
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.HandlerResultHandler
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.result.method.annotation.AbstractMessageWriterResultHandler
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.jvm.javaMethod

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException::class)
    fun handle(e: ServiceException): Result<Void> {
        log.warn("catch service exception: ${e.message}")
        return Result(ok = false, code = e.code, msg = e.localizedMessage)
    }

    @ExceptionHandler(Throwable::class)
    fun handle(e: Throwable): Result<Void> {
        log.error("catch unexpected exception", e)
        return Result(
            ok = false,
            code = BaseCode.FAILURE.code,
            msg = e.localizedMessage,
            debugMsg = e.stackTraceToString()
        )
    }
}

@EnableWebFlux
@Configuration
class WebConfig(private val mapper: ObjectMapper) : WebFluxConfigurer {

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().enableLoggingRequestDetails(true)
        configurer.customCodecs().registerWithDefaultConfig(Jackson2JsonDecoder(mapper))
        configurer.customCodecs().registerWithDefaultConfig(Jackson2JsonEncoder(mapper))
    }

    override fun addFormatters(registry: FormatterRegistry) {
        // cannot use lambda here because spring needs the parameterized types
        registry.addConverter(object : Converter<String, LocalDateTime> {
            override fun convert(source: String) = source.toDatetime()
        })
        registry.addConverter(object : Converter<String, LocalDate> {
            override fun convert(source: String) = source.toDate()
        })
        registry.addConverter(object : Converter<String, LocalTime> {
            override fun convert(source: String) = source.toTime()
        })
    }

    @Bean
    fun contextFilter() = ServerWebExchangeContextFilter()
}

/**
 * Constructor is copied from [org.springframework.web.reactive.config.WebFluxConfigurationSupport.responseBodyResultHandler].
 */
@Component
class WebServiceResultHandler(
    @Qualifier("webFluxAdapterRegistry") reactiveAdapterRegistry: ReactiveAdapterRegistry,
    serverCodecConfigurer: ServerCodecConfigurer,
    @Qualifier("webFluxContentTypeResolver") contentTypeResolver: RequestedContentTypeResolver,
) : AbstractMessageWriterResultHandler(serverCodecConfigurer.writers, contentTypeResolver, reactiveAdapterRegistry),
    HandlerResultHandler {
    /** Order should be higher than [org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler]. */
    override fun getOrder() = 99

    override fun supports(result: HandlerResult): Boolean {
        return result.returnTypeSource.containingClass.isAnnotationPresent(WebService::class.java)
    }

    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        var v = result.returnValue
        var r = result.returnTypeSource
        if (v is Mono<*>) {
            v = v.map { if (it is Result<*>) it else Result(data = it) }.defaultIfEmpty(emptyResult())
            r = MethodParameter(::EMPTY_RESULT.getter.javaMethod!!, -1)
        }
        return writeBody(v, r, exchange)
    }
}

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(builder: WebClient.Builder) = builder.build()
}

@Configuration
class SwaggerConfig {

    @Bean
    fun operationCustomizer(builder: WebClient.Builder) = OperationCustomizer { operation, handlerMethod ->
        operation.addParametersItem(
            Parameter().`in`(ParameterIn.HEADER.toString())
                .schema(StringSchema()).name("token")
        )
    }
}

@Component
class LoginFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange)
    }
}
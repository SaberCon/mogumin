package cn.sabercon.common

import cn.sabercon.common.util.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.Parameter
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.WebFilter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException::class)
    fun handle(e: ServiceException): ResponseEntity<ErrorResponse<Nothing>> {
        log.warn("catch service exception: ${e.message}")
        return ResponseEntity.status(e.status).body(ErrorResponse(e.code, e.localizedMessage))
    }
}

@EnableWebFlux
@Configuration
class WebConfig(private val mapper: ObjectMapper) : WebFluxConfigurer {

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.apply {
            defaultCodecs().enableLoggingRequestDetails(true)
            customCodecs().registerWithDefaultConfig(Jackson2JsonDecoder(mapper))
            customCodecs().registerWithDefaultConfig(Jackson2JsonEncoder(mapper))
        }
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.apply {
            // cannot use lambda here because spring needs the parameterized types
            addConverter(object : Converter<String, LocalDateTime> {
                override fun convert(source: String) = source.toDatetime()
            })
            addConverter(object : Converter<String, LocalDate> {
                override fun convert(source: String) = source.toDate()
            })
            addConverter(object : Converter<String, LocalTime> {
                override fun convert(source: String) = source.toTime()
            })
        }
    }
}

@Configuration
class FilterConfig {

    @Bean
    @Order(1)
    fun loginFilter() = WebFilter { exchange, chain ->
        // TODO add auth
        chain.filter(exchange)
    }

    @Bean
    @Order(2)
    fun contextFilter() = ServerWebExchangeContextFilter()
}

@Configuration
class JsonConfig {

    @Bean
    @Primary
    fun objectMapper() = JSON
}

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(builder: WebClient.Builder) = builder.build()
}

@Configuration
class SwaggerConfig {

    @Bean
    fun operationCustomizer() = OperationCustomizer { operation, _ ->
        operation.addParametersItem(
            Parameter().`in`(ParameterIn.HEADER.toString()).schema(StringSchema()).name("token")
        )
    }
}
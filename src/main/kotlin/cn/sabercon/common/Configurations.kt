package cn.sabercon.common

import cn.sabercon.common.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.WebFilter
import reactor.netty.http.client.HttpClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@EnableWebFlux
@Configuration
class WebConfig : WebFluxConfigurer {

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.apply {
            defaultCodecs().enableLoggingRequestDetails(true)
            customCodecs().registerWithDefaultConfig(Jackson2JsonDecoder(JSON))
            customCodecs().registerWithDefaultConfig(Jackson2JsonEncoder(JSON))
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
    fun corsFilter(): WebFilter {
        val config = CorsConfiguration().apply {
            allowedOriginPatterns = listOf(CorsConfiguration.ALL)
            allowedMethods = listOf(CorsConfiguration.ALL)
            allowedHeaders = listOf(CorsConfiguration.ALL)
            allowCredentials = false
        }

        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }

        return CorsWebFilter(source)
    }

    @Bean
    @Order(2)
    fun loginFilter() = WebFilter { exchange, chain ->
        exchange.attributes["userId"] = exchange.request.headers[HttpHeaders.AUTHORIZATION]
            ?.getOrNull(0)
            ?.takeIf { it.startsWith("Bearer ", true) }
            ?.substring("Bearer ".length)
            ?.let { JwtUtils.decodeToken(it) }
            ?: 0

        chain.filter(exchange)
    }
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
    fun webClient(builder: WebClient.Builder) = builder
        .clientConnector(ReactorClientHttpConnector(HttpClient.create().responseTimeout(30.seconds)))
        .codecs { it.defaultCodecs().maxInMemorySize(8 * 1000 * 1000) }
        .build()
}
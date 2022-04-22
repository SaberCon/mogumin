package cn.sabercon.common.web.config

import cn.sabercon.common.Jwt
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.WebFilter

@Configuration
class FilterConfiguration {

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
    fun loginFilter(jwt: Jwt) = WebFilter { exchange, chain ->
        exchange.attributes["userId"] = exchange.request.headers[HttpHeaders.AUTHORIZATION]
            ?.getOrNull(0)
            ?.takeIf { it.startsWith("Bearer ", true) }
            ?.substring("Bearer ".length)
            ?.let { jwt.decodeToken(it) }

        chain.filter(exchange)
    }
}
package cn.sabercon.common.web

import cn.sabercon.common.Jwt
import cn.sabercon.common.data.USER_ID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.server.WebFilter

@EnableWebFlux
@Configuration
class WebFluxConfiguration : WebFluxConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns(CorsConfiguration.ALL)
            .allowedMethods(CorsConfiguration.ALL)
            .allowedHeaders(CorsConfiguration.ALL)
            .allowCredentials(false)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
    }

    @Bean
    fun loginFilter(jwt: Jwt) = WebFilter { exchange, chain ->
        exchange.attributes[USER_ID] = exchange.request.headers[HttpHeaders.AUTHORIZATION]
            ?.getOrNull(0)
            ?.takeIf { it.startsWith("Bearer ", true) }
            ?.substring("Bearer ".length)
            ?.let { jwt.decodeToken(it) }
            ?: 0

        chain.filter(exchange)
    }
}

package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.base.ContextHolder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

val webClient: WebClient by lazy { ContextHolder.getBean() }

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(builder: WebClient.Builder) = builder.build()
}
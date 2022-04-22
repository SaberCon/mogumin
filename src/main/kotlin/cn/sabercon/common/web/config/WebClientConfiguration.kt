package cn.sabercon.common.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient {
        return builder.codecs { it.defaultCodecs().maxInMemorySize(8 * 1000 * 1000) }.build()
    }
}
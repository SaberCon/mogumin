package cn.sabercon.megumin.aliyun

import cn.sabercon.common.web.body
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class OssRouterConfiguration {

    @Bean
    fun ossRouter(client: AliyunClient) = coRouter {
        GET("/oss") { body(client.buildOssData()) }
    }
}
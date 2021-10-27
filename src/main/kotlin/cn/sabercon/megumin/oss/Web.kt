package cn.sabercon.megumin.oss

import cn.sabercon.common.ext.coRouter
import cn.sabercon.common.ext.get
import cn.sabercon.common.ext.success
import cn.sabercon.megumin.client.AliyunClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OssRouterConfig {

    @Bean
    fun ossRouter(client: AliyunClient) = coRouter("/oss") {
        get { success(client.buildOssData()) }
    }
}
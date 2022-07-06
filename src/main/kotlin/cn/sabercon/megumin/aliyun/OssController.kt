package cn.sabercon.megumin.aliyun

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class OssController(private val aliyunClient: AliyunClient) {

    @QueryMapping
    fun ossData() = aliyunClient.buildOssData()
}

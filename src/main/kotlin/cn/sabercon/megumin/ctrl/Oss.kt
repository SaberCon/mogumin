package cn.sabercon.megumin.ctrl

import cn.sabercon.common.WebController
import cn.sabercon.common.client.AliyunClient
import org.springframework.web.bind.annotation.GetMapping

@WebController(["oss"])
class OssController(private val aliyunClient: AliyunClient) {

    @GetMapping
    suspend fun buildOssData() = aliyunClient.buildOssData()
}
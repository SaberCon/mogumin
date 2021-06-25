package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.util.AliyunHelper
import org.springframework.web.bind.annotation.GetMapping

@WebService(["oss"])
class OssService(private val aliyunHelper: AliyunHelper) {

    @GetMapping
    suspend fun buildOssData() = aliyunHelper.buildOssData()
}
package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.extension.get
import cn.sabercon.mogumin.extension.set
import cn.sabercon.mogumin.util.AliyunHelper
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping

@WebService(["sms"])
class SmsService(private val redisOps: ReactiveStringRedisTemplate, private val aliyunHelper: AliyunHelper) {

    @GetMapping
    suspend fun sendCode(type: Int, phone: String) {
        redisOps.set("$SMS_CODE_KEY:${type}:$phone", aliyunHelper.sendSmsCode(phone), "5m")
    }

    suspend fun checkCode(type: SmsType, phone: String, code: String) =
        redisOps.get<String>("$SMS_CODE_KEY:${type.ordinal}:$phone") == code
}

enum class SmsType {
    LOGIN, UPDATE_PWD, BIND_PHONE, UNBIND_PHONE
}

const val SMS_CODE_KEY = "sms"
package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.util.AliyunHelper
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping

@WebService(["sms"])
class SmsService(private val redisOps: ReactiveStringRedisTemplate, private val aliyunHelper: AliyunHelper) {

    @GetMapping
    suspend fun sendCode(type: Int, phone: String) {
        aliyunHelper.sendSmsCode(phone)
    }

    suspend fun checkCode(type: SmsType, phone: String, code: String) = true
}

enum class SmsType(val code: Int) {
    LOGIN(1),
    UPDATE_PWD(2),
    BIND_PHONE(3),
    UNBIND_PHONE(4),
}
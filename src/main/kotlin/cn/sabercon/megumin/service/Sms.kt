package cn.sabercon.megumin.service

import cn.sabercon.common.BaseCode
import cn.sabercon.common.client.AliyunClient
import cn.sabercon.common.data.REDIS
import cn.sabercon.common.data.get
import cn.sabercon.common.data.set
import cn.sabercon.common.util.ensure
import cn.sabercon.common.util.minutes
import org.springframework.data.redis.core.deleteAndAwait
import org.springframework.stereotype.Service

@Service
class SmsService(private val aliyunClient: AliyunClient) {

    suspend fun sendCode(type: Int, phone: String) {
        REDIS.set("$SMS_CODE_KEY:$type:$phone", aliyunClient.sendSmsCode(phone), 10.minutes)
    }

    suspend fun checkCode(type: Int, phone: String, code: String) {
        val key = "$SMS_CODE_KEY:$type:$phone"
        ensure(REDIS.get<String>(key) == code, BaseCode.INVALID_SMS_CODE)
        REDIS.deleteAndAwait(key)
    }
}

const val SMS_CODE_KEY = "sms"
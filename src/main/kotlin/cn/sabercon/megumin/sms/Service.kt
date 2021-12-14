package cn.sabercon.megumin.sms

import cn.sabercon.common.data.redis.get
import cn.sabercon.common.data.redis.set
import cn.sabercon.common.throwClientError
import cn.sabercon.common.util.minutes
import cn.sabercon.megumin.client.AliyunClient
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service

@Service
class SmsHandler(private val aliyunClient: AliyunClient, private val redis: ReactiveStringRedisTemplate) {

    suspend fun sendCode(type: SmsType, phone: String) {
        redis.set("$SMS_CODE_PREFIX${type.code}:$phone", aliyunClient.sendSmsCode(phone), 10.minutes)
    }

    suspend fun checkCode(type: SmsType, phone: String, code: String) {
        if (redis.get<String>("$SMS_CODE_PREFIX${type.code}:$phone") != code) {
            throwClientError("Invalid sms code")
        }
    }
}

private const val SMS_CODE_PREFIX = "sms:"
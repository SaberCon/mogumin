package cn.sabercon.megumin.aliyun

import cn.sabercon.common.data.redis.getValue
import cn.sabercon.common.data.redis.setValue
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.minutes

@Service
class SmsHandler(private val aliyunClient: AliyunClient, private val redis: ReactiveStringRedisTemplate) {

    suspend fun sendCode(type: SmsType, phone: String) {
        redis.setValue("$SMS_CODE_PREFIX${type.code}:$phone", aliyunClient.sendSmsCode(phone), 10.minutes)
    }

    suspend fun checkCode(type: SmsType, phone: String, code: String): Boolean {
        return redis.getValue<String>("$SMS_CODE_PREFIX${type.code}:$phone") == code
    }
}

enum class SmsType(val code: Int, val authRequired: Boolean) {
    LOGIN(1, false),
    UPDATE_PWD(2, true),
    BIND_PHONE(3, false),
    UNBIND_PHONE(4, true),
}

private const val SMS_CODE_PREFIX = "sms:"

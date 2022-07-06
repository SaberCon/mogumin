package cn.sabercon.megumin.aliyun

import cn.sabercon.common.data.redis.getValue
import cn.sabercon.common.data.redis.setValue
import cn.sabercon.dgs.codegen.generated.types.SmsType
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.minutes

@Service
class SmsService(private val aliyunClient: AliyunClient, private val redis: ReactiveStringRedisTemplate) {

    suspend fun sendCode(type: SmsType, phone: String) {
        redis.setValue("$SMS_CODE_PREFIX$type:$phone", aliyunClient.sendSmsCode(phone), 10.minutes)
    }

    suspend fun checkCode(type: SmsType, phone: String, code: String): Boolean {
        return redis.getValue<String>("$SMS_CODE_PREFIX$type:$phone") == code
    }
}

private const val SMS_CODE_PREFIX = "sms:"

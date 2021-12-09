package cn.sabercon.megumin.sms

import cn.sabercon.common.data.redis.get
import cn.sabercon.common.data.redis.set
import cn.sabercon.common.util.ensure
import cn.sabercon.common.util.minutes
import cn.sabercon.megumin.MeguminCode
import cn.sabercon.megumin.client.AliyunClient
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service

@Service
class SmsHandler(private val aliyunClient: AliyunClient, private val redis: ReactiveStringRedisTemplate) {

    suspend fun sendCode(type: Int, phone: String) {
        redis.set("$SMS_CODE_PREFIX$type:$phone", aliyunClient.sendSmsCode(phone), 10.minutes)
    }

    suspend fun checkCode(type: Int, phone: String, code: String) {
        val key = "$SMS_CODE_PREFIX$type:$phone"
        ensure(redis.get<String>(key) == code, MeguminCode.INVALID_SMS_CODE)
    }
}

const val SMS_CODE_PREFIX = "sms:"
package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.extension.get
import cn.sabercon.mogumin.extension.set
import cn.sabercon.mogumin.util.AliyunHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping

@WebService(["sms"])
class SmsService(private val redisOps: ReactiveStringRedisTemplate, private val aliyunHelper: AliyunHelper) {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("send")
    suspend fun sendCode(type: Int, phone: String?) {
        val targetPhone = phone ?: userService.findCurrentUser().phone
        redisOps.set("$SMS_CODE_KEY:$type:$targetPhone", aliyunHelper.sendSmsCode(targetPhone), "5m")
    }

    @GetMapping("check")
    suspend fun checkCode(type: Int, code: String, phone: String?): Boolean {
        val targetPhone = phone ?: userService.findCurrentUser().phone
        return redisOps.get<String>("$SMS_CODE_KEY:${type}:$targetPhone") == code
    }
}

enum class SmsType(val code: Int) {
    LOGIN(1), UPDATE_PWD(2), BIND_PHONE(3), UNBIND_PHONE(4)
}

const val SMS_CODE_KEY = "sms"
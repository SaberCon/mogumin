package cn.sabercon.mogumin.service

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("sms")
class SmsService {

    suspend fun sendCode(type: Int, phone: String): Unit = TODO()

    suspend fun checkCode(type: SmsType, phone: String, code: String) = true
}

enum class SmsType(val code: Int) {
    LOGIN(1),
    UPDATE_PWD(2),
    BIND_PHONE(3),
    UNBIND_PHONE(4),
}
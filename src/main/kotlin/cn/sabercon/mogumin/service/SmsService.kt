package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.WebService

@WebService
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
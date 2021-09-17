package cn.sabercon.megumin.ctrl

import cn.sabercon.common.BaseCode
import cn.sabercon.common.WebController
import cn.sabercon.megumin.service.SmsService
import cn.sabercon.megumin.service.UserService
import org.springframework.web.bind.annotation.GetMapping

@WebController(["sms"])
class SmsController(
    private val service: SmsService,
    private val userService: UserService,
) {

    @GetMapping("send")
    suspend fun sendCode(type: Int, phone: String?) = service.sendCode(type, getPhone(type, phone))

    @GetMapping("check")
    suspend fun checkCode(type: Int, phone: String?, code: String) =
        service.checkCode(type, getPhone(type, phone), code)

    suspend fun getPhone(type: Int, phone: String?): String {
        return SmsType.values().firstOrNull { it.code == type }?.let {
            if (it.authRequired) userService.getCurrentUser().phone else phone
        } ?: BaseCode.BAD_REQUEST.throws()
    }
}

enum class SmsType(val code: Int, val authRequired: Boolean) {
    LOGIN(1, false),
    UPDATE_PWD(2, true),
    BIND_PHONE(3, true),
    UNBIND_PHONE(4, true),
}
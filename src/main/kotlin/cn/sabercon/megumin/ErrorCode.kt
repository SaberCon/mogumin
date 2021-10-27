package cn.sabercon.megumin

import cn.sabercon.common.ErrorCode
import org.springframework.http.HttpStatus

enum class MeguminCode(override val code: String, override val msg: String) : ErrorCode {
    INVALID_SMS_CODE("20001", "Invalid sms code"),
    LOGIN_ERROR("20002", "Invalid phone or password"),
    PHONE_BOUND("20003", "This phone has been bound to other account"),
    ;

    override val status = HttpStatus.BAD_REQUEST
}
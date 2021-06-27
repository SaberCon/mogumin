package cn.sabercon.mogumin.base

interface ErrorCode {
    val code: String
    val msg: String
    fun throws(msg: String = this.msg): Nothing = throw ServiceException(code, msg)
}

enum class BaseCode(override val code: String, override val msg: String) : ErrorCode {
    FAILURE("10001", "failure"),
    UNAUTHORIZED("10002", "unauthorized"),

    SMS_CODE_WRONG("20001", "the sms code is wrong"),
    LOGIN_ERROR("20002", "the phone or password is wrong"),
    PHONE_ALREADY_BOUND("20003", "the phone has been bound to other account"),
}

class ServiceException(val code: String, msg: String) : RuntimeException(msg)
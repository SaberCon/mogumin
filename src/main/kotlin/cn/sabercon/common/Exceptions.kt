package cn.sabercon.common

import org.springframework.http.HttpStatus

interface ErrorCode {
    val code: String
    val msg: String
    val status: HttpStatus
    fun error(msg: String = this.msg) = ServiceException(status, code, msg)
    fun throws(msg: String = this.msg): Nothing = throw error(msg)
}

enum class BaseCode(
    override val code: String,
    override val msg: String,
    override val status: HttpStatus = HttpStatus.BAD_REQUEST
) : ErrorCode {
    BAD_REQUEST("400", "Bad Request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("401", "Unauthorized", HttpStatus.UNAUTHORIZED),
    I_AM_A_TEAPOT("418", "I'm a teapot", HttpStatus.I_AM_A_TEAPOT),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    ILLEGAL_ARGUMENT("400", "Illegal argument", HttpStatus.BAD_REQUEST),
    ILLEGAL_STATE("500", "Illegal state", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_SMS_CODE("20001", "Invalid sms code"),
    LOGIN_ERROR("20002", "Invalid phone or password"),
    PHONE_BOUND("20003", "This phone has been bound to other account"),
}

class ServiceException(val status: HttpStatus, val code: String, msg: String) : RuntimeException(msg)
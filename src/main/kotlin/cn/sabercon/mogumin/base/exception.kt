package cn.sabercon.mogumin.base

interface ErrorCode {
    val code: String
    val msg: String
    fun throws(msg: String = this.msg): Nothing = throw ServiceException(code, msg)
}

enum class BaseCode(override val code: String, override val msg: String) : ErrorCode {
    FAILURE("10001", "failure"),
    ASSERTION_ERROR("10002", "assertion failed"),
    PARAM_WRONG("10003", "param is wrong"),
    UNAUTHORIZED("10004", "unauthorized"),
}

class ServiceException(val code: String, msg: String) : RuntimeException(msg)
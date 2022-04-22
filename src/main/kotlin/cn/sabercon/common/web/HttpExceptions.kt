package cn.sabercon.common.web

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class HttpException(val status: HttpStatus, val code: Int, message: String) : RuntimeException(message)

interface ErrorCode {
    val status: HttpStatus
    val code: Int
    val message: String
    fun error(message: String = this.message) = HttpException(status, code, message)
    fun throws(message: String = this.message): Nothing = throw error(message)
}

fun HttpStatus.error(reason: String? = null) = ResponseStatusException(this, reason)

fun HttpStatus.throws(reason: String? = null): Nothing = throw error(reason)

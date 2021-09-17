package cn.sabercon.common.util

import cn.sabercon.common.BaseCode
import cn.sabercon.common.ErrorCode
import org.springframework.util.Base64Utils
import java.security.MessageDigest

inline fun ensure(value: Boolean, code: ErrorCode = BaseCode.BAD_REQUEST, lazyMessage: () -> String = { code.msg }) {
    if (!value) {
        code.throws(lazyMessage())
    }
}

fun sha256(input: String) = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    .let { Base64Utils.encodeToString(it) }
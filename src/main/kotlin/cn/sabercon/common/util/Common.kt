package cn.sabercon.common.util

import cn.sabercon.common.BaseCode
import cn.sabercon.common.ErrorCode

inline fun ensure(value: Boolean, code: ErrorCode = BaseCode.BAD_REQUEST, lazyMessage: () -> String = { code.msg }) {
    if (!value) {
        code.throws(lazyMessage())
    }
}
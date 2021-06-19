package cn.sabercon.mogumin.base

import cn.sabercon.mogumin.util.RedisKey
import java.time.Duration

// redis key
val LOGIN_USER_PREFIX = RedisKey("login:", Duration.ofDays(1))


// string
const val TOKEN_HEADER = "token"

const val USER_ID = "userId"

const val DEFAULT_AVATAR = "http://oss.sabercon.cn/base/tagaki.jpg"
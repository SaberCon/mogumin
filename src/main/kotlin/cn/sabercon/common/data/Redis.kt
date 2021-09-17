package cn.sabercon.common.data

import cn.sabercon.common.util.ContextHolder
import cn.sabercon.common.util.parseJson
import cn.sabercon.common.util.toJson
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import java.time.Duration

val REDIS: ReactiveStringRedisTemplate by lazy { ContextHolder.getBean() }

suspend fun ReactiveStringRedisTemplate.set(key: String, value: Any, timeout: Duration? = null) =
    when (timeout) {
        null -> opsForValue().setAndAwait(key, value.toJson())
        else -> opsForValue().setAndAwait(key, value.toJson(), timeout)
    }

suspend inline fun <reified T : Any> ReactiveStringRedisTemplate.get(key: String): T? =
    opsForValue().getAndAwait(key)?.parseJson()
package cn.sabercon.common.data.redis

import cn.sabercon.common.ext.format
import cn.sabercon.common.ext.parse
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import java.time.Duration

suspend fun ReactiveStringRedisTemplate.set(key: String, value: Any, timeout: Duration? = null) =
    when (timeout) {
        null -> opsForValue().setAndAwait(key, value.format())
        else -> opsForValue().setAndAwait(key, value.format(), timeout)
    }

suspend inline fun <reified T : Any> ReactiveStringRedisTemplate.get(key: String): T? =
    opsForValue().getAndAwait(key)?.parse()
package cn.sabercon.mogumin.util

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import java.time.Duration

suspend fun ReactiveStringRedisTemplate.set(key: String, value: Any, timeout: Duration? = null) =
    timeout?.let { opsForValue().setAndAwait(key, toJson(value), it) }
        ?: opsForValue().setAndAwait(key, toJson(value))

suspend inline fun <reified T: Any> ReactiveStringRedisTemplate.get(key: String): T? =
    opsForValue().getAndAwait(key)?.let { parseJson(it) }
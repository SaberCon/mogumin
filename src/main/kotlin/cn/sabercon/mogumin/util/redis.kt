package cn.sabercon.mogumin.util

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import java.time.Duration

data class RedisKey(val name: String, val timeout: Duration? = null) {
    fun addSuffix(vararg suffix: String) = copy(name = name + suffix.joinToString(":"))
}

suspend fun ReactiveStringRedisTemplate.set(redisKey: RedisKey, value: Any) =
    redisKey.timeout?.let { opsForValue().setAndAwait(redisKey.name, toJson(value), it) }
        ?: opsForValue().setAndAwait(redisKey.name, toJson(value))

suspend inline fun <reified T> ReactiveStringRedisTemplate.get(key: String): T? =
    opsForValue().getAndAwait(key)?.let { parseJson<T>(it) }
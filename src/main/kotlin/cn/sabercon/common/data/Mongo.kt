package cn.sabercon.common.data

import cn.sabercon.common.util.copy
import cn.sabercon.common.util.getProperty
import cn.sabercon.common.util.now
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.mapping.event.ReactiveAfterSaveCallback
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import reactor.kotlin.core.publisher.toMono
import kotlin.reflect.KProperty

@Configuration
class MongoConfig {

    @Bean
    fun mongoBeforeConvertCallback() = ReactiveBeforeConvertCallback<Any> { entity, _ ->
        val id: String = entity.getProperty(ID)
        when {
            id.isEmpty() -> entity.copy(CTIME to now, MTIME to now).toMono()
            else -> entity.copy(MTIME to now).toMono()
        }
    }

    @Bean
    fun mongoAfterSaveCallback() = ReactiveAfterSaveCallback<Any> { entity, document, _ ->
        val id: String = entity.getProperty(ID)
        when {
            id.isEmpty() -> entity.copy(ID to document.getObjectId("_id").toString()).toMono()
            else -> entity.toMono()
        }
    }
}

// query
infix fun <T> KProperty<T>.eq(value: T) = isEqualTo(value)

// update
fun update() = Update.update(MTIME, now)

fun <T> Update.set(key: KProperty<T>, value: T) = set(asString(key), value)

fun <T> update(key: KProperty<T>, value: T) = update().set(key, value)

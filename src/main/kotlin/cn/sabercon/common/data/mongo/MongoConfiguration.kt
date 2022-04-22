package cn.sabercon.common.data.mongo

import cn.sabercon.common.data.CTIME
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.MTIME
import cn.sabercon.common.util.UnsafeReflection
import cn.sabercon.common.util.now
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback
import reactor.kotlin.core.publisher.toMono

@Configuration
class MongoConfiguration {

    @Bean
    fun mongoBeforeConvertCallback() = ReactiveBeforeConvertCallback<Any> { entity, _ ->
        val id: String = UnsafeReflection.get(entity, ID)
        val now = now()

        when (id) {
            "" -> UnsafeReflection.modifyData(entity, ID to ObjectId().toString(), CTIME to now, MTIME to now).toMono()
            else -> UnsafeReflection.modifyData(entity, MTIME to now).toMono()
        }
    }
}
package cn.sabercon.common.data.mongo

import cn.sabercon.common.data.CREATED_AT
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.UPDATED_AT
import cn.sabercon.common.util.UnsafeReflection
import cn.sabercon.common.util.UnsafeReflection.modifyData
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
            "" -> modifyData(entity, ID to ObjectId().toString(), CREATED_AT to now, UPDATED_AT to now).toMono()
            else -> modifyData(entity, UPDATED_AT to now).toMono()
        }
    }
}

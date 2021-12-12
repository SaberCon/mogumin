package cn.sabercon.common.data.mongo

import cn.sabercon.common.data.CTIME
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.MTIME
import cn.sabercon.common.util.copy
import cn.sabercon.common.util.getProperty
import cn.sabercon.common.util.now
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback
import reactor.kotlin.core.publisher.toMono

@Configuration
class MongoConfig {

    @Bean
    fun mongoBeforeConvertCallback() = ReactiveBeforeConvertCallback<Any> { entity, _ ->
        when (entity.getProperty<String>(ID)) {
            "" -> entity.copy(ID to ObjectId().toString(), CTIME to now, MTIME to now).toMono()
            else -> entity.copy(MTIME to now).toMono()
        }
    }
}
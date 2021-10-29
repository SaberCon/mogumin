package cn.sabercon.common.data.mongo

import cn.sabercon.common.data.CTIME
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.MTIME
import cn.sabercon.common.util.copy
import cn.sabercon.common.util.getProperty
import cn.sabercon.common.util.now
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.mapping.event.ReactiveAfterSaveCallback
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback
import reactor.kotlin.core.publisher.toMono

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
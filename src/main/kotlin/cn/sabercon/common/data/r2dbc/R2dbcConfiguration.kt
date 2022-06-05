package cn.sabercon.common.data.r2dbc

import cn.sabercon.common.data.CREATED_AT
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.UPDATED_AT
import cn.sabercon.common.util.UnsafeReflection
import cn.sabercon.common.util.UnsafeReflection.modifyData
import cn.sabercon.common.util.now
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import reactor.kotlin.core.publisher.toMono

@Configuration
class R2dbcConfiguration {

    @Bean
    fun sqlBeforeConvertCallback() = BeforeConvertCallback<Any> { entity, _ ->
        val id: Long = UnsafeReflection.get(entity, ID)
        val now = now()

        when {
            id <= 0 -> modifyData(entity, CREATED_AT to now, UPDATED_AT to now).toMono()
            else -> modifyData(entity, UPDATED_AT to now).toMono()
        }
    }
}

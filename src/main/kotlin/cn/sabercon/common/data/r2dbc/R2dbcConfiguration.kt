package cn.sabercon.common.data.r2dbc

import cn.sabercon.common.data.CTIME
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.MTIME
import cn.sabercon.common.util.UnsafeReflection
import cn.sabercon.common.util.now
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
import reactor.kotlin.core.publisher.toMono

@Configuration
class R2dbcConfiguration {

    @Bean
    fun namingStrategy() = object : NamingStrategy {

        override fun getTableName(type: Class<*>) = "t_${super.getTableName(type)}"

        override fun getColumnName(property: RelationalPersistentProperty) = "f_${super.getColumnName(property)}"
    }

    @Bean
    fun sqlBeforeConvertCallback() = BeforeConvertCallback<Any> { entity, _ ->
        val id: Long = UnsafeReflection.get(entity, ID)
        val now = now()

        when {
            id <= 0 -> UnsafeReflection.modifyData(entity, CTIME to now, MTIME to now).toMono()
            else -> UnsafeReflection.modifyData(entity, MTIME to now).toMono()
        }
    }
}

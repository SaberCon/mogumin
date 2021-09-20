package cn.sabercon.common.data

import cn.sabercon.common.util.ContextHolder
import cn.sabercon.common.util.copy
import cn.sabercon.common.util.getProperty
import cn.sabercon.common.util.now
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import reactor.kotlin.core.publisher.toMono

val SQL: R2dbcEntityTemplate by lazy { ContextHolder.getBean() }

@Configuration
class R2dbcConfig {

    @Bean
    fun initializer(connectionFactory: ConnectionFactory) = ConnectionFactoryInitializer().apply {
        setConnectionFactory(connectionFactory)
        val resource = ResourceDatabasePopulator(ClassPathResource("schema.sql"))
        setDatabasePopulator(resource)
    }

    @Bean
    fun namingStrategy() = object : NamingStrategy {
        override fun getTableName(type: Class<*>) = "t_${super.getTableName(type)}"

        override fun getColumnName(property: RelationalPersistentProperty) = "f_${super.getColumnName(property)}"
    }

    @Bean
    fun sqlBeforeConvertCallback() = BeforeConvertCallback<Any> { entity, _ ->
        val id: Long = entity.getProperty(ID)
        when {
            id <= 0 -> entity.copy(CTIME to now, MTIME to now).toMono()
            else -> entity.copy(MTIME to now).toMono()
        }
    }

}
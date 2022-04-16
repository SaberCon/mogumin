package cn.sabercon.common.data.r2dbc

import cn.sabercon.common.data.CTIME
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.MTIME
import cn.sabercon.common.util.UnsafeReflection
import cn.sabercon.common.util.now
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.Option
import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
import reactor.kotlin.core.publisher.toMono

@Configuration
class R2dbcConfig {

    @Bean(initMethod = "migrate")
    fun flyway(props: R2dbcProperties): Flyway {
        val options = ConnectionFactoryOptions.parse(props.url)
        val driver = options.getValue(Option.valueOf<String>("driver")) as String
        val host = options.getValue(Option.valueOf<String>("host")) as String
        val port = options.getValue(Option.valueOf<Int>("port")) as Int
        val database = options.getValue(Option.valueOf<String>("database")) as String
        val user = options.getValue(Option.valueOf<String>("user")) as String
        val password = options.getValue(Option.sensitiveValueOf<String>("password")) as String
        val jdbcUrl = "jdbc:$driver://$host:$port/$database"

        return Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(jdbcUrl, user, password)
            .load()
    }

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
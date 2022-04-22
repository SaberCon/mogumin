package cn.sabercon.common.data.r2dbc

import io.r2dbc.spi.ConnectionFactoryOptions
import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfiguration {

    @Bean(initMethod = "migrate")
    fun flyway(props: R2dbcProperties): Flyway {
        val options = ConnectionFactoryOptions.parse(props.url)
        val driver = options.getValue(ConnectionFactoryOptions.DRIVER) as String
        val host = options.getValue(ConnectionFactoryOptions.HOST) as String
        val port = options.getValue(ConnectionFactoryOptions.PORT) as Int
        val database = options.getValue(ConnectionFactoryOptions.DATABASE) as String
        val user = options.getValue(ConnectionFactoryOptions.USER) as String
        val password = options.getValue(ConnectionFactoryOptions.PASSWORD) as String
        val jdbcUrl = "jdbc:$driver://$host:$port/$database"

        return Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(jdbcUrl, user, password)
            .load()
    }
}

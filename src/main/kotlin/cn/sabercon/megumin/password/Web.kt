package cn.sabercon.megumin.password

import cn.sabercon.common.ext.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PasswordRouterConfig {

    @Bean
    fun passwordRouter(handler: PasswordHandler) = coRouter("/pwd") {
        get("/{id}") { success(handler.get(it.userId(), it.objectIdPathParam())) }

        get { success(handler.list(it.userId(), it.pageable())) }

        post {
            handler.save(it.userId(), it.body())
            success()
        }

        delete("/{id}") {
            handler.delete(it.userId(), it.objectIdPathParam())
            success()
        }

        delete {
            handler.delete(it.userId(), it.body<List<String>>())
            success()
        }
    }
}

data class PasswordParam(
    val id: String = "",
    val name: String,
    val pwd: String,
    val username: String?,
    val website: String?,
    val desc: String?,
)
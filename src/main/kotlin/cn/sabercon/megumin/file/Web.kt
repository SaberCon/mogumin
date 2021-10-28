package cn.sabercon.megumin.file

import cn.sabercon.common.ext.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FileRouterConfig {

    @Bean
    fun fileRouter(handler: FileHandler) = coRouter("/file") {
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

data class FileParam(
    val id: String = "",
    val name: String,
    val url: String,
    val size: String,
)
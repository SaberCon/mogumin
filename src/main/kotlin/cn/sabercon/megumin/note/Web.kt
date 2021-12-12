package cn.sabercon.megumin.note

import cn.sabercon.common.ext.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NoteRouterConfig {

    @Bean
    fun noteRouter(handler: NoteHandler) = coRouter("/note") {
        get("/{id}") { success(handler.get(it.userId(), it.objectIdPathParam())) }

        get { success(handler.list(it.userId())) }

        post {
            handler.save(it.userId(), it.body())
            success()
        }

        delete("/{id}") {
            handler.delete(it.userId(), it.objectIdPathParam())
            success()
        }
    }
}

data class NoteParam(
    val id: String = "",
    val title: String,
    val content: String,
)
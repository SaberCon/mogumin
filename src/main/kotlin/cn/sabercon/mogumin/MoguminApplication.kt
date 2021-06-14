package cn.sabercon.mogumin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing

@EnableReactiveMongoAuditing
@SpringBootApplication
class MoguminApplication

fun main(args: Array<String>) {
    runApplication<MoguminApplication>(*args)
}

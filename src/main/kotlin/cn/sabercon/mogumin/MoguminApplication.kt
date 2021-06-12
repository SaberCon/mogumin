package cn.sabercon.mogumin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoguminApplication

fun main(args: Array<String>) {
    runApplication<MoguminApplication>(*args)
}

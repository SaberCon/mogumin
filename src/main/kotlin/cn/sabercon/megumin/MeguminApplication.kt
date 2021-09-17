package cn.sabercon.megumin

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component

@ComponentScan("cn.sabercon")
@SpringBootApplication
class MeguminApplication

fun main(args: Array<String>) {
    runApplication<MeguminApplication>(*args)
}

@Component
class JobRunner : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        // to run some ad-hoc tasks
    }

}

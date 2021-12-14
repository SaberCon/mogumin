package cn.sabercon.common.util

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ContextHolder : ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        CONTEXT = applicationContext
    }

    companion object {

        lateinit var CONTEXT: ApplicationContext

        fun getProperty(key: String, defaultValue: String = "") = CONTEXT.environment.getProperty(key) ?: defaultValue

        inline fun <reified T : Any> getBean(name: String): T = CONTEXT.getBean<T>(name)

        inline fun <reified T : Any> getBean(): T = CONTEXT.getBean()

        fun isLocal() = CONTEXT.environment.activeProfiles.contains("local")

        fun isTest() = CONTEXT.environment.activeProfiles.contains("test")

        fun isProd() = CONTEXT.environment.activeProfiles.contains("prod")
    }
}
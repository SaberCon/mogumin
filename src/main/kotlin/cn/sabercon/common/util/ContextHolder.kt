package cn.sabercon.common.util

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class ContextHolder : ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        CONTEXT = applicationContext
    }

    companion object {

        private lateinit var CONTEXT: ApplicationContext

        fun getContext(): ApplicationContext = CONTEXT

        fun getEnv(): Environment = getContext().environment

        inline fun <reified T : Any> getBean(name: String): T = getContext().getBean<T>(name)

        inline fun <reified T : Any> getBean(): T = getContext().getBean()

        fun getProperty(key: String, defaultValue: String = ""): String = getEnv().getProperty(key) ?: defaultValue

        fun getProfiles(): Array<String> = getEnv().activeProfiles
    }
}

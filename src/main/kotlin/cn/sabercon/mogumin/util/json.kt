package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.base.ContextHolder
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val jsonMapper: ObjectMapper by lazy { ContextHolder.getBean() }

private val TIME_MODULE =
    JavaTimeModule().addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DATETIME_FMT))!!
        .addSerializer(LocalDate::class.java, LocalDateSerializer(DATE_FMT))!!
        .addSerializer(LocalTime::class.java, LocalTimeSerializer(TIME_FMT))!!
        .addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DATETIME_FMT))!!
        .addDeserializer(LocalDate::class.java, LocalDateDeserializer(DATE_FMT))!!
        .addDeserializer(LocalTime::class.java, LocalTimeDeserializer(TIME_FMT))!!

@Configuration
class JsonConfig {

    @Bean
    @Primary
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper =
        builder.modulesToInstall(TIME_MODULE).modulesToInstall(kotlinModule())
            .serializationInclusion(JsonInclude.Include.NON_NULL).build()
}

fun toJson(obj: Any) = jsonMapper.writeValueAsString(obj)!!

inline fun <reified T: Any> parseJson(str: String): T = jsonMapper.readValue(str)

inline fun <reified T: Any> convert(obj: Any): T = jsonMapper.convertValue(obj)
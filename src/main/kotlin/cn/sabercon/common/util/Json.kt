package cn.sabercon.common.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private val TIME_MODULE = JavaTimeModule()
    .addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DATETIME_FMT))
    .addSerializer(LocalDate::class.java, LocalDateSerializer(DATE_FMT))
    .addSerializer(LocalTime::class.java, LocalTimeSerializer(TIME_FMT))
    .addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DATETIME_FMT))
    .addDeserializer(LocalDate::class.java, LocalDateDeserializer(DATE_FMT))
    .addDeserializer(LocalTime::class.java, LocalTimeDeserializer(TIME_FMT))

val JSON = jacksonMapperBuilder()
    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true)
    .serializationInclusion(JsonInclude.Include.NON_NULL)
    .addModules(TIME_MODULE)
    .build()!!

fun Any?.toJson() = JSON.writeValueAsString(this)!!

inline fun <reified T : Any> String.parseJson(): T = JSON.readValue(this)
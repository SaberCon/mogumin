package cn.sabercon.common.util

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue

val JSON = jacksonMapperBuilder()
    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true)
    .addModules(JavaTimeModule())
    .build()!!

fun Any.toJson() = JSON.writeValueAsString(this)!!

fun String.parseJsonNode() = JSON.readTree(this)!!

inline fun <reified T : Any> String.parseJson(): T = JSON.readValue(this)
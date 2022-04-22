package cn.sabercon.common.util

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue

val JSON: ObjectMapper = jacksonMapperBuilder()
    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true)
    .addModules(JavaTimeModule())
    .build()

fun Any.toJson(): String = JSON.writeValueAsString(this)

fun String.toJsonNode(): JsonNode = JSON.readTree(this)

inline fun <reified T : Any> String.toJsonObject(): T = JSON.readValue(this)
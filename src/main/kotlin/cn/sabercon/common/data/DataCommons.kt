package cn.sabercon.common.data

import org.springframework.data.domain.Sort
import kotlin.reflect.KProperty

const val ID = "id"
const val USER_ID = "userId"
const val CREATED_AT = "createdAt"
const val UPDATED_AT = "updatedAt"

fun asc(vararg properties: String): Sort = Sort.by(properties.map { Sort.Order.asc(it) })
fun desc(vararg properties: String): Sort = Sort.by(properties.map { Sort.Order.desc(it) })
fun asc(vararg properties: KProperty<*>): Sort = Sort.by(properties.map { Sort.Order.asc(asString(it)) })
fun desc(vararg properties: KProperty<*>): Sort = Sort.by(properties.map { Sort.Order.desc(asString(it)) })

package cn.sabercon.common.data

import org.springframework.data.domain.Sort
import kotlin.reflect.KProperty

const val ID = "id"
const val CTIME = "ctime"
const val MTIME = "mtime"

fun asc(vararg properties: String): Sort = Sort.by(Sort.Direction.ASC, *properties)
fun desc(vararg properties: String): Sort = Sort.by(Sort.Direction.DESC, *properties)
fun asc(vararg properties: KProperty<*>): Sort = Sort.by(properties.map { Sort.Order.asc(asString(it)) })
fun desc(vararg properties: KProperty<*>): Sort = Sort.by(properties.map { Sort.Order.desc(asString(it)) })

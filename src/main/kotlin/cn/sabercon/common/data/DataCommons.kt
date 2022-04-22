package cn.sabercon.common.data

import org.springframework.data.domain.Sort
import kotlin.reflect.KProperty

const val ID = "id"
const val CTIME = "ctime"
const val MTIME = "mtime"

fun asc(vararg properties: String): Sort = Sort.by(*properties).ascending()
fun desc(vararg properties: String): Sort = Sort.by(*properties).descending()
fun asc(vararg properties: KProperty<*>): Sort = asc(*properties.map { asString(it) }.toTypedArray())
fun desc(vararg properties: KProperty<*>): Sort = desc(*properties.map { asString(it) }.toTypedArray())

package cn.sabercon.common.data.mongo

import cn.sabercon.common.data.MTIME
import cn.sabercon.common.data.asString
import cn.sabercon.common.util.now
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import kotlin.reflect.KProperty

fun update() = Update.update(MTIME, now())

fun <T> update(key: KProperty<T>, value: T) = update().set(key, value)

fun <T> Update.set(key: KProperty<T>, value: T) = set(asString(key), value)


infix fun <T> KProperty<T>.eq(value: T) = isEqualTo(value)
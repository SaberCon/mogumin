package cn.sabercon.common.data.mongo

import cn.sabercon.common.data.MTIME
import cn.sabercon.common.data.asString
import cn.sabercon.common.util.now
import org.springframework.data.mongodb.core.query.Update
import kotlin.reflect.KProperty

fun update(): Update = Update.update(MTIME, now())

fun <T> update(key: KProperty<T>, value: T): Update = update().set(key, value)

fun <T> Update.set(key: KProperty<T>, value: T): Update = set(asString(key), value)

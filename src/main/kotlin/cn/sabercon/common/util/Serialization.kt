package cn.sabercon.common.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

inline fun <reified T : Any> String.parse(): T {
    if (T::class.java.isEnum) {
        return T::class.java.enumConstants.first { (it as Enum<*>).name.equals(this, true) }
    }

    val obj: Any = when (T::class) {
        String::class -> this
        OffsetDateTime::class -> toDatetime()
        else -> toJsonObject<T>()
    }
    return obj as T
}

fun Any.format(): String {
    return when (this) {
        is String -> this
        is OffsetDateTime -> format(DateTimeFormatter.ISO_DATE_TIME)
        is Enum<*> -> name
        else -> toJson()
    }
}

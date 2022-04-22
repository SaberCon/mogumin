package cn.sabercon.common.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

inline fun <reified T : Any> String.parse(): T {
    if (T::class.java.isEnum) {
        return T::class.java.enumConstants.first { (it as Enum<*>).name.equals(this, true) }
    }

    val obj: Any = when (T::class) {
        String::class -> this
        LocalDateTime::class -> toDatetime()
        LocalDate::class -> toDate()
        LocalTime::class -> toTime()
        else -> toJsonObject<T>()
    }
    return obj as T
}

fun Any.format(): String {
    return when (this) {
        is String -> this
        is LocalDateTime -> format(DateTimeFormatter.ISO_DATE_TIME)
        is LocalDate -> format(DateTimeFormatter.ISO_DATE)
        is LocalTime -> format(DateTimeFormatter.ISO_TIME)
        is Enum<*> -> name
        else -> toJson()
    }
}

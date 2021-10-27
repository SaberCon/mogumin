package cn.sabercon.common.ext

import cn.sabercon.common.util.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

inline fun <reified T : Any> String.parse(): T {
    if (T::class.java.isEnum) {
        return T::class.java.enumConstants.first { (it as Enum<*>).name.equals(this, true) }
    }

    val obj: Any = when (T::class) {
        String::class -> this
        Boolean::class -> toBoolean()
        Int::class -> toInt()
        Long::class -> toLong()
        Float::class -> toFloat()
        Double::class -> toDouble()
        LocalDateTime::class -> toDatetime()
        LocalDate::class -> toDate()
        LocalTime::class -> toTime()
        else -> parseJson<T>()
    }
    return obj as T
}

fun Any.format(): String {
    return when (this) {
        is String, is Boolean, is Number -> toString()
        is LocalDateTime -> format(DATETIME_FMT)
        is LocalDate -> format(DATE_FMT)
        is LocalTime -> format(TIME_FMT)
        is Enum<*> -> name
        else -> toJson()
    }
}
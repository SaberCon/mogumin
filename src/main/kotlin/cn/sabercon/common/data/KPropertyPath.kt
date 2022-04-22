package cn.sabercon.common.data

import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/** copied from [org.springframework.data.mapping] */
class KPropertyPath<T, U>(val parent: KProperty<U?>, val child: KProperty1<U, T>) : KProperty<T> by child

fun asString(property: KProperty<*>): String {
    return when (property) {
        is KPropertyPath<*, *> -> "${asString(property.parent)}.${property.child.name}"
        else -> property.name
    }
}

operator fun <T, U> KProperty<T?>.div(other: KProperty1<T, U>): KProperty<U> = KPropertyPath(this, other)
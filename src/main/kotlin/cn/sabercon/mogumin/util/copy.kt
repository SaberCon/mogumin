package cn.sabercon.mogumin.util

import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> copyFrom(vararg pairs: Pair<String, Any?>): T {
    val constructor = T::class.primaryConstructor!!
    return pairs.toMap().filterKeys { constructor.parameters.any { p -> p.name!! == it } }
        .mapKeys { constructor.parameters.first { p -> p.name!! == it.key } }
        .let(constructor::callBy)
}
package cn.sabercon.common.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

/**
 * ### CAUTION:
 * Reflection is error-prone and ill-performance, so **DON'T** use these functions unless you have to
 */
object UnsafeReflection {
    fun <T : Any?> get(target: Any, propertyName: String): T {
        val property: KProperty1<*, T> = getProperty(target::class, propertyName)
        return property.getter.call(target)
    }

    fun <T : Any> modifyData(data: T, vararg properties: Pair<String, Any?>): T {
        require(data::class.isData) { "Only an instance of data class can be modified" }
        val copyMethod: KFunction<T> = getFunction(data::class, "copy")

        return properties.toMap()
            .mapKeys { copyMethod.findParameterByName(it.key)!! }
            .plus(copyMethod.instanceParameter!! to data)
            .let { copyMethod.callBy(it) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any, V : Any?> getProperty(kClass: KClass<T>, name: String): KProperty1<T, V> {
        return kClass.memberProperties.first { it.name == name } as KProperty1<T, V>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any, R : Any?> getFunction(kClass: KClass<T>, name: String): KFunction<R> {
        return kClass.memberFunctions.first { it.name == name } as KFunction<R>
    }
}

package cn.sabercon.common.data

import cn.sabercon.common.util.getQueryParam
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

const val ID = "id"
const val CTIME = "ctime"
const val MTIME = "mtime"
val DEFAULT_SORT = desc(ID)

fun asc(vararg properties: String) = properties.map { Sort.Order.asc(it) }.let { Sort.by(it) }
fun desc(vararg properties: String) = properties.map { Sort.Order.desc(it) }.let { Sort.by(it) }
fun asc(vararg properties: KProperty<*>) = properties.map { asString(it) }.toTypedArray().let { asc(*it) }
fun desc(vararg properties: KProperty<*>) = properties.map { asString(it) }.toTypedArray().let { desc(*it) }

suspend fun pageable(sort: Sort = DEFAULT_SORT) = PageRequest.of(
    (getQueryParam("p")?.toIntOrNull()?.takeIf { it > 0 } ?: 1) - 1,
    getQueryParam("s")?.toIntOrNull()?.takeIf { it > 0 } ?: 20,
    sort,
)

interface AssetRepository<T> : CoroutineCrudRepository<T, String> {

    suspend fun findByIdAndUserId(id: String, userId: Long): T?

    fun findByUserId(userId: Long, pageable: Pageable): Flow<T>

    suspend fun deleteByIdInAndUserId(ids: List<String>, userId: Long)
}


/** copied from [org.springframework.data.mapping] */
class KPropertyPath<T, U>(val parent: KProperty<U?>, val child: KProperty1<U, T>) : KProperty<T> by child

fun asString(property: KProperty<*>): String {
    return when (property) {
        is KPropertyPath<*, *> -> "${asString(property.parent)}.${property.child.name}"
        else -> property.name
    }
}

operator fun <T, U> KProperty<T?>.div(other: KProperty1<T, U>): KProperty<U> = KPropertyPath(this, other)
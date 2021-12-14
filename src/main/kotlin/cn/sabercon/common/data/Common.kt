package cn.sabercon.common.data

import cn.sabercon.common.Page
import cn.sabercon.common.ext.mapArray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

const val ID = "id"
const val CTIME = "ctime"
const val MTIME = "mtime"
val DEFAULT_SORT = desc(ID)

fun asc(vararg properties: String) = Sort.by(*properties).ascending()
fun desc(vararg properties: String) = Sort.by(*properties).descending()
fun asc(vararg properties: KProperty<*>) = asc(*properties.mapArray(::asString))
fun desc(vararg properties: KProperty<*>) = desc(*properties.mapArray(::asString))

@NoRepositoryBean
interface AssetRepository<T : Any> : CoroutineSortingRepository<T, String> {

    suspend fun findByUserIdAndId(userId: Long, id: String): T?

    fun findByUserId(userId: Long, sort: Sort): Flow<T>

    fun findByUserId(userId: Long, pageable: Pageable): Flow<T>

    suspend fun countByUserId(userId: Long): Long

    suspend fun findPageByUserId(userId: Long, pageable: Pageable) =
        Page(countByUserId(userId), findByUserId(userId, pageable).toList())

    suspend fun deleteByUserIdAndId(userId: Long, id: String)

    suspend fun deleteByUserIdAndIdIn(userId: Long, ids: Collection<String>)
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
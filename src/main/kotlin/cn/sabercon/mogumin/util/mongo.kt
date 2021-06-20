package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.base.PageModel
import cn.sabercon.mogumin.base.getQueryParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

const val ID = "id"

val DEFAULT_ASC_SORT = Sort.by(ID).ascending()
val DEFAULT_DESC_SORT = Sort.by(ID).descending()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.findOneOrNull(vararg criteria: Criteria) =
    findOne<T>(Query(Criteria().andOperator(*criteria))).awaitSingleOrNull()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.findOne(vararg criteria: Criteria) =
    findOneOrNull<T>(*criteria) ?: throw NoSuchElementException()

inline fun <reified T : Any> ReactiveMongoTemplate.findFlow(
    vararg criteria: Criteria,
    sort: Sort = DEFAULT_DESC_SORT,
): Flow<T> = find<T>(Query(Criteria().andOperator(*criteria)).with(sort)).asFlow()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.findPage(
    vararg criteria: Criteria,
    sort: Sort = DEFAULT_DESC_SORT,
    page: Int = 0,
    size: Int = 0,
): PageModel<T> {
    val pn = page.takeIf { it > 0 } ?: getQueryParam("pn")?.toIntOrNull()?.takeIf { it > 0 } ?: 1
    val ps = size.takeIf { it > 0 } ?: getQueryParam("ps")?.toIntOrNull()?.takeIf { it > 0 } ?: 20
    val query = Query(Criteria().andOperator(*criteria))
    return PageModel(
        total = count<T>(query).awaitSingle(),
        list = find<T>(query.with(PageRequest.of(pn - 1, ps, sort))).asFlow().toList()
    )
}

suspend inline fun <reified T : Any> ReactiveMongoTemplate.upsert(vararg criteria: Criteria, update: Update) =
    update<T>().matching(Criteria().andOperator(*criteria)).apply(update).upsertAndAwait()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.updateFirst(vararg criteria: Criteria, update: Update) =
    update<T>().matching(Criteria().andOperator(*criteria)).apply(update).firstAndAwait()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.updateMulti(vararg criteria: Criteria, update: Update) =
    update<T>().matching(Criteria().andOperator(*criteria)).apply(update).allAndAwait()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.remove(vararg criteria: Criteria) =
    remove<T>(Query(Criteria().andOperator(*criteria))).awaitSingle()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.exists(vararg criteria: Criteria) =
    exists<T>(Query(Criteria().andOperator(*criteria))).awaitSingle()

suspend fun <T : Any> ReactiveMongoTemplate.saveAndAwait(objectToSave: T) = save(objectToSave).awaitSingle()!!

suspend fun <T : Any> ReactiveMongoTemplate.insertAndAwait(objectToSave: T) = insert(objectToSave).awaitSingle()!!

suspend inline fun <reified T : Any> ReactiveMongoTemplate.find(id: String) = findById<T>(id).awaitSingle()

suspend inline fun <reified T : Any> ReactiveMongoTemplate.update(id: String, update: Update) =
    updateFirst<T>(Criteria.where(ID).isEqualTo(id), update = update)


// query
infix fun <T> KProperty<T>.eq(value: T) = this.isEqualTo(value)


// update
fun update(key: KProperty<*>, value: Any?) = Update.update(asString(key), value)

fun <T> Update.set(key: KProperty<*>, value: Any?) = set(asString(key), value)


// copy from org.springframework.data.mapping
class KPropertyPath<T, U>(
    val parent: KProperty<U?>,
    val child: KProperty1<U, T>
) : KProperty<T> by child

fun asString(property: KProperty<*>): String {
    return when (property) {
        is KPropertyPath<*, *> ->
            "${asString(property.parent)}.${property.child.name}"
        else -> property.name
    }
}

operator fun <T, U> KProperty<T?>.div(other: KProperty1<T, U>): KProperty<U> = KPropertyPath(this, other)
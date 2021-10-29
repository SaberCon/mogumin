package cn.sabercon.common.data.r2dbc

import cn.sabercon.common.data.MTIME
import cn.sabercon.common.data.asString
import cn.sabercon.common.util.ContextHolder
import cn.sabercon.common.util.now
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Update
import org.springframework.data.relational.core.query.isEqual
import org.springframework.data.relational.core.query.isIn
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import kotlin.reflect.KProperty

suspend fun <T : Any> tx(f: suspend (ReactiveTransaction) -> T?) =
    ContextHolder.getBean<TransactionalOperator>().executeAndAwait(f)


fun update() = Update.update(MTIME, now)

fun <T> Update.set(key: KProperty<T>, value: T) = set(asString(key), value)

fun <T> update(key: KProperty<T>, value: T) = update().set(key, value)


fun where(key: KProperty<*>) = Criteria.where(asString(key))

infix fun Criteria.and(key: KProperty<*>) = and(asString(key))

infix fun Criteria.CriteriaStep.eq(value: Any): Criteria = isEqual(value)


infix fun <T : Any> KProperty<T?>.eq(value: T) = where(this).isEqual(value)

infix fun <T : Any> KProperty<T?>.not(value: T) = where(this).not(value)

infix fun <T : Any> KProperty<T?>.isIn(value: Collection<T>) = where(this).isIn(value)

infix fun <T : Any> KProperty<T?>.notIn(value: Collection<T>) = where(this).notIn(value)

infix fun <T : Any> KProperty<T?>.between(value: Pair<T, T>) = where(this).between(value.first, value.second)

infix fun <T : Any> KProperty<T?>.notBetween(value: Pair<T, T>) = where(this).notBetween(value.first, value.second)

infix fun <T : Any> KProperty<T?>.lt(value: T) = where(this).lessThan(value)

infix fun <T : Any> KProperty<T?>.lte(value: T) = where(this).lessThanOrEquals(value)

infix fun <T : Any> KProperty<T?>.gt(value: T) = where(this).greaterThan(value)

infix fun <T : Any> KProperty<T>.gte(value: T) = where(this).greaterThanOrEquals(value)
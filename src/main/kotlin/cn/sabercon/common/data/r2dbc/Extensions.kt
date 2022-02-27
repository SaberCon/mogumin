package cn.sabercon.common.data.r2dbc

import cn.sabercon.common.data.MTIME
import cn.sabercon.common.data.asString
import cn.sabercon.common.util.ContextHolder
import cn.sabercon.common.util.now
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Update
import org.springframework.data.relational.core.query.isEqual
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import kotlin.reflect.KProperty

suspend fun <T : Any> tx(f: suspend (ReactiveTransaction) -> T?) =
    ContextHolder.getBean<TransactionalOperator>().executeAndAwait(f)


fun update() = Update.update(MTIME, now())

fun <T> update(key: KProperty<T>, value: T) = update().set(key, value)

fun <T> Update.set(key: KProperty<T>, value: T) = set(asString(key), value)


fun where(key: KProperty<*>) = Criteria.where(asString(key))

infix fun Criteria.and(key: KProperty<*>) = and(asString(key))

infix fun Criteria.CriteriaStep.eq(value: Any): Criteria = isEqual(value)
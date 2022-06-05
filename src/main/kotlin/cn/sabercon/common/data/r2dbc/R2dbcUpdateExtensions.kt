package cn.sabercon.common.data.r2dbc

import cn.sabercon.common.data.UPDATED_AT
import cn.sabercon.common.data.asString
import cn.sabercon.common.util.now
import org.springframework.data.r2dbc.core.ReactiveUpdateOperation.TerminatingUpdate
import org.springframework.data.r2dbc.core.ReactiveUpdateOperation.UpdateWithQuery
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import kotlin.reflect.KProperty

fun update(): Update = Update.update(UPDATED_AT, now())

fun <T> update(key: KProperty<T>, value: T): Update = update().set(key, value)

fun <T> Update.set(key: KProperty<T>, value: T): Update = set(asString(key), value)

fun UpdateWithQuery.matching(criteria: Criteria): TerminatingUpdate {
    return matching(Query.query(criteria))
}

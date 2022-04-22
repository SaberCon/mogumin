package cn.sabercon.common.data.r2dbc

import cn.sabercon.common.data.asString
import org.springframework.data.relational.core.query.Criteria
import kotlin.reflect.KProperty

fun where(key: KProperty<*>): Criteria.CriteriaStep = Criteria.where(asString(key))

infix fun Criteria.and(key: KProperty<*>): Criteria.CriteriaStep = and(asString(key))

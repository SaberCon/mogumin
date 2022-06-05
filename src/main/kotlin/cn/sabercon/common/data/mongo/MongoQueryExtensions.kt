package cn.sabercon.common.data.mongo

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

infix fun Criteria.sort(sort: Sort): Query = Query.query(this).with(sort)

infix fun Criteria.pageable(pageable: Pageable): Query = Query.query(this).with(pageable)

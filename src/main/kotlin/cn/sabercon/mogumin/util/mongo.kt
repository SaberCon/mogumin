package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.model.IdSequence
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where

@Configuration
class MongoSequenceIdConfig(private val mongoOps: ReactiveMongoTemplate) {

    suspend fun generateSequence(name: String) = mongoOps.findAndModify(
        query(where(IdSequence::id).isEqualTo(name)),
        Update().inc(IdSequence::seq.toString(), 1),
        options().upsert(true).returnNew(true),
        IdSequence::class.java
    ).awaitSingle()!!
}
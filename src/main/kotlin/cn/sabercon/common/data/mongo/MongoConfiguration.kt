package cn.sabercon.common.data.mongo

import cn.sabercon.common.data.CREATED_AT
import cn.sabercon.common.data.ID
import cn.sabercon.common.data.UPDATED_AT
import cn.sabercon.common.util.UnsafeReflection
import cn.sabercon.common.util.UnsafeReflection.modifyData
import cn.sabercon.common.util.now
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback
import reactor.kotlin.core.publisher.toMono
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Configuration
class MongoConfiguration {

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        val offsetDateTimeReadConverter = @ReadingConverter object : Converter<Date, OffsetDateTime> {
            override fun convert(source: Date) = source.toInstant().atOffset(ZoneOffset.UTC)
        }
        val offsetDateTimeWriteConverter = @WritingConverter object : Converter<OffsetDateTime, Date> {
            override fun convert(source: OffsetDateTime) = Date.from(source.toInstant())
        }

        return MongoCustomConversions(listOf(offsetDateTimeReadConverter, offsetDateTimeWriteConverter))
    }

    @Bean
    fun mongoBeforeConvertCallback() = ReactiveBeforeConvertCallback<Any> { entity, _ ->
        val id: String = UnsafeReflection.get(entity, ID)
        val now = now()

        when (id) {
            "" -> modifyData(entity, ID to ObjectId().toString(), CREATED_AT to now, UPDATED_AT to now).toMono()
            else -> modifyData(entity, UPDATED_AT to now).toMono()
        }
    }
}

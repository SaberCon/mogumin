package cn.sabercon.common.graphql

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType

object CustomScalars {

    val URI: GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("URI")
        .coercing(object : Coercing<String, String> {
            override fun serialize(dataFetcherResult: Any): String {
                if (dataFetcherResult !is String) throw CoercingSerializeException()
                return dataFetcherResult
            }

            override fun parseValue(input: Any): String {
                if (input !is String) throw CoercingParseValueException()
                return input
            }

            override fun parseLiteral(input: Any): String {
                if (input !is StringValue) throw CoercingParseLiteralException()
                return input.value
            }

        })
        .build()
}

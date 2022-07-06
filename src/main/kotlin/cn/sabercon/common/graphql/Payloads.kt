package cn.sabercon.common.graphql

data class DefaultPayload(val clientMutationId: String? = null)

data class CreatePayload<out T : Any>(
    val clientMutationId: String? = null,
    val subject: T,
)

data class UpdatePayload<out T : Any>(
    val clientMutationId: String? = null,
    val subject: T?,
)

package cn.sabercon.common.graphql

import cn.sabercon.common.Jwt
import cn.sabercon.common.data.USER_ID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.server.WebGraphQlInterceptor

@Configuration
class GraphQlConfiguration {

    @Bean
    fun loginGraphQlInterceptor(jwt: Jwt) = WebGraphQlInterceptor { request, chain ->
        val userId = jwt.decodeAuthorizationHeader(request.headers)
        request.configureExecutionInput { _, builder ->
            builder.graphQLContext { it.put(USER_ID, userId) }.build()
        }
        chain.next(request)
    }
}

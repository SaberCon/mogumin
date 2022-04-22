package cn.sabercon.megumin

import cn.sabercon.megumin.user.User
import cn.sabercon.megumin.user.UserRepo
import kotlinx.coroutines.reactor.asFlux
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono


@Component
class HeaderInterceptor : WebGraphQlInterceptor {
    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        val headerValue = request.headers["myHeader"]
        request.configureExecutionInput { executionInput, builder ->
            builder.graphQLContext(mapOf("aaa" to "bbb")).build()
        }
        return chain.next(request)
    }
}

@Controller
class GraphQLController constructor(val userRepo: UserRepo) {

    @QueryMapping
    fun getAllUsers(@ContextValue aaa: String): Flux<User> {
        println(aaa)
        return userRepo.findAll().asFlux()
    }

    @QueryMapping
    fun getAllPersons(): Flux<Person> {
        return Flux.just(Person(1, "a", 10), Person(2, "b", 20))
    }

    @BatchMapping(typeName = "Person")
    fun books(persons: List<Person>): Mono<Map<Person, List<Book>>> {
        return persons.associateWith { listOf(Book(it.id, "book")) }.toMono()
    }

    @SchemaMapping
    fun friends(person: Person): Flux<Person> {
        return Flux.just(person.copy(id = 100), person.copy(id = 200))
    }

    @SchemaMapping
    fun desc(book: Book): Mono<String> {
        return "desc".toMono()
    }
}

data class Person(
    val id: Long,
    val name: String,
    val age: Int,
)

data class Book(
    val personId: Long,
    val name: String,
)

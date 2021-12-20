package cn.sabercon.common.ext

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

suspend inline fun <reified T : Any> WebClient.get(uri: String, vararg uriVariables: Any): T =
    get().uri(uri, *uriVariables).retrieve().awaitBody()

suspend inline fun <reified T : Any> WebClient.post(uri: String, body: Any): T =
    post().uri(uri).bodyValue(body).retrieve().awaitBody()
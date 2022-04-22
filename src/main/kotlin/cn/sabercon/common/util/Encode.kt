package cn.sabercon.common.util

import com.google.common.hash.Hashing
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*

fun urlEncode(input: String): String = URLEncoder.encode(input, Charsets.UTF_8)

fun base64(input: ByteArray): String = Base64.getEncoder().encode(input).toString(Charsets.UTF_8)

fun base64(input: String): String = base64(input.toByteArray())

fun sha256(input: ByteArray): String =
    MessageDigest.getInstance("SHA-256").digest(input).let { base64(it) }

fun sha256(input: String): String = sha256(input.toByteArray())

fun hmacSha1(key: ByteArray, input: ByteArray): String =
    Hashing.hmacSha1(key).hashBytes(input).asBytes().let { base64(it) }

fun hmacSha1(key: String, input: String): String = hmacSha1(key.toByteArray(), input.toByteArray())

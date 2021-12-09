package cn.sabercon.common.util

import com.google.common.hash.Hashing
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*

fun urlEncode(input: String) = URLEncoder.encode(input, Charsets.UTF_8)!!

fun base64(input: ByteArray) = Base64.getEncoder().encode(input).toString(Charsets.UTF_8)

fun base64(input: String) = base64(input.toByteArray())

fun sha256(input: ByteArray) = MessageDigest.getInstance("SHA-256").digest(input).let { base64(it) }

fun sha256(input: String) = sha256(input.toByteArray())

fun hmacSha1(key: ByteArray, input: ByteArray) = Hashing.hmacSha1(key).hashBytes(input).asBytes().let { base64(it) }

fun hmacSha1(key: String, input: String) =
    Hashing.hmacSha1(key.toByteArray()).hashString(input, Charsets.UTF_8).asBytes().let { base64(it) }
package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.base.assertTrue
import com.google.common.hash.Hashing
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URLEncoder
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

@Component
@EnableConfigurationProperties(AliyunProperties::class)
class AliyunHelper(private val properties: AliyunProperties) {

    private val client = WebClient.builder().uriBuilderFactory(DefaultUriBuilderFactory().apply {
        encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
    }).build()

    private val timeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:SSS'Z'")!!
    private val timeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")!!

    fun buildOssData(): OssData {
        val now = ZonedDateTime.now(ZoneOffset.UTC)
        val expiration = now.plusMinutes(5)
        val dir = "mogumin/${now.year}/${now.monthValue}_${now.dayOfMonth}/"
        val policy = """{"expiration":"${expiration.format(timeFormatter1)}.","conditions":[["content-length-range",0,536870912],["starts-with","${'$'}key","$dir"]]}"""
            .let { Base64Utils.encodeToString(it.encodeToByteArray()) }
        // todo: need to check if the sign is correct
        return OssData(
            accessId = properties.accessKeyId,
            host = properties.oss.host,
            dir = dir,
            policy = policy,
            signature = sign(policy),
            expire = expiration.toEpochSecond(),
            )
    }

    suspend fun sendSmsCode(phone: String): String {
        val code = Random.nextInt(10000, 20000).toString().drop(1)
        val params = mapOf(
            "AccessKeyId" to properties.accessKeyId,
            "SignatureMethod" to "HMAC-SHA1",
            "SignatureNonce" to UUID.randomUUID().toString(),
            "SignatureVersion" to "1.0",
            "Timestamp" to ZonedDateTime.now(ZoneOffset.UTC).format(timeFormatter2),
            "Format" to "json",
            "Action" to "SendSms",
            "Version" to "2017-05-25",
            "SignName" to properties.sms.signName,
            "TemplateCode" to properties.sms.templateCode,
            "TemplateParam" to """{"code":"$code"}""",
            "PhoneNumbers" to phone,
        ).let(::TreeMap)
        val query = params.entries.joinToString("&") { "${specialUrlEncode(it.key)}=${specialUrlEncode(it.value)}" }
        val signature = specialUrlEncode(sign("GET&${specialUrlEncode("/")}&${specialUrlEncode(query)}"))
        val url = "https://dysmsapi.aliyuncs.com?Signature=$signature&$query"
        client.get().uri(url).retrieve().awaitBody<String>()
            .let { assertTrue(it.contains(""""Code":"OK"""")) { "error when sending sms code, message: $it" } }
        return code
    }

    private fun specialUrlEncode(value: String) = URLEncoder.encode(value, Charsets.UTF_8)
        .replace("+", "%20").replace("*", "%2A").replace("%7E", "~")

    private fun sign(input: String) = Hashing.hmacSha1("${properties.accessKeySecret}&".encodeToByteArray())
        .hashString(input, Charsets.UTF_8).asBytes().let(Base64Utils::encodeToString)
}

@ConstructorBinding
@ConfigurationProperties("aliyun")
data class AliyunProperties(
    val accessKeyId: String,
    val accessKeySecret: String,
    val sms: SmsProperties,
    val oss: OssProperties,
) {
    data class SmsProperties(val signName: String, val templateCode: String)
    data class OssProperties(val endpoint: String, val bucket: String, val host: String)
}

data class OssData(
    val accessId: String,
    val host: String,
    val dir: String,
    val policy: String,
    val signature: String,
    val expire: Long,
)
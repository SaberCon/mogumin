package cn.sabercon.common.util

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig

fun main() {
    println("please input the secret key:")
    val key = readLine()!!
    println("please input the message:")
    val message = readLine()!!
    println("encrypted message: ${getEncryptor(key).encrypt(message)}")
}

private fun getEncryptor(key: String): PooledPBEStringEncryptor {
    val config = SimpleStringPBEConfig().apply {
        password = key
        algorithm = "PBEWITHHMACSHA512ANDAES_256"
        keyObtentionIterations = 1000
        poolSize = 1
        stringOutputType = "base64"
        setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
        setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator")
    }
    return PooledPBEStringEncryptor().apply { setConfig(config) }
}
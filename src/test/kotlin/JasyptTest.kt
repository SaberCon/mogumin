import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.junit.jupiter.api.Test

class JasyptTest {

    @Test
    fun `jasypt encrypt`() {
        val key = "key"
        val message = "message"
        println("encrypted message: ${getEncryptor(key).encrypt(message)}")
    }

    @Test
    fun `jasypt decrypt`() {
        val key = "key"
        val message = "raXP0IxWmdkTpiSIx+r8aDEJ7QHzKDZERKjKdfuSjWiVDrqD66pxEUgyXLwSbF20"
        println("decrypted message: ${getEncryptor(key).decrypt(message)}")
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
}
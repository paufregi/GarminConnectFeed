package paufregi.connectfeed

import java.io.InputStream
import java.io.OutputStream
import java.security.Key
import java.security.KeyStore
import java.security.KeyStoreSpi
import java.security.Provider
import java.security.SecureRandom
import java.security.Security
import java.security.cert.Certificate
import java.security.spec.AlgorithmParameterSpec
import java.util.Collections
import java.util.Date
import java.util.Enumeration
import java.util.concurrent.ConcurrentHashMap
import javax.crypto.KeyGeneratorSpi
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Robolectric does not ship the `AndroidKeyStore` security provider, which
 * [paufregi.connectfeed.data.utils.Crypto] depends on. This registers an in-memory
 * replacement so that encrypted DataStores can be exercised in JVM unit tests.
 *
 * Call [install] before `Crypto` is first touched (e.g. in a `@Before`).
 */
object FakeAndroidKeyStore {

    private const val NAME = "AndroidKeyStore"

    internal val keys = ConcurrentHashMap<String, SecretKey>()

    private val provider = object : Provider(NAME, 1.0, "In-memory AndroidKeyStore for unit tests") {
        init {
            put("KeyStore.$NAME", FakeKeyStoreSpi::class.java.name)
            put("KeyGenerator.AES", FakeAesKeyGenerator::class.java.name)
        }
    }

    fun install() {
        if (Security.getProvider(NAME) == null) {
            Security.insertProviderAt(provider, 1)
        }
    }

    fun reset() = keys.clear()
}

class FakeKeyStoreSpi : KeyStoreSpi() {
    private val keys get() = FakeAndroidKeyStore.keys

    override fun engineGetEntry(alias: String, protParam: KeyStore.ProtectionParameter?): KeyStore.Entry? =
        keys[alias]?.let { KeyStore.SecretKeyEntry(it) }
    override fun engineGetKey(alias: String, password: CharArray?): Key? = keys[alias]
    override fun engineGetCertificateChain(alias: String): Array<Certificate>? = null
    override fun engineGetCertificate(alias: String): Certificate? = null
    override fun engineGetCreationDate(alias: String): Date? = if (keys.containsKey(alias)) Date() else null
    override fun engineSetKeyEntry(alias: String, key: Key, password: CharArray?, chain: Array<Certificate>?) {
        keys[alias] = key as SecretKey
    }
    override fun engineSetKeyEntry(alias: String, key: ByteArray, chain: Array<Certificate>?) {
        keys[alias] = SecretKeySpec(key, "AES")
    }
    override fun engineSetCertificateEntry(alias: String, cert: Certificate) = Unit
    override fun engineDeleteEntry(alias: String) { keys.remove(alias) }
    override fun engineAliases(): Enumeration<String> = Collections.enumeration(keys.keys)
    override fun engineContainsAlias(alias: String): Boolean = keys.containsKey(alias)
    override fun engineSize(): Int = keys.size
    override fun engineIsKeyEntry(alias: String): Boolean = keys.containsKey(alias)
    override fun engineIsCertificateEntry(alias: String): Boolean = false
    override fun engineGetCertificateAlias(cert: Certificate): String? = null
    override fun engineStore(stream: OutputStream?, password: CharArray?) = Unit
    override fun engineLoad(stream: InputStream?, password: CharArray?) = Unit
}

class FakeAesKeyGenerator : KeyGeneratorSpi() {
    private var alias: String = "default"
    private var keySize: Int = 256
    private var random: SecureRandom = SecureRandom()

    override fun engineInit(random: SecureRandom?) {
        random?.let { this.random = it }
    }

    override fun engineInit(params: AlgorithmParameterSpec, random: SecureRandom?) {
        random?.let { this.random = it }
        // Resolve reflectively so this works regardless of which classloader loaded KeyGenParameterSpec
        alias = runCatching {
            params.javaClass.getMethod("getKeystoreAlias").invoke(params) as String
        }.getOrDefault("default")
        keySize = runCatching {
            (params.javaClass.getMethod("getKeySize").invoke(params) as Int).takeIf { it > 0 } ?: 256
        }.getOrDefault(256)
    }

    override fun engineInit(keysize: Int, random: SecureRandom?) {
        random?.let { this.random = it }
        keySize = keysize
    }

    override fun engineGenerateKey(): SecretKey {
        val bytes = ByteArray(keySize / 8).also { random.nextBytes(it) }
        return SecretKeySpec(bytes, "AES").also { FakeAndroidKeyStore.keys[alias] = it }
    }
}





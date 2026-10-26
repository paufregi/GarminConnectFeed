package paufregi.connectfeed

import okhttp3.tls.HeldCertificate
import java.io.File
import java.security.KeyStore

/**
 * On a device the MockWebServer certificate is trusted through `network_security_config`
 * (`@raw/cert` debug override). Under Robolectric the platform trust store is used instead,
 * so point the JVM-wide trust store at a PKCS12 file containing the test certificate.
 *
 * Must be called before any OkHttp client is built (i.e. before `hiltRule.inject()`).
 */
object TestTrustStore {

    private const val PASSWORD = "connectfeed"

    @Volatile
    private var installed = false

    fun install() {
        if (installed) return
        synchronized(this) {
            if (installed) return

            val pem = Thread.currentThread().contextClassLoader
                ?.getResourceAsStream("server.pem")
                ?.bufferedReader()?.use { it.readText() }
                ?: throw IllegalArgumentException("Resource not found: server.pem")
            val certificate = HeldCertificate.decode(pem).certificate

            val keyStore = KeyStore.getInstance("PKCS12").apply {
                load(null, null)
                setCertificateEntry("mockwebserver", certificate)
            }

            val file = File.createTempFile("test-truststore", ".p12").apply { deleteOnExit() }
            file.outputStream().use { keyStore.store(it, PASSWORD.toCharArray()) }

            System.setProperty("javax.net.ssl.trustStore", file.absolutePath)
            System.setProperty("javax.net.ssl.trustStorePassword", PASSWORD)
            System.setProperty("javax.net.ssl.trustStoreType", "PKCS12")

            installed = true
        }
    }
}


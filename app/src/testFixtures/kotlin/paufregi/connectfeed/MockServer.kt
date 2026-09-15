package paufregi.connectfeed

import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.Headers.Companion.toHeaders
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.rules.ExternalResource
import java.io.IOException
import javax.net.ssl.SSLSocketFactory

class MockServer(
    val port: Int = 0,
    val sslSocketFactory: SSLSocketFactory? = null,
    val dispatcher: Dispatcher? = null
) : ExternalResource() {
    val server: MockWebServer = MockWebServer()

    fun enqueue(
        code: Int = 200,
        headers: Map<String, String> = emptyMap(),
        body: String = ""
    ) {
        val mockResponse = MockResponse(code, headers.toHeaders(), body)
        server.enqueue(mockResponse)
    }

    fun url(path: String) = server.url(path)

    fun takeRequest() = server.takeRequest()

    override fun before() {
        try {
            sslSocketFactory?.let { server.useHttps(it) }
            server.start(port)
            dispatcher?.let { server.dispatcher = it }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun after() {
        server.close()
    }

    companion object {
        fun createSecure(port: Int, dispatcher: Dispatcher) =
            MockServer(port, createSSLSocketFactory(), dispatcher)

        private fun createSSLSocketFactory(): SSLSocketFactory =
            requireNotNull(MockServer::class.java.classLoader?.getResourceAsStream("server.pem")) {
                "Resource not found: server.pem"
            }.bufferedReader().use { reader ->
                HandshakeCertificates.Builder()
                    .heldCertificate(HeldCertificate.decode(reader.readText()))
                    .build()
                    .sslSocketFactory()
            }
    }
}
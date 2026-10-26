package paufregi.connectfeed

import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.Headers.Companion.toHeaders
import org.junit.rules.ExternalResource
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.ssl.SSLSocketFactory

class MockWebServerRule(
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
            // Bind on the wildcard address so "localhost" works whether it resolves to 127.0.0.1 or ::1
            server.start(InetSocketAddress(port).address, port)
            dispatcher?.let { server.dispatcher = it }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun after() {
        server.close()
    }
}
package paufregi.connectfeed

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.Headers.Companion.toHeaders
import org.junit.rules.ExternalResource
import java.io.IOException

class MockWebServerRule(
    val port: Int = 0,
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
            server.start(port)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun after() {
        server.close()
    }
}
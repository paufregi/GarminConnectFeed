package paufregi.connectfeed

import mockwebserver3.Dispatcher
import mockwebserver3.MockWebServer
import org.junit.rules.ExternalResource
import java.io.IOException
import javax.net.ssl.SSLSocketFactory

class MockWebServerRule(
    val port: Int = 0,
    val sslSocketFactory: SSLSocketFactory? = null,
    val dispatcher: Dispatcher? = null
) : ExternalResource() {
    val server: MockWebServer = MockWebServer()

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
}
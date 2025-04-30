package paufregi.connectfeed.data.api.utils

import com.google.common.truth.Truth.assertThat
import okhttp3.Request
import org.junit.Test

class HttpTest {

    val request = Request.Builder().url("http://test/").build()

    @Test
    fun `Auth request`() {
        val result = authRequest(request, "token")

        assertThat(result.url.toString()).isEqualTo("http://test/")
        assertThat(result.header("Authorization")).isEqualTo("Bearer token")
    }

    @Test
    fun `Failed auth request`() {
        val result = failedAuthResponse(request, "reason")

        assertThat(result.code).isEqualTo(401)
        assertThat(result.message).isEqualTo("Auth failed")
        assertThat(result.body?.string()).isEqualTo("reason")
        assertThat(result.request).isEqualTo(request)
    }
}
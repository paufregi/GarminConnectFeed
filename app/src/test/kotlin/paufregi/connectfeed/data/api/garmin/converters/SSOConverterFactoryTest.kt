package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Retrofit

class SSOConverterFactoryTest {

    val factory = SSOConverterFactory()

    @Test
    fun `CSRF string converter`() {
        val result = factory
            .stringConverter(CSRF::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(CSRFConverter.StringConverter::class.java)
    }

    @Test
    fun `Ticket string converter`() {
        val result = factory
            .stringConverter(Ticket::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(TicketConverter.StringConverter::class.java)
    }

    @Test
    fun `Unsupported string converter`() {
        val result = factory
            .stringConverter(String::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isNull()
    }

    @Test
    fun `CSRF responseBody converter`() {
        val result = factory
            .responseBodyConverter(CSRF::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(CSRFConverter.ResponseBodyConverter::class.java)
    }

    @Test
    fun `Ticket responseBody converter`() {
        val result = factory
            .responseBodyConverter(Ticket::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(TicketConverter.ResponseBodyConverter::class.java)
    }

    @Test
    fun `Unsupported responseBody converter`() {
        val result = factory
            .responseBodyConverter(Ticket::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isNull()
    }
}
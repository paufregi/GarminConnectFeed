package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk

import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.OAuth1
import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Retrofit

class GarminConverterFactoryTest {

    @Test
    fun `CSRF string converter`() {
        val result = GarminConverterFactory()
            .stringConverter(CSRF::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(CSRFStringConverter::class.java)
    }

    @Test
    fun `Ticket string converter`() {
        val result = GarminConverterFactory()
            .stringConverter(Ticket::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(TicketStringConverter::class.java)
    }

    @Test
    fun `Unsupported string converter`() {
        val result = GarminConverterFactory()
            .stringConverter(String::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isNull()
    }

    @Test
    fun `CSRF extractor responseBody converter`() {
        val result = GarminConverterFactory()
            .responseBodyConverter(CSRF::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(CSRFExtractor::class.java)
    }

    @Test
    fun `Ticket extractor responseBody converter`() {
        val result = GarminConverterFactory()
            .responseBodyConverter(Ticket::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(TicketExtractor::class.java)
    }

    @Test
    fun `Oauth1 extractor responseBody converter`() {
        val result = GarminConverterFactory()
            .responseBodyConverter(OAuth1::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isInstanceOf(Oauth1Extractor::class.java)
    }

    @Test
    fun `Unsupported responseBody converter`() {
        val result = GarminConverterFactory()
            .responseBodyConverter(String::class.java, arrayOf(), mockk<Retrofit>() )

        assertThat(result).isNull()
    }
}
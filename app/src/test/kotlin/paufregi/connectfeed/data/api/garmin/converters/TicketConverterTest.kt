package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.htmlTicket

class TicketConverterTest {

    private val mediaType = "text/html; charset=UTF-8"

    @Test
    fun `ResponseBody to Ticket`() {
        val responseBody = htmlTicket.toResponseBody(mediaType.toMediaType())
        val result = TicketConverter.ResponseBodyConverter.convert(responseBody)

        assertThat(result).isEqualTo(Ticket("TEST_TICKET_VALUE"))
    }

    @Test
    fun `ResponseBody to Ticket - no Ticket`() {
        val responseBody = "".toResponseBody(mediaType.toMediaType())
        val result = TicketConverter.ResponseBodyConverter.convert(responseBody)

        assertThat(result).isEqualTo(Ticket(""))
    }

    @Test
    fun `Ticket to string`() {
        val ticket = Ticket("TEST_TICKET_VALUE")
        val result = TicketConverter.StringConverter.convert(ticket)
        
        assertThat(result).isEqualTo("TEST_TICKET_VALUE")
    }
}
package paufregi.connectfeed.data.api.garmin.converters

import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Converter

class TicketStringConverter : Converter<Ticket, String> {
    override fun convert(value: Ticket): String {
        return value.value
    }
}
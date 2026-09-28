package paufregi.connectfeed.data.api.garmin.converters

import okhttp3.ResponseBody
import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Converter

object TicketConverter {
    private val regex = Regex("""\?ticket=([^"]+)""")

    object ResponseBodyConverter : Converter<ResponseBody, Ticket> {
        override fun convert(value: ResponseBody): Ticket {
            return Ticket(regex.find(value.string())?.groupValues?.getOrNull(1).orEmpty())
        }
    }

    object StringConverter : Converter<Ticket, String> {
        override fun convert(value: Ticket): String {
            return value.value
        }
    }
}

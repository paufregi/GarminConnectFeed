package paufregi.connectfeed.data.api.garmin.converters

import okhttp3.ResponseBody
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class SSOConverterFactory : Converter.Factory() {

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        return when (type) {
            CSRF::class.java -> CSRFConverter.StringConverter
            Ticket::class.java -> TicketConverter.StringConverter
            else -> null
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation?>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            CSRF::class.java -> CSRFConverter.ResponseBodyConverter
            Ticket::class.java -> TicketConverter.ResponseBodyConverter
            else -> null
        }
    }
}
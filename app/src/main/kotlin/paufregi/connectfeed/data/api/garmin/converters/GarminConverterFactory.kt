package paufregi.connectfeed.data.api.garmin.converters

import okhttp3.ResponseBody
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.time.Instant

class GarminConverterFactory : Converter.Factory() {

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        return when (type) {
            CSRF::class.java -> CSRFStringConverter()
            Ticket::class.java -> TicketStringConverter()
            Instant::class.java -> InstantStringConverter()
            else -> null
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation?>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            CSRF::class.java -> CSRFExtractor()
            Ticket::class.java -> TicketExtractor()
            PreAuthToken::class.java -> PreAuthTokenExtractor()
            else -> null
        }
    }
}
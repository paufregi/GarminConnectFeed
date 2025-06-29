package paufregi.connectfeed.data.api.garmin.converters

import paufregi.connectfeed.core.utils.Formatter
import retrofit2.Converter
import java.time.Instant
import java.time.ZoneId

class InstantStringConverter : Converter<Instant, String> {
    override fun convert(value: Instant): String {
        return Formatter.date(ZoneId.systemDefault()).format(value)
    }
}
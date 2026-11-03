package paufregi.connectfeed.data.api.garmin.converters

import okhttp3.ResponseBody
import paufregi.connectfeed.data.api.garmin.models.CSRF
import retrofit2.Converter

object CSRFConverter {
    private val regex = Regex("""name="_csrf"\s+value="([^"]+)"""")


    object ResponseBodyConverter : Converter<ResponseBody, CSRF> {
        override fun convert(value: ResponseBody): CSRF {
            return CSRF(regex.find(value.string())?.groupValues?.getOrNull(1).orEmpty())
        }
    }

    object StringConverter : Converter<CSRF, String> {
        override fun convert(value: CSRF): String = value.value
    }
}


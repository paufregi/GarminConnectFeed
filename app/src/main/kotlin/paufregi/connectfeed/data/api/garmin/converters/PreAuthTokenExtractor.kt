package paufregi.connectfeed.data.api.garmin.converters

import okhttp3.ResponseBody
import paufregi.connectfeed.data.api.garmin.models.PreAuthTokenDto
import retrofit2.Converter

class PreAuthTokenExtractor : Converter<ResponseBody, PreAuthTokenDto> {
    override fun convert(value: ResponseBody): PreAuthTokenDto {
        val queryMap = value.string().split('&').associate {
            val parts = it.split('=')
            val k = parts.firstOrNull()
            val v = parts.drop(1).firstOrNull()
            Pair(k, v)
        }

        return PreAuthTokenDto(
            queryMap["oauth_token"].orEmpty(),
            queryMap["oauth_token_secret"].orEmpty()
        )
    }
}
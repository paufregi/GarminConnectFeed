package paufregi.connectfeed.data.api.garmin

import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import paufregi.connectfeed.data.api.garmin.converters.SSOConverterFactory
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface GarminSSO {

    @GET("/sso/signin")
    suspend fun getCSRF(
        @HeaderMap headerMap: Map<String, String> = headerCSRF,
        @QueryMap queryMap: Map<String, String> = query,
    ): Response<CSRF>

    @FormUrlEncoded
    @POST("/sso/signin")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("_csrf") csrf: CSRF,
        @Field("embed") embed: Boolean = true,
        @HeaderMap headerMap: Map<String, String> = headerLogin,
        @QueryMap queryMap: Map<String, String> = query,
    ): Response<Ticket>

    companion object {
        const val BASE_URL = "https://sso.garmin.com"

        val query = mapOf(
            "id" to "gauth-widget",
            "embedWidget" to "true",
            "gauthHost" to "https://sso.garmin.com/sso/embed",
            "service" to "https://sso.garmin.com/sso/embed",
            "source" to "https://sso.garmin.com/sso/embed",
            "redirectAfterAccountLoginUrl" to "https://sso.garmin.com/sso/embed",
            "redirectAfterAccountCreationUrl" to "https://sso.garmin.com/sso/embed",
        )

        val loginReferer = BASE_URL.toHttpUrl().newBuilder()
            .addPathSegment("sso")
            .addPathSegment("signin")
            .apply {
                query.forEach { (key, value) -> addQueryParameter(key, value) }
            }
            .build()
            .toString()

        val headerCSRF = mapOf(
            "accept" to "text/html",
            "Referer" to "https://sso.garmin.com/sso/embed"
        )

        val headerLogin = mapOf(
            "accept" to "text/html",
            "Referer" to loginReferer
        )

        private val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        fun client(url: String): GarminSSO =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(SSOConverterFactory())
                .build()
                .create(GarminSSO::class.java)
    }
}

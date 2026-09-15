package paufregi.connectfeed.data.api.garmin

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import paufregi.connectfeed.data.api.garmin.models.LoginRequest
import paufregi.connectfeed.data.api.garmin.models.LoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface GarminSSO {

    @POST("/mobile/api/login")
    suspend fun login(
        @Body request: LoginRequest,
        @HeaderMap headerMap: Map<String, String> = header,
        @QueryMap queryMap: Map<String, String> = query,
    ): Response<LoginResponse>

    companion object {
        const val BASE_URL = "https://sso.garmin.com"

        val header = mapOf(
            "User-Agent" to "Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36",
            "Accept" to "application/json, text/plain, */*",
            "Content-Type" to "application/json",
            "Origin" to "https://sso.garmin.com",
        )

        val query = mapOf(
            "clientId" to "GCM_ANDROID_DARK",
            "locale" to "en-US",
            "service" to "https://mobile.integration.garmin.com/gcm/android"
        )

        private val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        fun client(url: String): GarminSSO =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(GarminSSO::class.java)
    }
}

package paufregi.connectfeed.data.api.garmin

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import java.util.Base64

interface GarminAuth {

    @FormUrlEncoded
    @POST("/di-oauth2-service/oauth/token")
    suspend fun exchange(

        @Header("Authorization") authorization: String,
        @Field("client_id") clientId: String,
        @Field("service_ticket") serviceTicket: String,
        @HeaderMap headerMap: Map<String, String> = nativeHeader,
        @Field("grant_type") grantType: String = GRANT_TYPE_EXCHANGE,
        @Field("service_url") serviceUrl: String = SERVICE_URL,
    ): Response<AuthToken>

    @FormUrlEncoded
    @POST("/di-oauth2-service/oauth/token")
    suspend fun refresh(
        @Header("Authorization") authorization: String,
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String,
        @HeaderMap headerMap: Map<String, String> = nativeHeader,
        @Field("grant_type") grantType: String = GRANT_TYPE_REFRESH,
    ): Response<AuthToken>

    companion object {
        const val BASE_URL = "https://diauth.garmin.com"

        const val GRANT_TYPE_EXCHANGE = "https://connectapi.garmin.com/di-oauth2-service/oauth/grant/service_ticket"
        const val GRANT_TYPE_REFRESH = "refresh_token"
        const val SERVICE_URL = "https://mobile.integration.garmin.com/gcm/android"

        private val nativeHeader =  mapOf(
            "User-Agent" to "GCM-Android-5.23",
            "X-Garmin-User-Agent" to "com.garmin.android.apps.connectmobile/5.23; ; Google/sdk_gphone64_arm64/google; Android/33; Dalvik/2.1.0",
            "X-Garmin-Paired-App-Version" to "10861",
            "X-Garmin-Client-Platform" to "Android",
            "X-App-Ver" to "10861",
            "X-Lang" to "en",
            "X-GCExperience" to "GC5",
            "Accept-Language" to "en-US,en;q=0.9",
            "Accept" to "application/json",
            "Content-Type" to "application/x-www-form-urlencoded",
            "Cache-Control" to "no-cache",
        )

        private val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        fun buildBasicAuth(clientId: String): String {
            val encoded = Base64.getEncoder().encodeToString("$clientId:".toByteArray())
            return "Basic $encoded"
        }

        fun client(url: String): GarminAuth =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(GarminAuth::class.java)
    }
}

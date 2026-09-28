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
        @Field("grant_type") grantType: String = GRANT_TYPE_EXCHANGE,
        @Field("service_url") serviceUrl: String = SERVICE_URL,
        @HeaderMap headerMap: Map<String, String> = nativeHeader,
    ): Response<AuthToken>

    @FormUrlEncoded
    @POST("/di-oauth2-service/oauth/token")
    suspend fun refresh(
        @Header("Authorization") authorization: String,
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = GRANT_TYPE_REFRESH,
        @Field("service_url") serviceUrl: String = SERVICE_URL,
        @HeaderMap headerMap: Map<String, String> = nativeHeader,
    ): Response<AuthToken>

    companion object {
        const val BASE_URL = "https://diauth.garmin.com"

        const val GRANT_TYPE_EXCHANGE = "https://connectapi.garmin.com/di-oauth2-service/oauth/grant/service_ticket"
        const val GRANT_TYPE_REFRESH = "refresh_token"
        const val SERVICE_URL = "https://sso.garmin.com/sso/embed"

        private val nativeHeader =  mapOf(
            "Accept" to "application/json,text/html;q=0.9,*/*;q=0.8",
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

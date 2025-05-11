package paufregi.connectfeed.data.api.strava

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import paufregi.connectfeed.data.api.strava.models.AuthToken
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface StravaAuth {

    @FormUrlEncoded
    @POST("api/v3/oauth/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun exchange(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): Response<AuthToken>

    @FormUrlEncoded
    @POST("api/v3/oauth/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun refreshAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token"
    ): Response<AuthToken>

    @FormUrlEncoded
    @POST("oauth/deauthorize")
    suspend fun deauthorization(
        @Field("access_token") accessToken: String
    ): Response<Unit>

    companion object {
        const val BASE_URL = "https://www.strava.com"

        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        fun client(url: String): StravaAuth {
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(StravaAuth::class.java)
        }
    }
}
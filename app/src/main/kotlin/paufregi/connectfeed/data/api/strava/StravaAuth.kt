package paufregi.connectfeed.data.api.strava

import paufregi.connectfeed.data.api.garmin.converters.GarminConverterFactory
import paufregi.connectfeed.data.api.strava.models.Token
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface StravaAuth {

    @FormUrlEncoded
    @POST("/oauth/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun exchange(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "activity:write,read"
    ): Response<Token>

    @FormUrlEncoded
    @POST("/oauth/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun refreshAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") code: String,
        @Field("grant_type") grantType: String = "activity:write,read"
    ): Response<Token>

    @FormUrlEncoded
    @POST("/oauth/deauthorize")
    suspend fun deauthorization(): Response<Unit>

    companion object {
        const val BASE_URL = "https://www.strava.com"

        fun client(url: String): StravaAuth  {
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GarminConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StravaAuth::class.java)
        }
    }
}
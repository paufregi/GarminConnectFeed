package paufregi.connectfeed.data.api.garmin

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor

interface GarminAuth {

    @POST("/oauth-service/oauth/exchange/user/2.0")
    @Headers(
        "User-Agent: com.garmin.android.apps.connectmobile",
        "Content-Type: application/x-www-form-urlencoded"
    )
    suspend fun exchange(): Response<AuthToken>

    @FormUrlEncoded
    @POST("/oauth-service/oauth/exchange/user/2.0")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun refresh(
        @Field("refresh_token") refreshToken: String,
    ): Response<AuthToken>

    companion object {
        const val BASE_URL = "https://connectapi.garmin.com"

        fun client(consumerKey: String, consumerSecret: String, token: PreAuthToken, url: String): GarminAuth {
            val consumer = OkHttpOAuthConsumer(consumerKey, consumerSecret)
            consumer.setTokenWithSecret(token.token, token.secret)

            val client = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))

            val json = Json {
                explicitNulls = false
                ignoreUnknownKeys = true
            }

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .client(client.build())
                .build()
                .create(GarminAuth::class.java)
        }
    }
}

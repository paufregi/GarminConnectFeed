package paufregi.connectfeed.data.api

import okhttp3.OkHttpClient
import paufregi.connectfeed.data.api.converters.GarminConverterFactory
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuthConsumer
import paufregi.connectfeed.data.api.models.Ticket
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor


interface GarminAuth1 {

    @GET("/oauth-service/oauth/preauthorized")
    @Headers(
        "User-Agent: com.garmin.android.apps.connectmobile"
    )
    suspend fun getOauth1(
        @Query("ticket") ticket: Ticket,
        @Query("login-url") loginUrl: String = "https://sso.garmin.com/sso/embed"
    ): Response<OAuth1>

    companion object {
        const val BASE_URL = "https://connectapi.garmin.com"

        fun client(oauthConsumer: OAuthConsumer, url: String): GarminAuth1 {
            val consumer = OkHttpOAuthConsumer(oauthConsumer.key, oauthConsumer.secret)

            val client = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GarminConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
                .create(GarminAuth1::class.java)
        }
    }
}

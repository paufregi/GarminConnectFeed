package paufregi.connectfeed.data.api.garmin

import okhttp3.OkHttpClient
import paufregi.connectfeed.data.api.garmin.converters.GarminConverterFactory
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import paufregi.connectfeed.data.api.garmin.models.Ticket
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor

interface GarminPreAuth {

    @GET("/oauth-service/oauth/preauthorized")
    @Headers(
        "User-Agent: com.garmin.android.apps.connectmobile"
    )
    suspend fun preauthorize(
        @Query("ticket") ticket: Ticket,
        @Query("login-url") loginUrl: String = "https://sso.garmin.com/sso/embed"
    ): Response<PreAuthToken>

    companion object {
        const val BASE_URL = "https://connectapi.garmin.com"

        fun client(consumerKey: String, consumerSecret: String, url: String): GarminPreAuth {
            val consumer = OkHttpOAuthConsumer(consumerKey, consumerSecret)

            val client = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GarminConverterFactory())
                .client(client.build())
                .build()
                .create(GarminPreAuth::class.java)
        }
    }
}

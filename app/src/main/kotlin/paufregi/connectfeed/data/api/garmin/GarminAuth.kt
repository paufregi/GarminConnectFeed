package paufregi.connectfeed.data.api.garmin

import okhttp3.OkHttpClient
import paufregi.connectfeed.data.api.garmin.converters.GarminConverterFactory
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import paufregi.connectfeed.data.api.garmin.models.Consumer
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    companion object {
        const val BASE_URL = "https://connectapi.garmin.com"

        fun client(consumer: Consumer, token: PreAuthToken, url: String): GarminAuth {
            val oAuthConsumer = OkHttpOAuthConsumer(consumer.key, consumer.secret)
            oAuthConsumer.setTokenWithSecret(token.token, token.secret)

            val client = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(oAuthConsumer))

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GarminConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
                .create(GarminAuth::class.java)
        }
    }
}

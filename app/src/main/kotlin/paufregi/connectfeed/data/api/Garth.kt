package paufregi.connectfeed.data.api

import paufregi.connectfeed.data.api.models.OAuthConsumer
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface Garth {
    @GET("/oauth_consumer.json")
    suspend fun getOAuthConsumer(): Response<OAuthConsumer>

    companion object {
        const val BASE_URL = "https://thegarth.s3.amazonaws.com"

        fun client(url: String): Garth  {
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Garth::class.java)
        }
    }
}

package paufregi.connectfeed.data.api.strava

import okhttp3.OkHttpClient
import paufregi.connectfeed.data.api.strava.interceptors.StravaAuthInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Strava {

    @FormUrlEncoded
    @POST("/activities")
    suspend fun getActivities(): Response<Unit>

    companion object {
        const val BASE_URL = "https://www.strava.com/api/v3"

        fun client(authInterceptor: StravaAuthInterceptor, url: String): Strava  {
            val client = OkHttpClient.Builder().addInterceptor(authInterceptor)

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
                .create(Strava::class.java)
        }
    }
}
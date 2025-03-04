package paufregi.connectfeed.data.api.strava

import okhttp3.OkHttpClient
import paufregi.connectfeed.data.api.strava.interceptors.StravaAuthInterceptor
import paufregi.connectfeed.data.api.strava.models.Activity
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Strava {

    @GET("athlete/activities")
    suspend fun getLatestActivities(
        @Query("before") before: Long? = null,
        @Query("after") after: Long? = null,
        @Query("page") page: Int? = 1,
        @Query("per_page") perPage: Int,
    ): Response<List<Activity>>

    companion object {
        const val BASE_URL = "https://www.strava.com/api/v3/"

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
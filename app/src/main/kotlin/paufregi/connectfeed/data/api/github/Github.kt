package paufregi.connectfeed.data.api.github

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import paufregi.connectfeed.data.api.github.models.Release
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

interface Github {
    @GET("/repos/paufregi/GarminConnectFeed/releases/latest")
    suspend fun getLatestRelease(
    ): Response<Release>

    companion object {
        const val BASE_URL = "https://api.github.com"

        fun client(url: String): Github {
            val json = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(Github::class.java)
        }
    }
}
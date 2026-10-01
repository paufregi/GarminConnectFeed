package paufregi.connectfeed.data.api.garmin

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import paufregi.connectfeed.data.api.garmin.interceptors.AuthInterceptor
import paufregi.connectfeed.data.api.garmin.models.Activity
import paufregi.connectfeed.data.api.garmin.models.Course
import paufregi.connectfeed.data.api.garmin.models.Gear
import paufregi.connectfeed.data.api.garmin.models.UpdateActivity
import paufregi.connectfeed.data.api.garmin.models.UserProfile
import paufregi.connectfeed.data.api.garmin.models.Workout
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GarminConnect {

    @Multipart
    @POST("/upload-service/upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): Response<Unit>

    @GET("/activitylist-service/activities/search/activities")
    suspend fun getActivities(
        @Query("limit") limit: Int,
        @Query("start") start: Int = 0,
    ): Response<List<Activity>>

    @GET("/activity-service/activity/{id}")
    suspend fun getActivity(
        @Path("id") id: Long
    ): Response<Activity>

    @GET("/course-service/course")
    suspend fun getCourses(): Response<List<Course>>

    @GET("/userprofile-service/socialProfile")
    suspend fun getUserProfile(): Response<UserProfile>

    @GET("/workout-service/workout/{id}")
    suspend fun getWorkout(
        @Path("id") id: Long
    ): Response<Workout>

    @GET("/gear-service/gear/v2/list")
    suspend fun getGears(): Response<List<Gear>>

    @PUT("/activity-service/activity/{id}")
    suspend fun updateActivity(
        @Path("id") id: Long,
        @Body updateActivity: UpdateActivity,
    ): Response<Unit>

    @PUT("/gear-service/activity/v2/{activityId}/associated-gear")
    suspend fun associateGears(
        @Path("activityId") activityId: Long,
        @Body gears: List<String>,
    ): Response<Unit>

    companion object {
        const val BASE_URL = "https://connectapi.garmin.com"

        val headers = mapOf(
            "User-Agent" to "GCM-Android-5.23",
            "X-Garmin-User-Agent" to "com.garmin.android.apps.connectmobile/5.23; ; Google/sdk_gphone64_arm64/google; Android/33; Dalvik/2.1.0",
            "X-Garmin-Paired-App-Version" to "10861"
        )


        fun client(authInterceptor: AuthInterceptor, url: String): GarminConnect {
            val client = OkHttpClient.Builder().addInterceptor(authInterceptor)

            val json = Json {
                explicitNulls = false
                ignoreUnknownKeys = true
            }

            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .client(client.build())
                .build()
                .create(GarminConnect::class.java)
        }
    }
}

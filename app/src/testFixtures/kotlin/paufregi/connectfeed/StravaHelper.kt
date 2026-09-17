package paufregi.connectfeed

import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.RecordedRequest
import okhttp3.Headers
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.api.strava.models.AuthToken
import paufregi.connectfeed.data.api.strava.models.Bike
import paufregi.connectfeed.data.api.strava.models.Shoe
import kotlin.time.Instant

fun createStravaToken(expiresAt: Instant, accessToken: String = "ACCESS_TOKEN", refreshToken: String = "REFRESH_TOKEN") = AuthToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresAt = expiresAt,
)

val athlete = Athlete(1, listOf(Bike("b12345678987655", "Giant Contend")), listOf(Shoe("g12345678987655", "Mizuno Neo Vista")))
val stravaAuthToken = createStravaToken(today)
val stravaRefreshedAuthToken = createStravaToken(tomorrow, "NEW_ACCESS_TOKEN", "NEW_REFRESH_TOKEN")

val stravaAuthTokenJson = """
    {
        "token_type": "Bearer",
        "expires_at": ${today.toEpochMilliseconds()},
        "expires_in": 21600,
        "refresh_token": "${stravaAuthToken.refreshToken}",
        "access_token": "${stravaAuthToken.accessToken}",
        "athlete": {
            "id" : 1,
            "username" : "paufregi",
            "firstname" : "Paul",
            "lastname" : "Test",
            "weight": 76.1
        }
    }
    """.trimIndent()

val stravaRefreshTokenJson = """
    {
        "token_type": "Bearer",
        "expires_at": ${stravaRefreshedAuthToken.expiresAt.toEpochMilliseconds()},
        "expires_in": 21600,
        "refresh_token": "${stravaRefreshedAuthToken.refreshToken}",
        "access_token": "${stravaRefreshedAuthToken.accessToken}",
        "athlete": {
            "id" : 1,
            "username" : "paufregi",
            "firstname" : "Paul",
            "lastname" : "Test",
            "weight": 76.1
        }
    }
    """.trimIndent()

val stravaDeauthorizationJson = """
    {
        "access_token": "REVOKED_ACCESS_TOKEN"
    }
    """.trimIndent()

val stravaAthlete = """
    {
      "id" : 1,
      "username" : "paufregi",
      "resource_state" : 3,
      "firstname" : "Paul",
      "lastname" : "Ellis",
      "city" : "Auckland",
      "state" : "NZ",
      "country" : "NZ",
      "sex" : "M",
      "premium" : true,
      "created_at" : "2017-11-14T02:30:05Z",
      "updated_at" : "2018-02-06T19:32:20Z",
      "badge_type_id" : 4,
      "profile_medium" : "https://xxxxxx.cloudfront.net/pictures/athletes/123456789/123456789/2/medium.jpg",
      "profile" : "https://xxxxx.cloudfront.net/pictures/athletes/123456789/123456789/2/large.jpg",
      "friend" : null,
      "follower" : null,
      "follower_count" : 5,
      "friend_count" : 5,
      "mutual_friend_count" : 0,
      "athlete_type" : 1,
      "date_preference" : "%m/%d/%Y",
      "measurement_preference" : "meters",
      "clubs" : [ ],
      "ftp" : null,
      "weight" : 0,
      "bikes" : [ {
        "id" : "b12345678987655",
        "primary" : true,
        "name" : "Giant Contend",
        "resource_state" : 2,
        "distance" : 0
      } ],
      "shoes" : [ {
        "id" : "g12345678987655",
        "primary" : true,
        "name" : "Mizuno Neo Vista",
        "resource_state" : 2,
        "distance" : 4904
      } ]
    }
""".trimIndent()

val stravaActivitiesJson = """
    [ {
      "resource_state" : 2,
      "athlete" : {
        "id" : 134815,
        "resource_state" : 1
      },
      "name" : "Happy Friday",
      "distance" : 7803.6,
      "moving_time" : 4500,
      "elapsed_time" : 4500,
      "total_elevation_gain" : 0,
      "type" : "Run",
      "sport_type" : "Run",
      "workout_type" : null,
      "id" : 1,
      "external_id" : "garmin_push_12345678987654321",
      "upload_id" : 987654321234567891234,
      "start_date" : "2018-05-02T12:15:09Z",
      "start_date_local" : "2018-05-02T05:15:09Z",
      "timezone" : "(GMT-08:00) America/Los_Angeles",
      "utc_offset" : -25200,
      "start_latlng" : null,
      "end_latlng" : null,
      "location_city" : null,
      "location_state" : null,
      "location_country" : "United States",
      "achievement_count" : 0,
      "kudos_count" : 3,
      "comment_count" : 1,
      "athlete_count" : 1,
      "photo_count" : 0,
      "map" : {
        "id" : "a12345678987654321",
        "summary_polyline" : null,
        "resource_state" : 2
      },
      "trainer" : true,
      "commute" : false,
      "manual" : false,
      "private" : false,
      "flagged" : false,
      "gear_id" : "b12345678987654321",
      "from_accepted_tag" : false,
      "average_speed" : 5.54,
      "max_speed" : 11,
      "average_cadence" : 67.1,
      "average_watts" : 175.3,
      "weighted_average_watts" : 210,
      "kilojoules" : 788.7,
      "device_watts" : true,
      "has_heartrate" : true,
      "average_heartrate" : 140.3,
      "max_heartrate" : 178,
      "max_watts" : 406,
      "pr_count" : 0,
      "total_photo_count" : 1,
      "has_kudoed" : false,
      "suffer_score" : 82
    }, {
      "resource_state" : 2,
      "athlete" : {
        "id" : 167560,
        "resource_state" : 1
      },
      "name" : "Bondcliff",
      "distance" : 23676.5,
      "moving_time" : 5400,
      "elapsed_time" : 5400,
      "total_elevation_gain" : 0,
      "type" : "Ride",
      "sport_type" : "Ride",
      "workout_type" : null,
      "id" : 2,
      "external_id" : "garmin_push_12345678987654321",
      "upload_id" : 1234567819,
      "start_date" : "2024-10-24T07:15:30Z",
      "start_date_local" : "2024-10-24T20:15:30Z",
      "timezone" : "(GMT-08:00) America/Los_Angeles",
      "utc_offset" : -25200,
      "start_latlng" : null,
      "end_latlng" : null,
      "location_city" : null,
      "location_state" : null,
      "location_country" : "United States",
      "achievement_count" : 0,
      "kudos_count" : 4,
      "comment_count" : 0,
      "athlete_count" : 1,
      "photo_count" : 0,
      "map" : {
        "id" : "a12345689",
        "summary_polyline" : null,
        "resource_state" : 2
      },
      "trainer" : true,
      "commute" : false,
      "manual" : false,
      "private" : false,
      "flagged" : false,
      "gear_id" : "b12345678912343",
      "from_accepted_tag" : false,
      "average_speed" : 4.385,
      "max_speed" : 8.8,
      "average_cadence" : 69.8,
      "average_watts" : 200,
      "weighted_average_watts" : 214,
      "kilojoules" : 1080,
      "device_watts" : true,
      "has_heartrate" : true,
      "average_heartrate" : 152.4,
      "max_heartrate" : 183,
      "max_watts" : 403,
      "pr_count" : 0,
      "total_photo_count" : 1,
      "has_kudoed" : false,
      "suffer_score" : 162
    } ]
""".trimIndent()

val stravaDetailedAthlete = """
    {    
      "id" : 1,
      "username" : "paufregi"
      "firstname" : "Paul",
      "lastname" : "Test",
      "weight": 76.1
    }
""".trimIndent()

const val stravaPort = 8084

val stravaDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.url.encodedPath
        val fields = request.getFields()
        return when {
            path.startsWith("/oauth/mobile/authorize") ->
                MockResponse(302, Headers.headersOf("Location", "paufregi.connectfeed://strava/auth?code=123456"))

            path == "/api/v3/oauth/token" && request.method == "POST" && fields["grant_type"] == "authorization_code" ->
                MockResponse(code = 200, body = stravaAuthTokenJson)

            path == "/api/v3/oauth/token" && request.method == "POST" && fields["grant_type"] == "refresh_token" ->
                MockResponse(code = 200, body = stravaRefreshTokenJson)

            path == "/oauth/deauthorize" && request.method == "POST" ->
                MockResponse(code = 200, body = stravaDeauthorizationJson)

            path == "/athlete" && request.method == "GET" ->
                MockResponse(code = 200, body = stravaAthlete)

            path.startsWith("/athlete/activities") && request.method == "GET" ->
                MockResponse(code = 200, body = stravaActivitiesJson)

            path.startsWith("/activities/") && request.method == "PUT" ->
                MockResponse(200)

            path == "/athlete" && request.method == "PUT" ->
                MockResponse(code = 200, body = stravaDetailedAthlete)

            else -> MockResponse(404)
        }
    }
}

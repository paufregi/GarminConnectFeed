package paufregi.connectfeed.data.datastore.models

import kotlinx.serialization.Serializable
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.models.AuthTokenDto as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthTokenDto as StravaAuthToken

@Serializable
data class Auth(
    val user: User? = null,
    val garminToken: GarminAuthToken? = null,
    val stravaToken: StravaAuthToken? = null,
)
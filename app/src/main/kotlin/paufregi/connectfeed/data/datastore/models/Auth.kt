package paufregi.connectfeed.data.datastore.models

import kotlinx.serialization.Serializable
import paufregi.connectfeed.data.api.garmin.models.AuthToken as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthToken as StravaAuthToken

@Serializable
data class Auth(
    val garminToken: GarminAuthToken? = null,
    val stravaToken: StravaAuthToken? = null
)
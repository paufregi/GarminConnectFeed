package paufregi.connectfeed.presentation.strava

sealed interface StravaState {
    data object Processing : StravaState
    data object Success : StravaState
    data object Failure : StravaState
}
package paufregi.connectfeed.presentation.main

data class MainState(
    val loggedIn: Boolean? = null,
    val showLogin: Boolean? = null,
) {
    val showApp: Boolean
        get() = loggedIn == true && showLogin != true
}
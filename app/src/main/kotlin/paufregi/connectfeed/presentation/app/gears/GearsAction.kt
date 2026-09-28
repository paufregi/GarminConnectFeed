package paufregi.connectfeed.presentation.app.gears

sealed interface GearsAction {
    data object Reset : GearsAction
    data object Sync : GearsAction
}


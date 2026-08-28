package paufregi.connectfeed.presentation.gears

sealed interface GearsAction {
    data object Reset : GearsAction
    data object Sync : GearsAction
}


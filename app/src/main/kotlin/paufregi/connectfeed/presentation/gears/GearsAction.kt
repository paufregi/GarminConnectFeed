package paufregi.connectfeed.presentation.gears

sealed interface GearsAction {
    data object Sync : GearsAction
}

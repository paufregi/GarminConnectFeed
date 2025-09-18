package paufregi.connectfeed.presentation.info

interface InfoAction {
    data object CheckUpdate : InfoAction
    data object Update : InfoAction
}
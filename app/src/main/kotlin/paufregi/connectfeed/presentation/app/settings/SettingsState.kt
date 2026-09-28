package paufregi.connectfeed.presentation.app.settings

import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.presentation.ui.models.ProcState

data class SettingsState(
    val process: ProcState? = null,
    val updating: Boolean = false,
    val user: User? = null,
    val hasStrava: Boolean? = null,
    val currentVersion: Version? = null,
    val latestRelease: Release? = null,
) {
    val hasUpdate: Boolean
        get() = latestRelease?.version?.let { currentVersion?.isLowerThan(it) } ?: false
}

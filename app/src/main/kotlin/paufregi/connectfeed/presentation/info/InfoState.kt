package paufregi.connectfeed.presentation.info

import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.presentation.ui.models.ProcessState

data class InfoState(
    val process: ProcessState = ProcessState.Idle,
    val currentVersion: Version? = null,
    val latestRelease: Release? = null,
) {
    val hasUpdate: Boolean
        get() = latestRelease?.version?.let { currentVersion?.isLowerThan(it) } ?: false
}

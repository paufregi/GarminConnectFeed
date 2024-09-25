package paufregi.garminfeed.lifecycle

import paufregi.garminfeed.data.database.models.Credentials
import paufregi.garminfeed.models.ImportStatus

data class State(
    val credentials: Credentials? = null,
    val importStatus: ImportStatus? = null,
    val clearCacheToast: Boolean = false
)
package paufregi.connectfeed.core.usecases

import paufregi.connectfeed.core.models.Version
import javax.inject.Inject
import javax.inject.Named

class GetVersion @Inject constructor(@param:Named("currentVersion") val version: String) {
    operator fun invoke(): Version? = Version.parse(version)
}

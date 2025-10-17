package paufregi.connectfeed.presentation.settings

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.core.models.Version

class SettingsStatePreview : PreviewParameterProvider<SettingsState> {
    val user = User(1, "John Doe", "https://example.com")
    val version = Version(1, 2, 3)

    override val values = sequenceOf(
        SettingsState(hasStrava = false, user = user, currentVersion = version, latestRelease = Release(version, "https://example.com")),
        SettingsState(hasStrava = true, user = user, currentVersion = version, latestRelease = Release(Version(2,0,0), "https://example.com")),
        SettingsState(hasStrava = true, user = user, currentVersion = version, latestRelease = Release(Version(2,0,0), "https://example.com"), updating = true),
    )
}
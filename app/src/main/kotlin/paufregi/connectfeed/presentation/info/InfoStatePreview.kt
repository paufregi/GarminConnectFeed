package paufregi.connectfeed.presentation.info

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.presentation.ui.models.ProcessState

class InfoStatePreview : PreviewParameterProvider<InfoState> {
    override val values = sequenceOf(
        InfoState(process = ProcessState.Processing),
        InfoState(
            process = ProcessState.Success("Ok"),
            latestRelease = Release(version = Version(1, 0, 0), downloadUrl = "url")
        ),
        InfoState(process = ProcessState.Failure("Error")),
        InfoState(process = ProcessState.Idle),
    )
}

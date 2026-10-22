package paufregi.connectfeed.presentation.strava

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.components.loading.ActivityLoading
import paufregi.connectfeed.presentation.ui.components.screens.ExternalScreen
import paufregi.connectfeed.presentation.ui.components.status.ActivityFailure
import paufregi.connectfeed.presentation.ui.components.status.ActivitySuccess
import paufregi.connectfeed.presentation.ui.models.ProcState
import paufregi.connectfeed.presentation.ui.theme.Theme

private const val TITLE = "Connect Strava"

@AndroidEntryPoint
@ExperimentalMaterial3Api
class StravaActivity : ComponentActivity() {
    private val viewModel: StravaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        intent.data?.getQueryParameter("code")?.let {
            viewModel.exchangeToken(it)
        }

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val user by viewModel.user.collectAsStateWithLifecycle()
            Theme {
                StravaContent(state, user) { finish() }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
internal fun StravaContent(
    state: ProcState,
    user: User? = null,
    onDone: () -> Unit = {},
) {
    ExternalScreen(user = user) {
        when (val process = state) {
            is ProcState.Success -> ActivitySuccess(TITLE, user, "Strava linked!") { onDone() }
            is ProcState.Failure -> ActivityFailure(TITLE, user, "Link failed!", process.reason) { onDone() }
            is ProcState.Running -> ActivityLoading(TITLE, user)
        }
    }
}

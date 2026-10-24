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
import paufregi.connectfeed.presentation.ui.components.frame.ActivityFrame
import paufregi.connectfeed.presentation.ui.components.frame.OkBar
import paufregi.connectfeed.presentation.ui.components.loading.Loading
import paufregi.connectfeed.presentation.ui.components.status.FailureMessage
import paufregi.connectfeed.presentation.ui.components.status.SuccessMessage
import paufregi.connectfeed.presentation.ui.models.Status
import paufregi.connectfeed.presentation.ui.theme.Theme

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
    state: Status,
    user: User? = null,
    onDone: () -> Unit = {},
) {
    ActivityFrame(
        title = "Connect Strava",
        user = user,
        loading = state is Status.Loading,
        bottomBar = { OkBar(onDone) },
    ) { padding ->
        when (state) {
            is Status.Success -> SuccessMessage("Strava linked!", padding)
            is Status.Failure -> FailureMessage("Link failed!", state.reason, padding)
            is Status.Loading -> Loading()
        }
    }
}

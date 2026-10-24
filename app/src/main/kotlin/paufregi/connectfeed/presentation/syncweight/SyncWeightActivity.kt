package paufregi.connectfeed.presentation.syncweight

import android.content.Intent
import android.net.Uri
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
import paufregi.connectfeed.presentation.ui.components.status.FailureMessage
import paufregi.connectfeed.presentation.ui.components.status.SuccessMessage
import paufregi.connectfeed.presentation.ui.models.ProcState
import paufregi.connectfeed.presentation.ui.theme.Theme

private const val TITLE = "Sync weight"

@AndroidEntryPoint
@ExperimentalMaterial3Api
class SyncWeightActivity : ComponentActivity() {
    private val viewModel: SyncWeightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)?.let {
            viewModel.updateWeight(contentResolver.openInputStream(it))
        }

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val user by viewModel.user.collectAsStateWithLifecycle()
            Theme {
                SyncWeightContent(state, user) { finish() }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
internal fun SyncWeightContent(
    state: ProcState,
    user: User? = null,
    onDone: () -> Unit = {},
) {
    ActivityFrame(
        title = TITLE,
        user = user,
        loading = state is ProcState.Running,
        bottomBar = { OkBar(onDone) },
    ) { padding ->
        when (state) {
            is ProcState.Success -> SuccessMessage(state.message ?: "", padding)
            is ProcState.Failure -> FailureMessage("Sync failed!", state.reason, padding)
            is ProcState.Running -> Unit
        }
    }
}

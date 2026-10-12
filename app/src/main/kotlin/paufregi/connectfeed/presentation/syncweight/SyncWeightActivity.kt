package paufregi.connectfeed.presentation.syncweight

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import paufregi.connectfeed.presentation.ui.components.loading.ActivityLoading
import paufregi.connectfeed.presentation.ui.components.status.ActivityFailure
import paufregi.connectfeed.presentation.ui.components.status.ActivitySuccess
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
                when(val s = state) {
                    is ProcState.Success -> ActivitySuccess(TITLE, user, s.message ?: "") { finish() }
                    is ProcState.Failure -> ActivityFailure(TITLE, user,"Sync failed!", s.reason) { finish() }
                    is ProcState.Running -> ActivityLoading(TITLE, user)
                }
            }
        }
    }
}

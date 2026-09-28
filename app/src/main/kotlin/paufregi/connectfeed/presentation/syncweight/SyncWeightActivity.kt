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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import paufregi.connectfeed.presentation.ui.components.Loading
import paufregi.connectfeed.presentation.ui.components.failureInfo
import paufregi.connectfeed.presentation.ui.components.frame.Frame
import paufregi.connectfeed.presentation.ui.components.successInfo
import paufregi.connectfeed.presentation.ui.models.ProcState
import paufregi.connectfeed.presentation.ui.theme.Theme

@AndroidEntryPoint
@ExperimentalMaterial3Api
class SyncWeightActivity : ComponentActivity() {
    private val viewModel: SyncWeightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)?.let {
            viewModel.updateWeight(contentResolver.openInputStream(it))
        }

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            Theme {
                Frame(enableMenu = false) {
                    when(state) {
                        is ProcState.Success -> successInfo { finish() }
                        is ProcState.Failure -> failureInfo { finish() }
                        is ProcState.Running -> Loading()
                    }
                }
            }
        }
    }
}

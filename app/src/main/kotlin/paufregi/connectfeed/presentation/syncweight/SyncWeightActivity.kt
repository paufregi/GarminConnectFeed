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
import paufregi.connectfeed.presentation.ui.theme.Theme

@AndroidEntryPoint
@ExperimentalMaterial3Api
class SyncWeightActivity : ComponentActivity() {
    private val viewModel: SyncWeightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)?.let { uri ->
            contentResolver.openInputStream(uri).let { input ->
                viewModel.syncWeight(input)
            }
        }

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            Theme {
                SyncWeightScreen(
                    state = state,
                    onComplete = { finish() }
                )
            }
        }
    }
}
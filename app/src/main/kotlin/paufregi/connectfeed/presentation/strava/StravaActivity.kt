package paufregi.connectfeed.presentation.strava

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import paufregi.connectfeed.presentation.ui.theme.Theme
import kotlin.getValue

@AndroidEntryPoint
@ExperimentalMaterial3Api
class StravaActivity: ComponentActivity() {
    private val viewModel: StravaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val code = intent.data?.getQueryParameter("code")

        Log.i("StravaActivity", "code: $code")

        code?.let { viewModel.saveCode(it) }

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            Theme {
                StravaScreen(
                    state = state,
                    onComplete = { finish() }
                )
            }
        }
    }

}
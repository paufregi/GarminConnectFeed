package paufregi.connectfeed.presentation.strava

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold
import paufregi.connectfeed.presentation.ui.theme.Theme
import kotlin.getValue

@ExperimentalMaterial3Api
class StravaActivity: ComponentActivity() {
    private val viewModel: StravaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val code = intent.data?.getQueryParameter("code")

        code?.let { viewModel.saveCode(it) }

        setContent {
            Theme {
                SimpleScaffold { pd ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(pd)
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                            .testTag("account_form")
                    ) {
                        TextField(value=code ?: "", onValueChange = {}, label = { Text("Strava OAuth") })
                    }
                }
            }

        }
    }

}
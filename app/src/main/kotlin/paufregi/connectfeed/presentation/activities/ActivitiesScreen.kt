package paufregi.connectfeed.presentation.activities

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import paufregi.connectfeed.presentation.ui.components.SimpleScaffold

@Composable
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
internal fun ActivitiesScreen(
    nav: NavHostController = rememberNavController(),
) {
    val viewModel = hiltViewModel<ActivitiesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SimpleScaffold { Text("Activities") }
}

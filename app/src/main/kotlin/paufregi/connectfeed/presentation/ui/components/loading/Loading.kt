package paufregi.connectfeed.presentation.ui.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
fun Loading(
    padding: PaddingValues = PaddingValues()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(padding)
            .padding(padding.add(horizontal = 20.dp))
            .testTag("loading")
    ) {
        CircularProgressIndicator(
            strokeWidth = 8.dp,
            modifier = Modifier.size(100.dp)
        )
    }
}
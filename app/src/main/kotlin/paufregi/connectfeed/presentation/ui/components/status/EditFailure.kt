package paufregi.connectfeed.presentation.ui.components.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.presentation.ui.components.frame.BottomApp
import paufregi.connectfeed.presentation.ui.components.frame.FormFrame
import paufregi.connectfeed.presentation.ui.components.frame.TopButton
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
internal fun EditFailure(
    title: String,
    reason: String,
    actionIcon: @Composable RowScope.() -> Unit = {},
    onReset: () -> Unit
) {
    FormFrame(
        title = title,
        navIcon = TopButton.back(enabled = false),
        actionIcon = actionIcon,
        bottomContent = BottomApp.ok(onReset)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(20.dp))
                .testTag("edit_failure")
        ) {
            Icon(
                imageVector = Icons.Filled.Cancel,
                contentDescription = "Failure",
                tint = Color.Red,
                modifier = Modifier
                    .size(110.dp)
                    .testTag("icon")
            )
            Text(
                text = "Update failed!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .testTag("title")
            )
            Text(
                text = "Something went wrong: $reason",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("reason")
            )
        }
    }
}
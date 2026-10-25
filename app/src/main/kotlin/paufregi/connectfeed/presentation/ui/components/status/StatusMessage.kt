package paufregi.connectfeed.presentation.ui.components.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.presentation.ui.utils.add

/**
 * Centered result message: large icon, headline and optional detail, followed by any
 * extra [content] (e.g. deep-link buttons). Meant to be used as the body of a frame.
 */
@Composable
fun StatusMessage(
    icon: ImageVector,
    tint: Color,
    message: String,
    padding: PaddingValues = PaddingValues(),
    detail: String? = null,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(padding)
            .padding(padding.add(20.dp))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = message,
            tint = tint,
            modifier = Modifier.size(110.dp).testTag("icon")
        )
        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = if (detail == null) 50.dp else 20.dp)
                .testTag("message")
        )
        detail?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("reason")
            )
        }
        content()
    }
}

@Composable
fun SuccessMessage(
    message: String,
    padding: PaddingValues = PaddingValues(),
    content: @Composable ColumnScope.() -> Unit = {},
) = StatusMessage(
    icon = Icons.Filled.CheckCircle,
    tint = Color.Green,
    message = message,
    padding = padding,
    content = content,
)

@Composable
fun FailureMessage(
    message: String,
    reason: String,
    padding: PaddingValues = PaddingValues(),
) = StatusMessage(
    icon = Icons.Filled.Cancel,
    tint = Color.Red,
    message = message,
    padding = padding,
    detail = "Something went wrong: $reason",
)


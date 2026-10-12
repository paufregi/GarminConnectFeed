package paufregi.connectfeed.presentation.ui.components.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.presentation.ui.components.frame.ActivityFrame
import paufregi.connectfeed.presentation.ui.components.frame.BottomApp
import paufregi.connectfeed.presentation.ui.utils.add

@Composable
@ExperimentalMaterial3Api
internal fun ActivitySuccess(
    title: String,
    user: User? = null,
    message: String,
    onDone: () -> Unit = {},
) {
    ActivityFrame(
        title = title,
        user = user,
        bottomContent = BottomApp.ok { onDone() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .padding(it.add(20.dp))
                .testTag("success")
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = Color.Green,
                modifier = Modifier
                    .size(110.dp)
                    .testTag("icon")
            )
            Text(
                text = message,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .testTag("message")
            )
        }
    }
}

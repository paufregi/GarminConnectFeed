package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import paufregi.connectfeed.presentation.ui.components.Button

object BottomApp{

    @Composable
    fun ok(onClick: () -> Unit = {}): @Composable () -> Unit = {
        BottomAppBar {
            Button(
                modifier = Modifier.fillMaxWidth().testTag("ok"),
                text = "OK",
                onClick = onClick,
            )
        }
    }

    @Composable
    fun save(
        enabled: Boolean = true,
        onClick: () -> Unit = {}
    ): @Composable () -> Unit = {
        BottomAppBar {
            Button(
                modifier = Modifier.fillMaxWidth().testTag("save"),
                text = "Save",
                enabled = enabled,
                onClick = onClick,
            )
        }
    }
}
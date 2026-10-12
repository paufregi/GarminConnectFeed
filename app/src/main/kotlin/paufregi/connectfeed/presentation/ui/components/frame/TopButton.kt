package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import paufregi.connectfeed.presentation.ui.components.Button

object TopButton {
    @Composable
    fun back(
        enabled: Boolean = true,
        onClick: () -> Unit = {},
    ): @Composable () -> Unit = {
        Button(
            modifier = Modifier.testTag("nav_back"),
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            enabled = enabled,
            onClick = onClick
        )
    }

    @Composable
    fun action(
        icon: ImageVector,
        enabled: Boolean = true,
        onClick: () -> Unit = {},
    ): @Composable RowScope.() -> Unit = {
        Button(
            modifier = Modifier.testTag("action"),
            icon = icon,
            enabled = enabled,
            onClick = onClick
        )
    }
}
package paufregi.connectfeed.presentation.ui.components.frame

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import paufregi.connectfeed.presentation.ui.components.Button

@Composable
fun MenuButton(
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) = Button(
    modifier = Modifier.testTag("menu"),
    icon = Icons.Filled.Menu,
    enabled = enabled,
    onClick = onClick,
)

@Composable
fun BackButton(
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) = Button(
    modifier = Modifier.testTag("nav_back"),
    icon = Icons.AutoMirrored.Filled.ArrowBack,
    enabled = enabled,
    onClick = onClick,
)

@Composable
fun ActionButton(
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) = Button(
    modifier = Modifier.testTag("action"),
    icon = icon,
    enabled = enabled,
    onClick = onClick,
)


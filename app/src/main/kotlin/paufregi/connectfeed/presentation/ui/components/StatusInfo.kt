package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

sealed class StatusInfoType(
    val icon: ImageVector,
    val color: Color
) {
    object Success : StatusInfoType(Icons.Default.CheckCircleOutline, Color.Green)
    object Failure : StatusInfoType(Icons.Default.WarningAmber, Color.Red)
    object Unknown : StatusInfoType(Icons.Default.WarningAmber, Color.DarkGray)
}

@Composable
@ExperimentalMaterial3Api
fun StatusInfo(
    type: StatusInfoType,
    text: String = "",
    actionButton: @Composable () -> Unit = { },
    paddingValues: PaddingValues = PaddingValues()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Icon(
            imageVector = type.icon,
            contentDescription = text,
            tint = type.color,
            modifier = Modifier.scale(2.5f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.testTag("status_info_text")
        )
        Spacer(modifier = Modifier.height(50.dp))
        actionButton()
    }
}

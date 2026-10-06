package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceHappy
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceNormal
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceSad
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceVeryHappy
import paufregi.connectfeed.presentation.ui.icons.garmin.FaceVerySad

private data class IconRadioItem(
    val value: Float,
    val icon: ImageVector
)

@Composable
fun FeelSelector(
    value: Float?,
    onChange: (Float?) -> Unit,
) {
    val options = listOf(
        IconRadioItem(0f, Icons.Connect.FaceVerySad),
        IconRadioItem(25f, Icons.Connect.FaceSad),
        IconRadioItem(50f, Icons.Connect.FaceNormal),
        IconRadioItem(75f, Icons.Connect.FaceHappy),
        IconRadioItem(100f, Icons.Connect.FaceVeryHappy),
    )

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = value == option.value
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 50.dp else 42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .alpha(if (isSelected) 1f else 0.5f)
                        .clickable { onChange(if (isSelected) null else option.value) }
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(if (isSelected) 46.dp else 38.dp)
                            .alpha(if (isSelected) 1f else 0.5f)
                    )
                }
            }
        }
        Text(
            feelLabel(value),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
                .testTag("feel_text")
        )
    }
}

private fun feelLabel(value: Float?) = when (value) {
    0f -> "Very weak"
    25f -> "Weak"
    50f -> "Normal"
    75f -> "Strong"
    100f -> "Very strong"
    null -> "None selected"
    else -> "What!?"
}

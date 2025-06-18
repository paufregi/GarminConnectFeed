package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
fun EffortSlider(
    value: Float?,
    onValueChange: (Float) -> Unit,
    interactionSource: MutableInteractionSource
) {
    Column {
        Slider(
            value = value ?: 0f,
            onValueChange = onValueChange,
            valueRange = 0f..100f,
            steps = 9,
            interactionSource = interactionSource,
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(10.dp),
                    thumbTrackGapSize = 0.dp,
                )
            },
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        TextEffort(
            value ?: 0f,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag("effort_text")
        )
    }
}
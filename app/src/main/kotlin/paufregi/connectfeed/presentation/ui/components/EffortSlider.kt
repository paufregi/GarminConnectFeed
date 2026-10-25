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
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
fun EffortSlider(
    value: Float?,
    onChange: (Float?) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        Slider(
            value = value ?: 0f,
            onValueChange = { onChange(it.toInt().toFloat()) },
            valueRange = 0f..100f,
            steps = 9,
            interactionSource = interactionSource,
            track = CustomSlider.track,
            thumb = CustomSlider.thumb(interactionSource),
            modifier = Modifier.fillMaxWidth()
        )
        Text(effortLabel(value), modifier = Modifier.align(Alignment.CenterHorizontally).testTag("effort_text"))
    }
}

private fun effortLabel(value: Float?): String {
    val score = ((value ?: 0f) / 10).toInt()
    val label = when (score) {
        0 -> "None selected"
        1 -> "Very light"
        2 -> "Light"
        3 -> "Moderate"
        4 -> "Somewhat Hard"
        5, 6 -> "Hard"
        7, 8 -> "Very Hard"
        9 -> "Extremely Hard"
        10 -> "Maximum"
        else -> "What!?"
    }

    return "$score - $label"
}

private object CustomSlider {

    @ExperimentalMaterial3Api
    val track: @Composable (SliderState) -> Unit = { sliderState ->
        SliderDefaults.Track(
            sliderState = sliderState,
            modifier = Modifier.height(10.dp),
            thumbTrackGapSize = 0.dp,
        )
    }

    @ExperimentalMaterial3Api
    fun thumb(interactionSource: MutableInteractionSource): @Composable (SliderState) -> Unit = {
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape),
        )
    }
}

package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun RowCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    label: String,
    tagName: String = "row_checkbox",
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onCheckedChange(!checked) }
            )
    ) {
        Checkbox(
            modifier = Modifier.testTag(tagName),
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Text(text = label)
    }
}
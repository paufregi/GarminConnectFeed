package paufregi.connectfeed.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.utils.Formatter

data class DropdownItem(
    val text: String,
    val distance: String? = null,
    val activityType: ActivityType? = null,
    val onClick: () -> Unit
)

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Activity.toDropdownItem(onClick: () -> Unit) = DropdownItem(
    text = name,
    distance = distance?.takeIf { it > 0 }?.let { Formatter.distance(it) },
    activityType = type,
    onClick = onClick
)

@ExperimentalMaterial3Api
fun ActivityType.toDropdownItem(onClick: () -> Unit) = DropdownItem(
    text = name,
    activityType = this,
    onClick = onClick
)

@ExperimentalMaterial3Api
fun EventType.toDropdownItem(onClick: () -> Unit) = DropdownItem(
    text = name,
    onClick = onClick
)

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Course.toDropdownItem(onClick: () -> Unit) = DropdownItem(
    text = name,
    distance = Formatter.distance(distance),
    activityType = type,
    onClick = onClick
)

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Profile.toDropdownItem(onClick: () -> Unit) = DropdownItem(
    text = this.name,
    distance = course?.let { Formatter.distance(it.distance) },
    activityType = activityType,
    onClick = onClick
)

@Composable
@ExperimentalMaterial3Api
fun Dropdown(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    selected: DropdownItem? = null,
    items: List<DropdownItem> = emptyList(),
    isError: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            label = label,
            value = selected?.text ?: "",
            supportingText = { DistanceText(selected?.distance) },
            leadingIcon = { ActivityIcon(selected?.activityType) },
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            isError = isError,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it.text) },
                    leadingIcon = { ActivityIcon(it.activityType) },
                    trailingIcon = { DistanceText(it.distance) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    onClick = {
                        it.onClick()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DistanceText(
    distance: String?
) {
    if (distance != null) {
        Text(text = "$distance km", fontSize = 11.sp)
    }
}

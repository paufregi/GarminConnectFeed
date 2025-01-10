package paufregi.connectfeed.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.sp
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.utils.Formatter
import kotlin.String

sealed interface DropdownItem {
    val text: String
    val activityType: ActivityType?
    val onClick: () -> Unit

    data class TextDropdownItem(
        override val text: String,
        override val activityType: ActivityType? = null,
        override val onClick: () -> Unit
    ) : DropdownItem

    data class DistanceDropdownItem(
        override val text: String,
        val distance: String?,
        override val activityType: ActivityType? = null,
        override val onClick: () -> Unit
    ) : DropdownItem
}

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Activity.toDropdownItem(onClick: () -> Unit) = DropdownItem.DistanceDropdownItem(
    text = name,
    distance = distance?.let { Formatter.distance(it) },
    activityType = type,
    onClick = onClick
)

@ExperimentalMaterial3Api
fun ActivityType.toDropdownItem(onClick: () -> Unit) = DropdownItem.TextDropdownItem(
    text = name,
    activityType = this,
    onClick = onClick
)

@ExperimentalMaterial3Api
fun EventType.toDropdownItem(onClick: () -> Unit) = DropdownItem.TextDropdownItem(
    text = name,
    onClick = onClick
)

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Course.toDropdownItem(onClick: () -> Unit) = DropdownItem.DistanceDropdownItem(
    text = name,
    distance = Formatter.distance(distance),
    activityType = type,
    onClick = onClick
)

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Profile.toDropdownItem(onClick: () -> Unit) = DropdownItem.DistanceDropdownItem(
    text = this.name,
    distance = course?.let { Formatter.distance(it.distance) },
    activityType = course?.type,
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

    ExposedDropdownMenuBox (
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            label = label,
            value = selected?.text ?: "",
            suffix = { Suffix(selected) },
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
        ){
            items.forEach{
                DropdownMenuItem(
                    text = { TextDropdownMenuItem(it) },
                    leadingIcon = { ActivityIcon(it.activityType) },
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
fun Suffix(
    item: DropdownItem?
) {
    when (item) {
        is DropdownItem.DistanceDropdownItem -> item.distance?.let { Text(text = "$it km", fontSize = 10.sp) }
        else -> null
    }
}

@Composable
fun TextDropdownMenuItem(
    item: DropdownItem
) {
    when (item) {
        is DropdownItem.TextDropdownItem -> Text(item.text)

        is DropdownItem.DistanceDropdownItem -> Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(item.text)
            if (item.distance != null) Text(text = "${item.distance} km", fontSize = 10.sp)
        }
    }
}

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
    val leadingIcon: @Composable (() -> Unit)?
    val onClick: () -> Unit

    data class TextDropdownItem(
        override val text: String,
        override val leadingIcon: @Composable (() -> Unit)? = null,
        override val onClick: () -> Unit
    ) : DropdownItem

    data class DistanceDropdownItem(
        override val text: String,
        val distance: String?,
        override val leadingIcon: @Composable (() -> Unit)? = null,
        override val onClick: () -> Unit
    ) : DropdownItem

    data class ProfileDropdownItem(
        override val text: String,
        val distance: String?,
        val water: Boolean,
        override val leadingIcon: @Composable (() -> Unit)? = null,
        override val onClick: () -> Unit
    ) : DropdownItem
}

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Activity.toDropdownItem(onClick: () -> Unit) = DropdownItem.DistanceDropdownItem(
    text = name,
    distance = if (distance != null) Formatter.distance(distance) else null,
    leadingIcon = { ActivityIcon(this.type) },
    onClick = onClick
)

@ExperimentalMaterial3Api
fun ActivityType.toDropdownItem(onClick: () -> Unit) = DropdownItem.TextDropdownItem(
    text = name,
    leadingIcon = { ActivityIcon(this) },
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
    leadingIcon = { ActivityIcon(type) },
    onClick = onClick
)

@SuppressLint("DefaultLocale")
@ExperimentalMaterial3Api
fun Profile.toDropdownItem(onClick: () -> Unit) = DropdownItem.ProfileDropdownItem(
    text = this.name,
    distance = if (course != null) Formatter.distance(course.distance) else null,
    water = water != null,
    leadingIcon = { ActivityIcon(activityType) },
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
            leadingIcon = selected?.leadingIcon,
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
                    leadingIcon = it.leadingIcon,
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

        is DropdownItem.ProfileDropdownItem -> Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(item.text)
            Row {
                if (item.water) { Icon(modifier = Modifier.scale(0.7f), imageVector = Icons.Default.LocalDrink, contentDescription = null) }
                if (item.distance != null) Text(text = "${item.distance} km", fontSize = 10.sp)
            }
//            Column(horizontalAlignment = Alignment.End) {
//                Row {
//                    if (item.water) {
//                        Icon(
//                            modifier = Modifier.scale(0.8f),
//                            imageVector = Icons.Default.LocalDrink,
//                            contentDescription = null,
//                        )
//                    }
//                    if (item.distance != null) {
//                        Icon(
//                            modifier = Modifier.scale(0.8f),
//                            imageVector = Icons.Default.LocationOn,
//                            contentDescription = null,
//                        )
//                    }
//                }
//                if (item.distance != null) {
//                    Text(text = "${item.distance} km", fontSize = 10.sp)
//                }
//            }
        }
    }
}

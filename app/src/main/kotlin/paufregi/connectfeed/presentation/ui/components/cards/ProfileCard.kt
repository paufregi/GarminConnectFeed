package paufregi.connectfeed.presentation.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.components.Button
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Shoe
import paufregi.connectfeed.presentation.ui.utils.iconFor

@Composable
@ExperimentalMaterial3Api
fun ProfileCard(
    profile: Profile,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    @Composable
    fun iconTint(enabled: Boolean) =
        if (enabled)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        else
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)

    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        ) {
            Icon(
                imageVector = iconFor(profile.type),
                contentDescription = profile.type.toString(),
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.padding(1.dp)
            ) {
                Text(text = profile.name)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = "Course",
                        tint = iconTint(profile.course != null),
                        modifier = Modifier.size(12.dp)
                    )
                    Icon(
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = "Water",
                        tint = iconTint(profile.water != null),
                        modifier = Modifier.size(12.dp)
                    )
                    Icon(
                        imageVector = Icons.Connect.Shoe,
                        contentDescription = "Shoe",
                        tint = iconTint(profile.gear),
                        modifier = Modifier.size(12.dp)
                    )
                    Icon(
                        imageVector = Icons.Outlined.SentimentVerySatisfied,
                        contentDescription = "Feel & Effort",
                        tint = iconTint(profile.feelAndEffort),
                        modifier = Modifier.size(12.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                        contentDescription = "Training Effect",
                        tint = iconTint(profile.trainingEffect),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                icon = Icons.Default.Delete,
                onClick = onDelete,
                modifier = Modifier.size(20.dp).testTag("delete_profile_${profile.id}")
            )
        }
    }
}

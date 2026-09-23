package paufregi.connectfeed.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
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
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.presentation.ui.icons.garmin.Connect
import paufregi.connectfeed.presentation.ui.icons.garmin.Shoe
import paufregi.connectfeed.presentation.ui.utils.iconFor

@Composable
@ExperimentalMaterial3Api
fun ProfileCard(
    profile: Profile,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    @Composable
    fun iconTint(enabled: Boolean) =
        if (enabled)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconFor(profile.type),
                contentDescription = profile.type.toString(),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = "Course",
                        modifier = modifier.size(20.dp),
                        tint = iconTint(profile.course != null)
                    )

                    Icon(
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = "Water",
                        modifier = modifier.size(20.dp),
                        tint = iconTint(profile.water != null)
                    )

                    Icon(
                        imageVector = Icons.Connect.Shoe,
                        contentDescription = "Shoe",
                        modifier = modifier.size(20.dp),
                        tint = iconTint(profile.gear)
                    )

                    Icon(
                        imageVector = Icons.Outlined.SentimentVerySatisfied,
                        contentDescription = "Feel & Effort",
                        modifier = modifier.size(20.dp),
                        tint = iconTint(profile.feelAndEffort)
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                        contentDescription = "Training Effect",
                        modifier = modifier.size(20.dp),
                        tint = iconTint(profile.trainingEffect)
                    )
                }
            }
        }
    }
}

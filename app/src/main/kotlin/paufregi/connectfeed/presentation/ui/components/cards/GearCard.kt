package paufregi.connectfeed.presentation.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.presentation.ui.icons.strava.Logo
import paufregi.connectfeed.presentation.ui.icons.strava.Strava
import paufregi.connectfeed.presentation.ui.utils.iconFor

@Composable
@ExperimentalMaterial3Api
fun GearCard(
    gear: Gear,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Icon(
                imageVector = iconFor(gear.type),
                contentDescription = gear.type.toString(),
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.padding(1.dp)
            ) {
                Row{
                    Text(text = gear.name)
                    Spacer(modifier = Modifier.weight(1f))
                    gear.stravaId?.let {
                        Icon(
                            imageVector = Icons.Strava.Logo,
                            contentDescription = gear.stravaId,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                gear.distance?.let {
                    Text(
                        text = "${Formatter.distance(it)} km",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                }
            }
        }
    }
}

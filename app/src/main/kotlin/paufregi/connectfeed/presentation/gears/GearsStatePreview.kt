package paufregi.connectfeed.presentation.gears

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType

class GearsStatePreview : PreviewParameterProvider<GearsState> {
    override val values = sequenceOf(
        GearsState(gears = emptyList()),
        GearsState(
            gears = listOf(
                Gear(
                    id = "gear-1",
                    name = "Daily trainers",
                    type = GearType.Shoe,
                    distance = 53210,
                ),
                Gear(
                    id = "gear-2",
                    name = "Weekend bike",
                    type = GearType.Bike,
                    distance = 123456,
                ),
            )
        )
    )
}


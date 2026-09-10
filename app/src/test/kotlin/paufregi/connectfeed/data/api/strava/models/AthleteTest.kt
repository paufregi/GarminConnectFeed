package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType

class AthleteTest {

    @Test
    fun `toGearList converts shoes and bikes`() {
        val athlete = Athlete(
            id = 1,
            shoes = listOf(Shoe(id = "shoe-1", name = "Shoe 1", distance = 1234)),
            bikes = listOf(Bike(id = "bike-1", name = "Bike 1", distance = 5678))
        )

        assertThat(athlete.toGearList()).isEqualTo(
            listOf(
                Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe, distance = 1234),
                Gear(id = "bike-1", name = "Bike 1", type = GearType.Bike, distance = 5678),
            )
        )
    }
}

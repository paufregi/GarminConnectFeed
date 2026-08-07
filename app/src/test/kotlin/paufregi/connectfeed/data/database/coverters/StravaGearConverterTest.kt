package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.database.entities.StravaGearEntity
import paufregi.connectfeed.data.api.strava.models.Gear as StravaGear

class StravaGearConverterTest {

    val stravaGear = StravaGear(
        id = "ID_1",
        name = "strava gear",
    )

    val entityStravaGear = StravaGearEntity(
        id = "ID_1",
        stravaAthleteId = 1,
        name = "strava gear",
        type = GearType.Bike,
    )

    @Test
    fun `StravaGear to entity`() {
        val result = stravaGear.toEntity(type = GearType.Bike, stravaAthleteId = 1)

        assertThat(result).isEqualTo(entityStravaGear)
    }

    @Test
    fun `Entity strava gear to strava gear`() {
        val result = entityStravaGear.toCore()

        assertThat(result).isEqualTo(stravaGear)
    }
}

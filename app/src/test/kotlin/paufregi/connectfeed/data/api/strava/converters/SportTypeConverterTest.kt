package paufregi.connectfeed.data.api.strava.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class SportTypeConverterTest {

    @Test
    fun `To Core activity - alpine ski`() {
        val activityType = SportTypeConverter.toActivityType("AlpineSki")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - backcountry ski`() {
        val activityType = SportTypeConverter.toActivityType("BackcountrySki")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - badminton`() {
        val activityType = SportTypeConverter.toActivityType("Badminton")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - canoeing`() {
        val activityType = SportTypeConverter.toActivityType("Canoeing")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - crossfit`() {
        val activityType = SportTypeConverter.toActivityType("Crossfit")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - eBikeRide`() {
        val activityType = SportTypeConverter.toActivityType("EBikeRide")
        assertThat(activityType).isEqualTo(ActivityType.EBiking)
    }

    @Test
    fun `To Core activity - elliptical`() {
        val activityType = SportTypeConverter.toActivityType("Elliptical")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - eMountainBikeRide`() {
        val activityType = SportTypeConverter.toActivityType("EMountainBikeRide")
        assertThat(activityType).isEqualTo(ActivityType.EBiking)
    }

    @Test
    fun `To Core activity - golf`() {
        val activityType = SportTypeConverter.toActivityType("Golf")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - gravel ride`() {
        val activityType = SportTypeConverter.toActivityType("GravelRide")
        assertThat(activityType).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Core activity - handcycle`() {
        val activityType = SportTypeConverter.toActivityType("Handcycle")
        assertThat(activityType).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Core activity - high intensity interval training`() {
        val activityType = SportTypeConverter.toActivityType("HighIntensityIntervalTraining")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - hike`() {
        val activityType = SportTypeConverter.toActivityType("Hike")
        assertThat(activityType).isEqualTo(ActivityType.Hiking)
    }

    @Test
    fun `To Core activity - ice skate`() {
        val activityType = SportTypeConverter.toActivityType("IceSkate")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - inline skate`() {
        val activityType = SportTypeConverter.toActivityType("InlineSkate")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - kayaking`() {
        val activityType = SportTypeConverter.toActivityType("Kayaking")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - kitesurf`() {
        val activityType = SportTypeConverter.toActivityType("Kitesurf")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - mountain bike ride`() {
        val activityType = SportTypeConverter.toActivityType("MountainBikeRide")
        assertThat(activityType).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Core activity - nordic ski`() {
        val activityType = SportTypeConverter.toActivityType("NordicSki")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - pickleball`() {
        val activityType = SportTypeConverter.toActivityType("Pickleball")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - pilates`() {
        val activityType = SportTypeConverter.toActivityType("Pilates")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - racquetball`() {
        val activityType = SportTypeConverter.toActivityType("Racquetball")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - ride`() {
        val activityType = SportTypeConverter.toActivityType("Ride")
        assertThat(activityType).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Core activity - rock climbing`() {
        val activityType = SportTypeConverter.toActivityType("RockClimbing")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - roller ski`() {
        val activityType = SportTypeConverter.toActivityType("RollerSki")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - rowing`() {
        val activityType = SportTypeConverter.toActivityType("Rowing")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - run`() {
        val activityType = SportTypeConverter.toActivityType("Run")
        assertThat(activityType).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `To Core activity - sail`() {
        val activityType = SportTypeConverter.toActivityType("Sail")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - skateboard`() {
        val activityType = SportTypeConverter.toActivityType("Skateboard")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - snowboard`() {
        val activityType = SportTypeConverter.toActivityType("Snowboard")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - snowshoe`() {
        val activityType = SportTypeConverter.toActivityType("Snowshoe")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - soccer`() {
        val activityType = SportTypeConverter.toActivityType("Soccer")
        assertThat(activityType).isEqualTo(ActivityType.Soccer)
    }

    @Test
    fun `To Core activity - squash`() {
        val activityType = SportTypeConverter.toActivityType("Squash")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - stair stepper`() {
        val activityType = SportTypeConverter.toActivityType("StairStepper")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - stand up paddling`() {
        val activityType = SportTypeConverter.toActivityType("StandUpPaddling")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - surfing`() {
        val activityType = SportTypeConverter.toActivityType("Surfing")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - swim`() {
        val activityType = SportTypeConverter.toActivityType("Swim")
        assertThat(activityType).isEqualTo(ActivityType.Swimming)
    }

    @Test
    fun `To Core activity - table tennis`() {
        val activityType = SportTypeConverter.toActivityType("TableTennis")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - tennis`() {
        val activityType = SportTypeConverter.toActivityType("Tennis")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - trail run`() {
        val activityType = SportTypeConverter.toActivityType("TrailRun")
        assertThat(activityType).isEqualTo(ActivityType.TrailRunning)
    }

    @Test
    fun `To Core activity - velomobile`() {
        val activityType = SportTypeConverter.toActivityType("Velomobile")
        assertThat(activityType).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Core activity - virtual ride`() {
        val activityType = SportTypeConverter.toActivityType("VirtualRide")
        assertThat(activityType).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Core activity - virtual row`() {
        val activityType = SportTypeConverter.toActivityType("VirtualRow")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - virtual run`() {
        val activityType = SportTypeConverter.toActivityType("VirtualRun")
        assertThat(activityType).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `To Core activity - walk`() {
        val activityType = SportTypeConverter.toActivityType("Walk")
        assertThat(activityType).isEqualTo(ActivityType.Walking)
    }

    @Test
    fun `To Core activity - weight training`() {
        val activityType = SportTypeConverter.toActivityType("WeightTraining")
        assertThat(activityType).isEqualTo(ActivityType.Strength)
    }

    @Test
    fun `To Core activity - wheelchair`() {
        val activityType = SportTypeConverter.toActivityType("Wheelchair")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - windsurf`() {
        val activityType = SportTypeConverter.toActivityType("Windsurf")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - workout`() {
        val activityType = SportTypeConverter.toActivityType("Workout")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

    @Test
    fun `To Core activity - yoga`() {
        val activityType = SportTypeConverter.toActivityType("Yoga")
        assertThat(activityType).isEqualTo(ActivityType.Yoga)
    }

    @Test
    fun `To Core activity - unknown`() {
        val activityType = SportTypeConverter.toActivityType("Unknown")
        assertThat(activityType).isEqualTo(ActivityType.Unknown)
    }

}
package paufregi.connectfeed.core.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class FormatterTest {

    val zoneId: ZoneId = ZoneId.of("Pacific/Auckland")

    @Test
    fun `Formatter date time for filename`() {
        val date = Instant.ofEpochMilli(1704057630000) // 2024-01-01 10:20:30
        val result = Formatter.dateTimeForFilename(zoneId).format(date)
        assertThat(result).isEqualTo("20240101_102030")
    }

    @Test
    fun `Formatter date time for importer`() {
        val date = Date.from(Instant.ofEpochMilli(1704057630000))
        val result = Formatter.dateTimeForImport(Locale.ENGLISH).parse("2024.01.01 10:20:30")
        assertThat(result).isEqualTo(date)
    }

    @Test
    fun `Formatter distance - double`() {
        val distance = 1234567.0
        val result = Formatter.distance(distance)
        assertThat(result).isEqualTo("1,234.57")
    }

    @Test
    fun `Formatter distance - integer`() {
        val distance = 1234567
        val result = Formatter.distance(distance)
        assertThat(result).isEqualTo("1,234")
    }

    @Test
    fun `Formatter workout`() {
        val workout = "Workout Of the Day"
        val result = Formatter.workout(workout)
        assertThat(result).isEqualTo("Workout: workout of the day")
    }

    @Test
    fun `Formatter workout - VO2 max`() {
        val workout = "VO2 Max - Zone 5"
        val result = Formatter.workout(workout)
        assertThat(result).isEqualTo("Workout: VO₂ max - zone 5")
    }

    @Test
    fun `Formatter workout - null`() {
        val result = Formatter.workout(null)
        assertThat(result).isNull()
    }

    @Test
    fun `Formatter training effect - sprint`() {
        assertThat(Formatter.trainingEffect("SPEED")).isEqualTo("sprint")
    }

    @Test
    fun `Formatter training effect - anaerobic capacity`() {
        assertThat(Formatter.trainingEffect("ANAEROBIC_CAPACITY")).isEqualTo("anaerobic capacity")
    }

    @Test
    fun `Formatter training effect - VO2 max`() {
        assertThat(Formatter.trainingEffect("VO2MAX")).isEqualTo("VO₂ max")
    }

    @Test
    fun `Formatter training effect - threshold`() {
        assertThat(Formatter.trainingEffect("LACTATE_THRESHOLD")).isEqualTo("threshold")
    }

    @Test
    fun `Formatter training effect - tempo`() {
        assertThat(Formatter.trainingEffect("TEMPO")).isEqualTo("tempo")
    }

    @Test
    fun `Formatter training effect - base`() {
        assertThat(Formatter.trainingEffect("AEROBIC_BASE")).isEqualTo("base")
    }

    @Test
    fun `Formatter training effect - recovery`() {
        assertThat(Formatter.trainingEffect("RECOVERY")).isEqualTo("recovery")
    }

    @Test
    fun `Formatter training effect - null`() {
        assertThat(Formatter.trainingEffect(null)).isNull()
    }

    @Test
    fun `Formatter description`() {
        val description = "Description"
        val trainingEffect = "RECOVERY"
        val workout = "workout"
        val result = Formatter.description(description, trainingEffect, true, workout)
        assertThat(result).isEqualTo("Description\n\nWorkout: workout\nBenefit: recovery")
    }

    @Test
    fun `Formatter description - workout VO2 max`() {
        val description = "Description"
        val trainingEffect = "VO2MAX"
        val workout = "VO2 Max - Zone 5"
        val result = Formatter.description(description, trainingEffect, true, workout)
        assertThat(result).isEqualTo("Description\n\nWorkout: VO₂ max - zone 5\nBenefit: VO₂ max")
    }

    @Test
    fun `Formatter description - no workout`() {
        val description = "Description"
        val trainingEffect = "RECOVERY"
        val result = Formatter.description(description, trainingEffect, true, null)
        assertThat(result).isEqualTo("Description\n\nBenefit: recovery")
    }

    @Test
    fun `Formatter description - no training effect`() {
        val description = "Description"
        val workout = "workout"
        val result = Formatter.description(description, null, true, workout)
        assertThat(result).isEqualTo("Description\n\nWorkout: workout")
    }

    @Test
    fun `Formatter description - no training effect & workout`() {
        val description = "Description"
        val result = Formatter.description(description, null, true, null)
        assertThat(result).isEqualTo("Description")
    }

    @Test
    fun `Formatter description - training effect flag false`() {
        val description = "Description"
        val trainingEffect = "RECOVERY"
        val workout = "Tempo"
        val result = Formatter.description(description, trainingEffect, false, workout)
        assertThat(result).isEqualTo("Description\n\nWorkout: tempo")
    }

    @Test
    fun `Formatter description - no description`() {
        val trainingEffect = "RECOVERY"
        val workout = "Tempo"
        val result = Formatter.description(null, trainingEffect, true, workout)
        assertThat(result).isEqualTo("Workout: tempo\nBenefit: recovery")
    }
}
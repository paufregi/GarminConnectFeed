package paufregi.connectfeed.core.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class FormatterTest {

    val zoneId: ZoneId = ZoneId.of("Pacific/Auckland")

    @Test
    fun `Formatter local date time - today`() {
        val today = LocalDateTime.of(2024, 1, 15, 9, 30)
        val result = Formatter.localDateTime(today, today)

        assertThat(result).isEqualTo("Today 09:30")
    }

    @Test
    fun `Formatter local date time - yesterday`() {
        val today = LocalDateTime.of(2024, 1, 15, 9, 30)
        val yesterday = LocalDateTime.of(2024, 1, 14, 18, 45)

        val result = Formatter.localDateTime(yesterday, today)

        assertThat(result).isEqualTo("Yesterday 18:45")
    }

    @Test
    fun `Formatter local date time - older date`() {
        val today = LocalDateTime.of(2024, 1, 15, 9, 30)
        val older = LocalDateTime.of(2024, 1, 10, 18, 45)

        val result = Formatter.localDateTime(older, today)

        assertThat(result).isEqualTo("10 Jan 2024 18:45")
    }

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
    fun `Formatter garmin description`() {
        val result = Formatter.garminDescription("VO2 Max - Zone 5")

        assertThat(result).isEqualTo("Workout: VO₂ max - zone 5")
    }

    @Test
    fun `Formatter garmin description - null`() {
        val result = Formatter.garminDescription(null)

        assertThat(result).isNull()
    }

    @Test
    fun `Formatter strava description`() {
        val result = Formatter.stravaDescription(
            description = "description",
            trainingEffect = "VO2MAX",
            workout = "VO2 Max - Zone 5",
        )

        assertThat(result).isEqualTo("description\n\nWorkout: VO₂ max - zone 5\nBenefit: VO₂ max")
    }

    @Test
    fun `Formatter strava description - null description`() {
        val result = Formatter.stravaDescription(
            description = null,
            trainingEffect = "VO2MAX",
            workout = "VO2 Max - Zone 5",
        )

        assertThat(result).isEqualTo("Workout: VO₂ max - zone 5\nBenefit: VO₂ max")
    }

    @Test
    fun `Formatter strava description - null training effect`() {
        val result = Formatter.stravaDescription(
            description = "description",
            trainingEffect = null,
            workout = "VO2 Max - Zone 5",
        )

        assertThat(result).isEqualTo("description\n\nWorkout: VO₂ max - zone 5")
    }

    @Test
    fun `Formatter strava description - null workout`() {
        val result = Formatter.stravaDescription(
            description = "description",
            trainingEffect = "VO2MAX",
            workout = null,
        )

        assertThat(result).isEqualTo("description\n\nBenefit: VO₂ max")
    }

    @Test
    fun `Formatter strava description - null description and training effect`() {
        val result = Formatter.stravaDescription(
            description = null,
            trainingEffect = null,
            workout = "VO2 Max - Zone 5",
        )

        assertThat(result).isEqualTo("Workout: VO₂ max - zone 5")
    }

    @Test
    fun `Formatter strava description - null description and workout`() {
        val result = Formatter.stravaDescription(
            description = null,
            trainingEffect = "VO2MAX",
            workout = null,
        )

        assertThat(result).isEqualTo("Benefit: VO₂ max")
    }

    @Test
    fun `Formatter strava description - null training effect and workout`() {
        val result = Formatter.stravaDescription(
            description = "description",
            trainingEffect = null,
            workout = null,
        )

        assertThat(result).isEqualTo("description")
    }

    @Test
    fun `Formatter strava description - all null`() {
        val result = Formatter.stravaDescription(
            description = null,
            trainingEffect = null,
            workout = null,
        )

        assertThat(result).isEqualTo("")
    }


}
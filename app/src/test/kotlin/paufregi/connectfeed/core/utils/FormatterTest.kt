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
    fun `Formatter date time`() {
        val date = Instant.ofEpochMilli(1704057630000) // 2024-01-01 10:20:30
        val result = Formatter.dateTime(zoneId).format(date)
        assertThat(result).isEqualTo("20240101_102030")
    }

    @Test
    fun `Formatter date`() {
        val date = Instant.ofEpochMilli(1704057630000) // 2024-01-01 10:20:30
        val result = Formatter.date(zoneId).format(date)
        assertThat(result).isEqualTo("2024-01-01")
    }

    @Test
    fun `Simple formatter date time`() {
        val date = Date.from(Instant.ofEpochMilli(1704057630000))
        val result = Formatter.simpleDateTime(Locale.ENGLISH).parse("2024.01.01 10:20:30")
        assertThat(result).isEqualTo(date)
    }

    @Test
    fun `Formatter distance`() {
        val distance = 1234567.0
        val result = Formatter.distance(distance)
        assertThat(result).isEqualTo("1234.57")
    }

    @Test
    fun `Formatter description`() {
        val description = "Description"
        val trainingEffect = "recovery"
        val result = Formatter.description(description, trainingEffect, true)
        assertThat(result).isEqualTo("Description\n\nTraining: recovery")
    }

    @Test
    fun `Formatter description - flag false`() {
        val description = "Description"
        val trainingEffect = "recovery"
        val result = Formatter.description(description, trainingEffect, false)
        assertThat(result).isEqualTo("Description")
    }

    @Test
    fun `Formatter description - null description`() {
        val trainingEffect = "recovery"
        val result = Formatter.description(null, trainingEffect, true)
        assertThat(result).isEqualTo("\n\nTraining: recovery")
    }

    @Test
    fun `Formatter description - null training effect`() {
        val description = "Description"
        val result = Formatter.description(description, null, true)
        assertThat(result).isEqualTo("Description")
    }
}
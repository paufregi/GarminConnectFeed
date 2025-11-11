package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType

class ActivityTypeConverterTest {
    private val converter = ActivityTypeConverter()

    @Test
    fun `To Activity type - Any`() {
        val name = converter.toName(ActivityType.Any)
        assertThat(name).isEqualTo(ActivityType.Any::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Any)
    }

    @Test
    fun `To Activity type - Running`() {
        val name = converter.toName(ActivityType.Running)
        assertThat(name).isEqualTo(ActivityType.Running::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Running)
    }

    @Test
    fun `To Activity type - Cycling`() {
        val name = converter.toName(ActivityType.Cycling)
        assertThat(name).isEqualTo(ActivityType.Cycling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Cycling)
    }

    @Test
    fun `To Activity type - Swimming`() {
        val name = converter.toName(ActivityType.Swimming)
        assertThat(name).isEqualTo(ActivityType.Swimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Swimming)
    }

    @Test
    fun `To Activity type - StrengthTraining`() {
        val name = converter.toName(ActivityType.StrengthTraining)
        assertThat(name).isEqualTo(ActivityType.StrengthTraining::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.StrengthTraining)
    }

    @Test
    fun `To Activity type - Fitness`() {
        val name = converter.toName(ActivityType.Fitness)
        assertThat(name).isEqualTo(ActivityType.Fitness::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Fitness)
    }

    @Test
    fun `To Activity type - Other`() {
        val name = converter.toName(ActivityType.Other)
        assertThat(name).isEqualTo(ActivityType.Other::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityType.Other)
    }


    @Test
    fun `To Activity type - invalid name returns unknown`() {
        val type = converter.fromName("nope")
        assertThat(type).isEqualTo(ActivityType.Unknown)
    }
}
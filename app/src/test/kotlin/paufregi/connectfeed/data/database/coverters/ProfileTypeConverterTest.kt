package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ProfileType

class ProfileTypeConverterTest {
    private val converter = ProfileTypeConverter()

    @Test
    fun `To ProfileType - Any`() {
        val name = converter.toName(ProfileType.Any)
        assertThat(name).isEqualTo(ProfileType.Any::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ProfileType.Any)
    }

    @Test
    fun `To ProfileType - Running`() {
        val name = converter.toName(ProfileType.Running)
        assertThat(name).isEqualTo(ProfileType.Running::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ProfileType.Running)
    }

    @Test
    fun `To ProfileType - Cycling`() {
        val name = converter.toName(ProfileType.Cycling)
        assertThat(name).isEqualTo(ProfileType.Cycling::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ProfileType.Cycling)
    }

    @Test
    fun `To ProfileType - Swimming`() {
        val name = converter.toName(ProfileType.Swimming)
        assertThat(name).isEqualTo(ProfileType.Swimming::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ProfileType.Swimming)
    }

    @Test
    fun `To ProfileType - Strength`() {
        val name = converter.toName(ProfileType.Strength)
        assertThat(name).isEqualTo(ProfileType.Strength::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ProfileType.Strength)
    }

    @Test
    fun `To ProfileType - Fitness`() {
        val name = converter.toName(ProfileType.Fitness)
        assertThat(name).isEqualTo(ProfileType.Fitness::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ProfileType.Fitness)
    }

    @Test
    fun `To ProfileType - Other`() {
        val name = converter.toName(ProfileType.Other)
        assertThat(name).isEqualTo(ProfileType.Other::class.simpleName)
        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ProfileType.Any)
    }

    @Test
    fun `To ProfileType - invalid name returns Other`() {
        val type = converter.fromName("nope")
        assertThat(type).isEqualTo(ProfileType.Other)
    }
}


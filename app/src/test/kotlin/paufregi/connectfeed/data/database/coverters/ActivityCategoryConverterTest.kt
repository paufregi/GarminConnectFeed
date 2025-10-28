package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityCategory

class ActivityCategoryConverterTest {

    private val converter = ActivityCategoryConverter()

    @Test
    fun `To Activity category - any`() {
        val name = converter.toName(ActivityCategory.Any)
        assertThat(name).isEqualTo(ActivityCategory.Any.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityCategory.Any)
    }

    @Test
    fun `To Activity category - running`() {
        val name = converter.toName(ActivityCategory.Running)
        assertThat(name).isEqualTo(ActivityCategory.Running.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityCategory.Running)
    }

    @Test
    fun `To Activity category - cycling`() {
        val name = converter.toName(ActivityCategory.Cycling)
        assertThat(name).isEqualTo(ActivityCategory.Cycling.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityCategory.Cycling)
    }

    @Test
    fun `To Activity category - swimming`() {
        val name = converter.toName(ActivityCategory.Swimming)
        assertThat(name).isEqualTo(ActivityCategory.Swimming.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityCategory.Swimming)
    }

    @Test
    fun `To Activity category - strength`() {
        val name = converter.toName(ActivityCategory.Strength)
        assertThat(name).isEqualTo(ActivityCategory.Strength.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityCategory.Strength)
    }

    @Test
    fun `To Activity type - other`() {
        val name = converter.toName(ActivityCategory.Other)
        assertThat(name).isEqualTo(ActivityCategory.Other.name)

        val type = converter.fromName(name)
        assertThat(type).isEqualTo(ActivityCategory.Other)
    }

    @Test
    fun `To Activity type - nope`() {
        val type = converter.fromName("nope")
        assertThat(type).isEqualTo(ActivityCategory.Other)
    }
}
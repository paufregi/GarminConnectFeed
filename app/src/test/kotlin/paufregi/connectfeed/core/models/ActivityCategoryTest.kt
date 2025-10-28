package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ActivityCategoryTest {

    @Test
    fun `Compatible with Running`() {
        val category = ActivityCategory.Running

        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Cycling.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }
    }

    @Test
    fun `Compatible with Cycling`() {
        val category = ActivityCategory.Cycling

        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Cycling.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }
    }

    @Test
    fun `Compatible with Swimming`() {
        val category = ActivityCategory.Swimming

        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Cycling.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }
    }

    @Test
    fun `Compatible with Strength`() {
        val category = ActivityCategory.Strength

        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Cycling.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }
    }

    @Test
    fun `Compatible with Fitness`() {
        val category = ActivityCategory.Fitness

        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Cycling.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }
    }

    @Test
    fun `Compatible with Other`() {
        val category = ActivityCategory.Other

        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Cycling.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isFalse()
        }

        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }
    }

    @Test
    fun `Compatible with Any`() {
        val category = ActivityCategory.Any

        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Cycling.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }

        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(category.compatibleWith(activity)).isTrue()
        }
    }

    @Test
    fun `Find category Running`() {
        ActivityCategory.Running.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(ActivityCategory.findCategory(activity)).isEqualTo(ActivityCategory.Running)
        }
    }

    @Test
    fun `Find category Swimming`() {
        ActivityCategory.Swimming.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(ActivityCategory.findCategory(activity)).isEqualTo(ActivityCategory.Swimming)
        }
    }

    @Test
    fun `Find category Strength`() {
        ActivityCategory.Strength.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(ActivityCategory.findCategory(activity)).isEqualTo(ActivityCategory.Strength)
        }
    }

    @Test
    fun `Find category Fitness`() {
        ActivityCategory.Fitness.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(ActivityCategory.findCategory(activity)).isEqualTo(ActivityCategory.Fitness)
        }
    }

    @Test
    fun `Find category Other`() {
        ActivityCategory.Other.types.forEach {
            val activity = Activity(1, "Activity", it)
            assertThat(ActivityCategory.findCategory(activity)).isEqualTo(ActivityCategory.Other)
        }
    }
}

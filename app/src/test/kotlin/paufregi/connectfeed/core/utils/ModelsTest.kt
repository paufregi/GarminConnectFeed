package paufregi.connectfeed.core.utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import java.time.Instant

class ModelsTest {

    @Test
    fun `Activity - getOrMatch - get`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val activity2 = Activity(
            id = 10,
            name = "otherActivity",
            type = ActivityType.Running,
        )

        val pool = emptyList<Activity>()

        val result = activity1.getOrMatch(activity2, pool)
        assertThat(result).isEqualTo(activity1)
    }

    @Test
    fun `Activity - getOrMatch - matching`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val activity2 = Activity(
            id = 10,
            name = "otherActivity",
            type = ActivityType.Cycling,
            date = Instant.parse("2025-01-01T10:00:00Z"),
        )

        val matching = Activity(
            id = 2,
            name = "activity2",
            type = ActivityType.Cycling,
            date = Instant.parse("2025-01-01T10:00:00Z"),
        )

        val pool = listOf(activity1, matching)

        val result = activity1.getOrMatch(activity2, pool)
        assertThat(result).isEqualTo(matching)
    }

    @Test
    fun `Activity - getOrMatch - no match`() = runTest {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val activity2 = Activity(
            id = 10,
            name = "otherActivity",
            type = ActivityType.Cycling,
            date = Instant.parse("2025-01-01T10:00:00Z"),
        )

        val pool = listOf(
            activity1,
            Activity(
                id = 2,
                name = "activity2",
                type = ActivityType.Running,
                date = Instant.parse("2025-01-01T10:00:00Z"),
            )
        )

        val result = activity1.getOrMatch(activity2, pool)
        assertThat(result).isNull()
    }

    @Test
    fun `Activity - getOrMatch - null - matching`() {
        val activity1 = null

        val activity2 = Activity(
            id = 10,
            name = "otherActivity",
            type = ActivityType.Cycling,
            date = Instant.parse("2025-01-01T10:00:00Z"),
        )

        val matching = Activity(
            id = 2,
            name = "activity2",
            type = ActivityType.Cycling,
            date = Instant.parse("2025-01-01T10:00:00Z"),
        )

        val pool = listOf(matching)

        val result = activity1.getOrMatch(activity2, pool)
        assertThat(result).isEqualTo(matching)
    }

    @Test
    fun `Activity - getOrMatch - null - no match`() {
        val activity1 = null

        val activity2 = Activity(
            id = 10,
            name = "otherActivity",
            type = ActivityType.Cycling,
            date = Instant.parse("2025-01-01T10:00:00Z"),
        )

        val pool = listOf(
            Activity(
                id = 2,
                name = "activity2",
                type = ActivityType.Running,
                date = Instant.parse("2025-01-01T10:00:00Z"),
            )
        )

        val result = activity1.getOrMatch(activity2, pool)
        assertThat(result).isNull()
    }

    @Test
    fun `Profile - getOrNull - get`() {
        val profile = Profile(
            name = "profile",
            activityType = ActivityType.Running,
        )

        val activity = Activity(
            id = 1,
            name = "activity",
            type = ActivityType.Running,
        )

        val result = profile.getOrNull(activity)
        assertThat(result).isEqualTo(profile)
    }

    @Test
    fun `Profile - getOrNull - no match`() {
        val profile = Profile(
            name = "profile",
            activityType = ActivityType.Cycling,
        )

        val activity = Activity(
            id = 10,
            name = "activity",
            type = ActivityType.Running,
        )

        val result = profile.getOrNull(activity)
        assertThat(result).isNull()
    }

    @Test
    fun `Profile - getOrNull - null`() = runTest {
        val profile: Profile? = null

        val activity = Activity(
            id = 10,
            name = "otherActivity",
            type = ActivityType.Cycling,
        )

        val result = profile.getOrNull(activity)
        assertThat(result).isNull()
    }

    @Test
    fun `Course - getOrNull activity - get`() {
        val course = Course(
            id = 1,
            name = "course",
            distance = 1.0,
            type = ActivityType.Running,
        )

        val activity = Activity(
            id = 1,
            name = "activity",
            type = ActivityType.Running,
        )

        val result = course.getOrNull(activity)
        assertThat(result).isEqualTo(course)
    }

    @Test
    fun `Course - getOrNull activity - no match`() {
        val course = Course(
            id = 1,
            name = "course",
            distance = 1.0,
            type = ActivityType.Cycling,
        )

        val activity = Activity(
            id = 10,
            name = "activity",
            type = ActivityType.Running,
        )

        val result = course.getOrNull(activity)
        assertThat(result).isNull()
    }

    @Test
    fun `Course - getOrNull activity - null`() = runTest {
        val course: Course? = null

        val activity = Activity(
            id = 10,
            name = "otherActivity",
            type = ActivityType.Cycling,
        )

        val result = course.getOrNull(activity)
        assertThat(result).isNull()
    }

    @Test
    fun `Course - getOrNull type - get`() {
        val course = Course(
            id = 1,
            name = "course",
            distance = 1.0,
            type = ActivityType.Running,
        )

        val result = course.getOrNull(ActivityType.Running)
        assertThat(result).isEqualTo(course)
    }

    @Test
    fun `Course - getOrNull type - no match`() {
        val course = Course(
            id = 1,
            name = "course",
            distance = 1.0,
            type = ActivityType.Cycling,
        )

        val result = course.getOrNull(ActivityType.Running)
        assertThat(result).isNull()
    }

    @Test
    fun `Course - getOrNull type - null`() = runTest {
        val course: Course? = null

        val result = course.getOrNull(ActivityType.Cycling)
        assertThat(result).isNull()
    }
}
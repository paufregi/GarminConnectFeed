package paufregi.connectfeed.core.utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.Profile
import retrofit2.Response
import java.time.Instant
import java.util.Date
import java.util.concurrent.Semaphore

class ExtensionTest {

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
    fun `Activity - getOrNull - get`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val result = activity1.getOrNull(ActivityType.Running)
        assertThat(result).isEqualTo(activity1)
    }

    @Test
    fun `Activity - getOrNull - no match`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val result = activity1.getOrNull(ActivityType.Cycling)
        assertThat(result).isNull()
    }

    @Test
    fun `Activity - getOrNull - null`() = runTest {
        val activity: Activity? = null

        val result = activity.getOrNull(ActivityType.Running)
        assertThat(result).isNull()
    }

    @Test
    fun `Activity - getOrNull profile - get`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val profile = Profile(
            name = "profile",
            activityType = ActivityType.Running,
        )

        val result = activity1.getOrNull(profile)
        assertThat(result).isEqualTo(activity1)
    }

    @Test
    fun `Activity - getOrNull profile - no match`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val profile = Profile(
            name = "profile",
            activityType = ActivityType.Cycling,
        )

        val result = activity1.getOrNull(profile)
        assertThat(result).isNull()
    }

    @Test
    fun `Activity - getOrNull profile - null`() = runTest {
        val activity: Activity? = null

        val profile = Profile(
            name = "profile",
            activityType = ActivityType.Running,
        )

        val result = activity.getOrNull(profile)
        assertThat(result).isNull()
    }

    @Test
    fun `Activity - getOrNull course - get`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val course = Course(
            id = 1,
            name = "course",
            distance = 1.0,
            type = ActivityType.Running,
        )

        val result = activity1.getOrNull(course)
        assertThat(result).isEqualTo(activity1)
    }

    @Test
    fun `Activity - getOrNull course - no match`() {
        val activity1 = Activity(
            id = 1,
            name = "activity1",
            type = ActivityType.Running,
        )

        val course = Course(
            id = 1,
            name = "course",
            distance = 1.0,
            type = ActivityType.Cycling,
        )

        val result = activity1.getOrNull(course)
        assertThat(result).isNull()
    }

    @Test
    fun `Activity - getOrNull course - null`() = runTest {
        val activity: Activity? = null

        val course = Course(
            id = 1,
            name = "course",
            distance = 1.0,
            type = ActivityType.Cycling,
        )
        val result = activity.getOrNull(course)
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
    fun `Course - getOrNull - get`() {
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
    fun `Course - getOrNull - no match`() {
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
    fun `Course - getOrNull - null`() = runTest {
        val course: Course? = null

        val result = course.getOrNull(ActivityType.Cycling)
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
    fun `Float - getOrNull - get`() {
        val value = 1.0f

        val result = value.getOrNull()
        assertThat(result).isEqualTo(value)
    }

    @Test
    fun `Float - getOrNull - 0f`() {
        val value = 0f

        val result = value.getOrNull()
        assertThat(result).isNull()
    }

    @Test
    fun `Float - getOrNull - null`() {
        val value: Float? = null

        val result = value.getOrNull()
        assertThat(result).isNull()
    }

    @Test
    fun `Date - sameDay - same day`() {
        val date = Date.from(Instant.parse("2025-01-01T10:20:30Z"))
        val other = Date.from(Instant.parse("2025-01-01T10:22:33Z"))

        val result = date.sameDay(other)
        assertThat(result).isTrue()
    }

    @Test
    fun `Date - sameDay - different day`() {
        val date = Date.from(Instant.parse("2025-01-01T10:20:30Z"))
        val other = Date.from(Instant.parse("2025-01-03T10:20:30Z"))

        val result = date.sameDay(other)
        assertThat(result).isFalse()
    }

    @Test
    fun `Response - toResult - success`() {
        val resp = Response.success("Success")

        val result = resp.toResult()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("Success")
    }

    @Test
    fun `Response - toResult - success unit`() {
        val resp = Response.success(Unit)

        val result = resp.toResult()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(Unit)
    }

    @Test
    fun `Response - toResult - failure`() {
        val resp = Response.error<String>(500, "error".toResponseBody())

        val result = resp.toResult()

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("error")
    }

    @Test
    fun `Response - toResult with empty - success`() {
        val resp = Response.success<String>("body")

        val result = resp.toResult("empty")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("body")
    }

    @Test
    fun `Response - toResult with empty - success with null body`() {
        val resp = Response.success<String>(null)

        val result = resp.toResult("empty")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("empty")
    }

    @Test
    fun `Response - toResult with empty - failure`() {
        val resp = Response.error<String>(500, "error".toResponseBody())

        val result = resp.toResult()

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("error")
    }

    @Test
    fun `Result - failure`() {
        val result = Result.failure<String>("error")

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("error")
    }

    @Test
    fun `Result - mapFailure - success`() {
        val result = Result.success("ok").mapFailure { Exception("new error") }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.exceptionOrNull()?.message).isNull()
    }

    @Test
    fun `Result - mapFailure - failure`() {
        val result = Result.failure<String>("error").mapFailure { Exception("new error") }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("new error")
    }

    @Test
    fun `Result - mapOrFailure - success`() {
        val result = Result.success("input").mapOrFailure { it.uppercase() }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo("INPUT")
    }

    @Test
    fun `Result - mapOrFailure - success with null transform`() {
        val result = Result.success("input").mapOrFailure { null }
        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Transformation returned null")
    }

    @Test
    fun `Result - mapOrFailure - failure`() {
        val result = Result.failure<String>(Exception("fail")).mapOrFailure { it.uppercase() }
        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("fail")
    }

    @Test
    fun `runCatchingResult - success`() {
        val block: () -> Result<String> = { Result.success("Success") }

        val result = runCatchingResult { block() }

        assertThat(result.isSuccess).isTrue()
        assertThat(result).isEqualTo(Result.success("Success"))
    }

    @Test
    fun `runCatchingResult - failure`() {
        val block: () -> Result<String> = { Result.failure<String>("Fail") }

        val result = runCatchingResult { block() }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Fail")
    }

    @Test
    fun `runCatchingResult - exception`() {
        val block: () -> Result<String> = { throw Exception("Fail") }

        val result = runCatchingResult { block() }

        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Fail")
    }

    @Test
    fun `Semaphore - withPermit - executes action within permit`() {
        val semaphore = Semaphore(1)
        var result = 0

        semaphore.withPermit { result = 42 }

        assertThat(result).isEqualTo(42)
    }

    @Test
    fun `Semaphore - withPermit - blocks when no permits are available`() = runTest {
        val semaphore = Semaphore(1)
        var actionExecuted = false

        coroutineScope {
            semaphore.acquire()
            val action = async { semaphore.withPermit { actionExecuted = true } }

            assertThat(actionExecuted).isFalse()
            semaphore.release()
            action.await()
            assertThat(actionExecuted).isTrue()
        }
    }
}
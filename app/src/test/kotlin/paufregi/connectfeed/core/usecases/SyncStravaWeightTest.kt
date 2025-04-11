package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.data.repository.GarminRepository
import java.time.Instant
import java.util.Date

class SyncStravaWeightTest {
    @Rule
    @JvmField
    var folder: TemporaryFolder = TemporaryFolder()

    private val repo = mockk<GarminRepository>()
    private val isStravaLoggedIn = mockk<IsStravaLoggedIn>()
    private lateinit var useCase: SyncStravaWeight

    @Before
    fun setup(){
        useCase = SyncStravaWeight(repo, isStravaLoggedIn)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Sync weight`() = runTest {
        every { isStravaLoggedIn.invoke() } returns flowOf(true)

        val date = Date.from(Instant.parse("2024-01-02T10:20:30Z"))
        val previousDate = Date.from(Instant.parse("2024-01-01T10:20:30Z"))

        val weights = listOf(
            Weight(
                timestamp = date,
                weight = 76.15f,
                bmi = 23.8f,
                fat = 23.2f,
                visceralFat = 7,
                water = 55.4f,
                muscle = 55.59f,
                bone = 2.89f,
                basalMet = 1618f,
                metabolicAge = 35
            ),
            Weight(
                timestamp = previousDate,
                weight = 77.15f,
                bmi = 23.8f,
                fat = 23.2f,
                visceralFat = 7,
                water = 55.4f,
                muscle = 55.59f,
                bone = 2.89f,
                basalMet = 1618f,
                metabolicAge = 35,
            )
        )

        coEvery { repo.updateStravaProfile(any()) } returns Result.Success(Unit)

        val res = useCase(weights, date)

        assertThat(res.isSuccessful).isTrue()

        verify { isStravaLoggedIn.invoke() }
        coVerify { repo.updateStravaProfile(any()) }
        confirmVerified(repo, isStravaLoggedIn)
    }

    @Test
    fun `Sync weight - no Strava`() = runTest {
        every { isStravaLoggedIn.invoke() } returns flowOf(false)

        val date = Date.from(Instant.parse("2024-01-02T10:20:30Z"))
        val previousDate = Date.from(Instant.parse("2024-01-01T10:20:30Z"))

        val weights = listOf(
            Weight(
                timestamp = date,
                weight = 76.15f,
                bmi = 23.8f,
                fat = 23.2f,
                visceralFat = 7,
                water = 55.4f,
                muscle = 55.59f,
                bone = 2.89f,
                basalMet = 1618f,
                metabolicAge = 35
            ),
            Weight(
                timestamp = previousDate,
                weight = 77.15f,
                bmi = 23.8f,
                fat = 23.2f,
                visceralFat = 7,
                water = 55.4f,
                muscle = 55.59f,
                bone = 2.89f,
                basalMet = 1618f,
                metabolicAge = 35,
            )
        )

        val res = useCase(weights, date)

        assertThat(res.isSuccessful).isTrue()

        verify { isStravaLoggedIn.invoke() }
        confirmVerified(repo, isStravaLoggedIn)
    }

    @Test
    fun `Sync weight - past date`() = runTest {
        every { isStravaLoggedIn.invoke() } returns flowOf(true)
        val date = Date.from(Instant.parse("2024-01-02T10:20:30Z"))
        val previousDate = Date.from(Instant.parse("2024-01-01T10:20:30Z"))

        val weights = listOf(Weight(
            timestamp = previousDate,
            weight = 76.15f,
            bmi = 23.8f,
            fat = 23.2f,
            visceralFat = 7,
            water = 55.4f,
            muscle = 55.59f,
            bone = 2.89f,
            basalMet = 1618f,
            metabolicAge = 35,
        ))

        coEvery { repo.updateStravaProfile(any()) } returns Result.Success(Unit)

        val res = useCase(weights, date)

        assertThat(res.isSuccessful).isTrue()

        verify { isStravaLoggedIn.invoke() }
        confirmVerified(repo, isStravaLoggedIn)
    }

    @Test
    fun `Sync weight - empty list`() = runTest {
        every { isStravaLoggedIn.invoke() } returns flowOf(true)
        val date = Date.from(Instant.parse("2024-01-02T10:20:30Z"))

        val res = useCase(emptyList<Weight>(), date)

        assertThat(res.isSuccessful).isTrue()
        verify { isStravaLoggedIn.invoke() }
        confirmVerified(repo, isStravaLoggedIn)
    }

    @Test
    fun `Sync weight - failure`() = runTest {
        every { isStravaLoggedIn.invoke() } returns flowOf(true)
        val date = Date.from(Instant.parse("2024-01-02T10:20:30Z"))

        val weights = listOf(
            Weight(
                timestamp = date,
                weight = 76.15f,
                bmi = 23.8f,
                fat = 23.2f,
                visceralFat = 7,
                water = 55.4f,
                muscle = 55.59f,
                bone = 2.89f,
                basalMet = 1618f,
                metabolicAge = 35
            )
        )

        coEvery { repo.updateStravaProfile(any()) } returns Result.Failure("error")

        val res = useCase(weights, date)

        assertThat(res.isSuccessful).isFalse()

        coVerify { repo.updateStravaProfile(any()) }
        verify { isStravaLoggedIn.invoke() }
        confirmVerified(repo, isStravaLoggedIn)
    }
}

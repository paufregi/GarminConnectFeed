package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.data.repository.GarminRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class SyncStravaWeightTest {
    @Rule
    @JvmField
    var folder: TemporaryFolder = TemporaryFolder()

    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SyncStravaWeight

    @Before
    fun setup(){
        mockkObject(RenphoReader)

        useCase = SyncStravaWeight(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
        unmockkObject(RenphoReader)
    }

    @Test
    fun `Sync weight`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-02 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
            2024-01-01 10:20:30,77.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val stubInputStream = IOUtils.toInputStream(csvText, "UTF-8")

        val date = Instant.parse("2024-01-02T10:20:30Z")
        val previousDate = date.minus(1, ChronoUnit.DAYS)

        val weights = listOf(
            Weight(
                timestamp = Date.from(date),
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
                timestamp = Date.from(previousDate),
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

        every { RenphoReader.read(any()) } returns weights
        coEvery { repo.updateStravaProfile(any()) } returns Result.Success(Unit)

        val res = useCase(stubInputStream, date)

        assertThat(res.isSuccessful).isTrue()

        verify { RenphoReader.read(stubInputStream) }
        coVerify { repo.updateStravaProfile(any()) }
        confirmVerified(repo, RenphoReader)
    }

    @Test
    fun `Sync weight - past date`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val stubInputStream = IOUtils.toInputStream(csvText, "UTF-8")

        val date = Instant.parse("2024-01-02T10:20:30Z")
        val previousDate = Instant.parse("2024-01-01T10:20:30Z")

        val weights = listOf(Weight(
            timestamp = Date.from(previousDate),
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

        every { RenphoReader.read(any()) } returns weights
        coEvery { repo.updateStravaProfile(any()) } returns Result.Success(Unit)

        val res = useCase(stubInputStream, date)

        assertThat(res.isSuccessful).isTrue()

        verify { RenphoReader.read(stubInputStream) }
        confirmVerified(repo, RenphoReader)
    }

    @Test
    fun `Sync weight - empty file`() = runTest {
        val csvText = ""
        val stubInputStream = IOUtils.toInputStream(csvText, "UTF-8")
        val date = Instant.parse("2024-01-02T10:20:30Z")

        every { RenphoReader.read(any()) } returns emptyList()

        val res = useCase(stubInputStream, date)

        assertThat(res.isSuccessful).isTrue()
        verify { RenphoReader.read(stubInputStream) }
        confirmVerified(repo, RenphoReader)
    }

    @Test
    fun `Sync weight - failure`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val stubInputStream = IOUtils.toInputStream(csvText, "UTF-8")

        val date = Instant.parse("2024-01-01T10:20:30Z")

        val weights = listOf(
            Weight(
                timestamp = Date.from(date),
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

        every { RenphoReader.read(any()) } returns weights
        coEvery { repo.updateStravaProfile(any()) } returns Result.Failure("error")

        val res = useCase(stubInputStream, date)

        assertThat(res.isSuccessful).isFalse()

        verify { RenphoReader.read(stubInputStream) }
        coVerify { repo.updateStravaProfile(any()) }
        confirmVerified(repo, RenphoReader)
    }
}

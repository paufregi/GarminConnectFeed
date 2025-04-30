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
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.utils.FitWriter
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository
import java.time.Instant
import java.util.Date


class SyncWeightTest {
    @Rule
    @JvmField
    var folder: TemporaryFolder = TemporaryFolder()

    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: SyncWeight

    @Before
    fun setup(){
        mockkObject(Formatter)
        mockkObject(FitWriter)

        useCase = SyncWeight(repo, folder.newFolder())
    }

    @After
    fun tearDown(){
        clearAllMocks()
        unmockkObject(Formatter::class)
        unmockkObject(FitWriter::class)
    }

    @Test
    fun `Upload file`() = runTest {
        val weights = listOf(Weight(
            timestamp = Date.from(Instant.ofEpochMilli(1704057630000)),
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

        every { Formatter.dateTimeForFilename(any()).format(any()) } returns "20240101_000000"
        every { FitWriter.weights(any(), any()) } returns Unit
        coEvery { repo.uploadFile(any()) } returns Result.success(Unit)

        val res = useCase(weights)

        assertThat(res.isSuccess).isTrue()
        verify {
            Formatter.dateTimeForFilename(any()).format(any())
            FitWriter.weights(any(), weights)
        }
        coVerify { repo.uploadFile(any()) }
        confirmVerified(repo, Formatter, FitWriter)
    }

    @Test
    fun `Upload empty list`() = runTest {
        every { Formatter.dateTimeForFilename(any()).format(any()) } returns "20240101_000000"
        every { FitWriter.weights(any(), any()) } returns Unit
        coEvery { repo.uploadFile(any()) } returns Result.success(Unit)

        val res = useCase(emptyList<Weight>())

        assertThat(res.isSuccess).isTrue()
        verify {
            Formatter.dateTimeForFilename(any()).format(any())
            FitWriter.weights(any(), emptyList())
        }
        coVerify { repo.uploadFile(any()) }
        confirmVerified(repo, Formatter, FitWriter)
    }

    @Test
    fun `Failed upload`() = runTest {
        every { Formatter.dateTimeForFilename(any()).format(any()) } returns "20240101_000000"
        every { FitWriter.weights(any(), any()) } returns Unit
        coEvery { repo.uploadFile(any()) } returns Result.failure("Failed to upload file")

        val weights = listOf(Weight(
            timestamp = Date.from(Instant.ofEpochMilli(1704057630000)),
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

        val res = useCase(weights)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Failed to upload file")
        verify {
            Formatter.dateTimeForFilename(any()).format(any())
            FitWriter.weights(any(), weights)
        }
        coVerify { repo.uploadFile(any()) }
        confirmVerified(repo, Formatter, FitWriter)
    }
}

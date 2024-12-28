package paufregi.connectfeed.core.usecases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.repository.GarminRepository

class GetUserTest{
    private val repo = mockk<GarminRepository>()
    private lateinit var useCase: GetUser

    @Before
    fun setup(){
        useCase = GetUser(repo)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get user`() = runTest {
        val user = User("user", "profilImage")

        coEvery { repo.getUser() } returns flowOf(user)

        val res = useCase()

        res.test {
            assertThat(awaitItem()).isEqualTo(user)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repo.getUser() }
        confirmVerified(repo)
    }

    @Test
    fun `No user`() = runTest {
        coEvery { repo.getUser() } returns flowOf(null)

        val res = useCase()

        res.test {
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { repo.getUser() }
        confirmVerified(repo)
    }
}
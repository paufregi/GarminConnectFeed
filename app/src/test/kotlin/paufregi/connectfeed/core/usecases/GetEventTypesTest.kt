package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.EventType

class GetEventTypesTest {

    private lateinit var useCase: GetEventTypes

    @Before
    fun setup(){
        useCase = GetEventTypes()
    }

    @After
    fun tearDown(){
    }

    @Test
    fun `Get event types`() = runTest {
        val res = useCase()

        assertThat(res).containsExactly(
            EventType.Race,
            EventType.Recreation,
            EventType.SpecialEvent,
            EventType.Training,
            EventType.Transportation,
            EventType.Touring,
            EventType.Geocaching,
            EventType.Fitness,
            EventType.Uncategorized,
        )
    }
}
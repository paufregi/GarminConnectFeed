package paufregi.connectfeed.core.usecases

import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import paufregi.connectfeed.data.repository.StravaAuthRepository

class StravaCodeExchangeTest{
    private val repo = mockk<StravaAuthRepository>()
    private lateinit var useCase: StravaCodeExchange

    @Before
    fun setup(){
        useCase = StravaCodeExchange(repo, "clientId", "clientSecret")
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

}
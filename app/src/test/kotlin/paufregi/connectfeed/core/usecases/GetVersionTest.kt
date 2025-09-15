package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import paufregi.connectfeed.core.models.Version

class GetVersionTest{
    private lateinit var useCase: GetVersion

    @Test
    fun `Get version`() = runTest {
        useCase = GetVersion("1.0.0")

        val version = useCase()

        assertThat(version).isNotNull()
        assertThat(version).isEqualTo(Version(1, 0 ,0))
    }

    @Test
    fun `Invalid version`() = runTest {
        useCase = GetVersion("invalid")

        val version = useCase()

        assertThat(version).isNull()
    }
}
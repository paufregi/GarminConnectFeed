package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class VersionTest {

    @Test
    fun `To string`() {
        val version = Version(1, 2, 3)
        assertThat(version.toString()).isEqualTo("v1.2.3")
    }

    @Test
    fun `Parse - valid`() {
        val version = Version.parse("1.2.3")
        val expectation = Version(1, 2, 3)

        assertThat(version).isNotNull()
        assertThat(version?.major).isEqualTo(expectation.major)
        assertThat(version?.minor).isEqualTo(expectation.minor)
        assertThat(version?.patch).isEqualTo(expectation.patch)
    }

    @Test
    fun `Parse - valid with prefix`() {
        val version = Version.parse("v1.2.3")
        val expectation = Version(1, 2, 3)

        assertThat(version).isNotNull()
        assertThat(version?.major).isEqualTo(expectation.major)
        assertThat(version?.minor).isEqualTo(expectation.minor)
        assertThat(version?.patch).isEqualTo(expectation.patch)
    }

    @Test
    fun `Parse - fail invalid`() {
        val version = Version.parse("NOPE")

        assertThat(version).isNull()
    }

    @Test
    fun `Parse - fail too many parts`() {
        val version = Version.parse("v1.2.3.4")

        assertThat(version).isNull()
    }

    @Test
    fun `Parse - fail invalid major`() {
        val version = Version.parse("vA.2.3")

        assertThat(version).isNull()
    }

    @Test
    fun `Parse - fail invalid minor`() {
        val version = Version.parse("v1.A.3")

        assertThat(version).isNull()
    }

    @Test
    fun `Parse - fail invalid patch`() {
        val version = Version.parse("v1.2.A")

        assertThat(version).isNull()
    }
}
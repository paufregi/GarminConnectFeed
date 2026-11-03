package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Release
import paufregi.connectfeed.core.models.Version
import paufregi.connectfeed.data.api.github.models.AssetDto
import paufregi.connectfeed.data.api.github.models.ReleaseDto

class ReleaseConverterTest {

    @Test
    fun `DTO to core`() {
        val dto = ReleaseDto(
            tagName = "v1.2.3",
            assets = listOf(
                AssetDto(
                    contentType = "application/vnd.android.package-archive",
                    downloadUrl = "https://example.com/app.apk"
                )
            )
        )

        val release = Release(
            version = Version(1, 2, 3),
            downloadUrl = "https://example.com/app.apk"
        )

        assertThat(dto.toCore()).isEqualTo(release)
    }

    @Test
    fun `DTO to core - invalid version`() {
        val dto = ReleaseDto(
            tagName = "not-a-version",
            assets = listOf(
                AssetDto(
                    contentType = "application/vnd.android.package-archive",
                    downloadUrl = "https://example.com/app.apk"
                )
            )
        )

        assertThat(dto.toCore()).isNull()
    }

    @Test
    fun `DTO to core - invalid contentType`() {
        val dto = ReleaseDto(
            tagName = "v1.2.3",
            assets = listOf(
                AssetDto(
                    contentType = "application/octet-stream",
                    downloadUrl = "https://example.com/app.apk"
                )
            )
        )

        assertThat(dto.toCore()).isNull()
    }

    @Test
    fun `DTO to core - invalid url`() {
        val dto = ReleaseDto(
            tagName = "v1.2.3",
            assets = listOf(
                AssetDto(
                    contentType = "application/vnd.android.package-archive",
                    downloadUrl = "https://example.com/app.zip"
                )
            )
        )

        assertThat(dto.toCore()).isNull()
    }

    @Test
    fun `DTO to core - no assets`() {
        val dto = ReleaseDto(
            tagName = "v1.2.3",
            assets = emptyList()
        )

        assertThat(dto.toCore()).isNull()
    }
}

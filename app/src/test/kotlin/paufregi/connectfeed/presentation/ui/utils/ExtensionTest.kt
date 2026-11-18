package paufregi.connectfeed.presentation.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExtensionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `PaddingValues - add horizontal and vertical padding`() {
        var result: PaddingValues? = null

        composeTestRule.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                result = PaddingValues(
                    start = 1.dp,
                    top = 2.dp,
                    end = 3.dp,
                    bottom = 4.dp,
                ).add(horizontal = 5.dp, vertical = 10.dp)
            }
        }

        val padding = checkNotNull(result)
        assertThat(padding.calculateStartPadding(LayoutDirection.Ltr)).isEqualTo(6.dp)
        assertThat(padding.calculateEndPadding(LayoutDirection.Ltr)).isEqualTo(8.dp)
        assertThat(padding.calculateTopPadding()).isEqualTo(12.dp)
        assertThat(padding.calculateBottomPadding()).isEqualTo(14.dp)
    }

    @Test
    fun `PaddingValues - add uses RTL layout direction`() {
        var result: PaddingValues? = null

        composeTestRule.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                result = PaddingValues(
                    start = 1.dp,
                    top = 2.dp,
                    end = 3.dp,
                    bottom = 4.dp,
                ).add(horizontal = 5.dp, vertical = 10.dp)
            }
        }

        val padding = checkNotNull(result)
        assertThat(padding.calculateStartPadding(LayoutDirection.Rtl)).isEqualTo(6.dp)
        assertThat(padding.calculateEndPadding(LayoutDirection.Rtl)).isEqualTo(8.dp)
        assertThat(padding.calculateTopPadding()).isEqualTo(12.dp)
        assertThat(padding.calculateBottomPadding()).isEqualTo(14.dp)
    }
}
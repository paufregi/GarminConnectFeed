package paufregi.connectfeed.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NavigatorTest {

    private fun backStack(vararg routes: Route): MutableList<NavKey> =
        mutableStateListOf<NavKey>().apply { addAll(routes) }

    @Test
    fun `navigate pushes route`() {
        val stack = backStack(Route.QuickEdit)
        val nav = Navigator(stack)

        nav.navigate(Route.Profiles)

        assertThat(stack).containsExactly(Route.QuickEdit, Route.Profiles).inOrder()
    }

    @Test
    fun `navigate ignores same top route`() {
        val stack = backStack(Route.QuickEdit)
        val nav = Navigator(stack)

        nav.navigate(Route.QuickEdit)

        assertThat(stack).containsExactly(Route.QuickEdit)
    }

    @Test
    fun `navigate with argument pushes new entry`() {
        val stack = backStack(Route.Profiles)
        val nav = Navigator(stack)

        nav.navigate(Route.Profile(1))
        nav.navigate(Route.Profile(2))

        assertThat(stack).containsExactly(Route.Profiles, Route.Profile(1), Route.Profile(2)).inOrder()
    }

    @Test
    fun `navigateUp pops top route`() {
        val stack = backStack(Route.Profiles, Route.Profile(1))
        val nav = Navigator(stack)

        nav.navigateUp()

        assertThat(stack).containsExactly(Route.Profiles)
    }

    @Test
    fun `navigateUp keeps root route`() {
        val stack = backStack(Route.QuickEdit)
        val nav = Navigator(stack)

        nav.navigateUp()

        assertThat(stack).containsExactly(Route.QuickEdit)
    }
}


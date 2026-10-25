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

    @Test
    fun `navigateTopLevel from root places destination above root`() {
        val stack = backStack(Route.QuickEdit)
        val nav = Navigator(stack)

        nav.navigateTopLevel(Route.Profiles)

        assertThat(stack).containsExactly(Route.QuickEdit, Route.Profiles).inOrder()
    }

    @Test
    fun `navigateTopLevel does not grow the stack when toggling destinations`() {
        val stack = backStack(Route.QuickEdit)
        val nav = Navigator(stack)

        nav.navigateTopLevel(Route.Profiles)
        nav.navigateTopLevel(Route.Gears)
        nav.navigateTopLevel(Route.Settings)
        nav.navigateTopLevel(Route.Profiles)

        assertThat(stack).containsExactly(Route.QuickEdit, Route.Profiles).inOrder()
    }

    @Test
    fun `navigateTopLevel to root resets stack to root only`() {
        val stack = backStack(Route.QuickEdit, Route.Profiles, Route.Profile(1))
        val nav = Navigator(stack)

        nav.navigateTopLevel(Route.QuickEdit)

        assertThat(stack).containsExactly(Route.QuickEdit)
    }

    @Test
    fun `navigateTopLevel discards detail entries`() {
        val stack = backStack(Route.QuickEdit, Route.Profiles, Route.Profile(1))
        val nav = Navigator(stack)

        nav.navigateTopLevel(Route.Gears)

        assertThat(stack).containsExactly(Route.QuickEdit, Route.Gears).inOrder()
    }

    @Test
    fun `navigateTopLevel is idempotent on current destination`() {
        val stack = backStack(Route.QuickEdit, Route.Profiles)
        val nav = Navigator(stack)

        nav.navigateTopLevel(Route.Profiles)

        assertThat(stack).containsExactly(Route.QuickEdit, Route.Profiles).inOrder()
    }

    @Test
    fun `navigateTopLevel on empty stack adds destination`() {
        val stack = backStack()
        val nav = Navigator(stack)

        nav.navigateTopLevel(Route.Gears)

        assertThat(stack).containsExactly(Route.Gears)
    }

    @Test
    fun `back from top-level destination returns to root`() {
        val stack = backStack(Route.QuickEdit)
        val nav = Navigator(stack)

        nav.navigateTopLevel(Route.Settings)
        nav.navigateUp()

        assertThat(stack).containsExactly(Route.QuickEdit)
    }
}


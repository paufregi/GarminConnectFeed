# Copilot Instructions for ConnectFeed

Use these instructions when generating or modifying code in this repository.

## Project Context

- App: ConnectFeed (Android, Kotlin)
- Purpose: Sync Renpho Health data to Garmin Connect, plus Garmin/Strava activity editing via profiles
- UI: Jetpack Compose
- DI: Hilt
- Local persistence: Room (`GarminDatabase`, `GarminDao`, `ProfileEntity`)
- Network: Retrofit APIs for Garmin, Strava, and GitHub
- State handling: `StateFlow` in ViewModels, commonly `stateIn` with `SharingStarted.WhileSubscribed`

## Architecture Guidance

- Keep presentation code in `app/src/main/kotlin/paufregi/connectfeed/presentation/` by feature.
- Preserve feature structure (`Screen`, `ViewModel`, state/action classes).
- Keep business logic in `core/usecases/`.
- Keep external/local data access in repositories under `data/repository/`.
- Use Hilt modules in `di/` for wiring dependencies.

## Code Generation Conventions

- Follow existing Kotlin style and naming in nearby files.
- Prefer small, focused changes that fit existing architecture.
- Reuse existing models and sealed types instead of introducing parallel structures.
- For navigation changes, extend sealed `Route` usage patterns already present.
- For process/result UI states, use `ProcessState` (`Idle`, `Processing`, `Success`, `Failure`).

## Profiles and Activity Types

- Profiles are user-scoped and persisted through `ProfileEntity`.
- When adding profile fields:
  - Update `ProfileEntity`
  - Update profile and quick edit UI/ViewModel logic
  - Add/adjust Room migration/schema as needed
- Activity compatibility logic relies on `core/models/ActivityType.kt`; extend there first when adding types.

## Data and Security

- Do not hardcode secrets.
- Keep key/secret usage aligned with `local.properties` + `buildConfigField` setup.
- Respect dependency version constraints/substitution configured in `app/build.gradle.kts`.

## Testing Expectations

- Add or update tests for behavior changes.
- Prefer existing test patterns:
  - Unit tests: `./gradlew test`
  - Instrumented tests: `./gradlew pixel10Check`
  - Compose UI tests with Hilt where relevant (`@HiltAndroidTest`)
- Keep tests near the feature/module being changed (`app/src/test` or `app/src/androidTest`).

### ViewModel Unit Test Standard

Use `SettingsViewModelTest` as the baseline for structure and tone. The current
`EditViewModelTest` and `QuickEditViewModelTest` demonstrate seeded state and
compatibility guards. Tests live under
`app/src/test/kotlin/paufregi/connectfeed/presentation/` in the matching feature package.

#### Structure and naming

- Use JUnit4, MockK, Truth assertions, Turbine, `runTest`, and the existing
  `MainDispatcherRule`. Follow the nearby coroutine opt-in convention.
- Declare mocks and reusable, synthetic fixtures as class properties, with
  `private lateinit var viewModel` and a **mandatory** `private fun createViewModel()`
  helper. Instantiate the ViewModel explicitly in each test.
- `@Before fun setup()` must contain only invariant mock configuration: stubs that
  return the same result throughout the suite. Put scenario-specific stubs inside
  their tests. Do not construct the ViewModel, seed state, or invoke actions in setup.
- `@After fun tearDown()` must call `confirmVerified(...)` with **all** mocks,
  including getters and relaxed mocks, followed by `clearAllMocks()`. Include
  object mocks in verification and restore them when applicable.
- Avoid additional helper functions, especially assertion helpers or shared
  scenario-stubbing helpers. Keep arrange/act/assert steps visible inside each test.
- Use descriptive backtick names: `Initial state`, `Initial state - no strava`,
  `Set name`, `Set profile - null`, `Set gear - incompatible`, `Save - success`,
  `Save - failure`, and `Save - failure without message`.

#### Scope and state seeding

- Organize tests around the initial state and its meaningful variations, followed
  by `onAction` behavior. Cover each action separately: invoke **one action once**
  per action test, rather than driving several actions to prepare another action.
  If there is no `onAction`, test the other public functions and exposed flows.
- Test genuine startup variations through dependency stubs, such as connected vs
  disconnected users, missing data, empty lists, and loading/failure states.
- When an action needs pre-existing edited state, add an internal function to the
  ViewModel that accepts its state type and only replaces the backing state:

  ```kotlin
  internal fun injectState(state: FeatureState) {
      _state.value = state
  }
  ```

- Use `injectState` in those tests instead of reflection, extra constructor
  parameters, or a sequence of setup actions. Subscribe to the exposed state,
  consume initial loading as needed, inject the state, and consume/assert the
  seeded emission before invoking the action. Finish startup loading first so it
  cannot overwrite the seed.
- Remember that StateFlow suppresses equal values. Ensure the seed differs from
  the current state before awaiting its emission, and seed a non-loading status
  when the action is expected to emit a new loading transition.

#### Assertions and mock verification

- Use `state.test { ... }` and explicit inline Truth assertions on emitted state.
  Assert relevant fields and preservation of unrelated seeded values where useful.
  Finish subscriptions with `cancelAndIgnoreRemainingEvents()`.
- Use `CompletableDeferred` to control suspended operations and assert the actual
  loading/processing transition before completing success or failure. Do not rely
  on sleeps, arbitrary delays, or fragile counts of transient emissions.
- Cover success, failure, null-message fallbacks, nullable inputs, reset behavior,
  and missing-data guards where supported. Assert notifications and navigation
  effects as well as state, including the absence of effects when appropriate.
- For incompatible course, gear, or profile actions that are intentionally ignored,
  seed a valid existing selection, invoke the incompatible action, then assert
  `expectNoEvents()` and that `viewModel.state.value` still equals the seeded
  emitted state. Do not wait for an emission from a no-op.
- Verify every actual dependency interaction, including invariant getter calls
  from initialization. Use `verify` for ordinary functions and `coVerify` for
  suspend functions. **Each verification block must contain exactly one function
  call**; never group several calls in a single block:

  ```kotlin
  verify { getUser() }
  verify { isStravaConnected() }
  coVerify { getLatestRelease() }
  coVerify { refreshUser() }
  verify { notification.show("User data refreshed") }
  ```

- Keep `confirmVerified` strict; do not bypass it with broad verification,
  unnecessary relaxed mocks, or cleared call histories.

## Practical Editing Rules

- Avoid broad refactors unless requested.
- Preserve existing public behavior unless task explicitly changes it.
- Keep API changes minimal and backward-compatible when possible.
- If uncertain, follow patterns from nearest existing implementation in the same feature folder.

## Code Review

Detailed, project-specific review criteria (architecture rules, security checks, Room migration
requirements, test coverage expectations, dependency conventions) are in
`.github/copilot-review-instructions.md`. Apply those rules when reviewing pull requests.


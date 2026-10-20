# AGENTS.md

Instructions for autonomous AI coding agents working in this repository.
Full GitHub Copilot guidance lives in:
- `.github/copilot-instructions.md` — code generation conventions
- `.github/copilot-review-instructions.md` — PR review criteria

---

## Project Snapshot

**ConnectFeed** — Android/Kotlin app that syncs Renpho Health body-composition data
to Garmin Connect, and lets users edit Garmin/Strava activities via custom profiles.

| Stack element | Detail |
|---|---|
| Language | Kotlin, JVM 17 |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt 2.60.1 |
| Local storage | Room 2.8.4 (`GarminDatabase` / `GarminDao` / `ProfileEntity`) |
| Network | Retrofit 3 + OkHttp 5 |
| State | `StateFlow` + `stateIn(SharingStarted.WhileSubscribed)` |
| UI process state | `ProcessState` sealed class (`Idle`, `Processing`, `Success`, `Failure`) |
| Navigation | Compose Navigation, sealed `Route` objects |
| Auth | Garmin OAuth 1.0a (OkHttp Signpost), Strava OAuth 2.0 |
| Tests (unit) | JUnit4 + MockK + Turbine + Truth |
| Tests (instrumented) | `@HiltAndroidTest` Compose tests, custom runner `paufregi.connectfeed.TestRunner` |

---

## Source Layout

```
app/src/main/kotlin/paufregi/connectfeed/
  ConnectFeed.kt            # Application entrypoint; runs startup cleanup
  presentation/
    main/                   # MainActivity, MainViewModel, app-level NavHost
    activities/             # Feature screens (Screen, ViewModel, State, Action)
    gears/
    login/
    profiles/
    settings/
    strava/
    syncweight/
    ui/                     # Shared Compose components, icons, theme, models, utils
  core/
    usecases/               # Business logic only — no Android/Compose imports
    models/                 # Domain models (ActivityType, Profile, …)
    utils/                  # Shared helpers used by domain/use-case code
  data/
    repository/             # Mediate between use cases ↔ APIs/DB
    api/                    # Retrofit interfaces (garmin/, strava/, github/)
    database/               # Room DB, DAO, entities, migrations
    datastore/              # DataStore (AuthStore, StravaStore)
    utils/                  # App-level helpers (security/update support)
  di/                       # Hilt modules
  system/                   # Startup/update helpers (CleanUp, Downloader, Installer)
```

**Layer rule** (never skip): `presentation` → `core/usecases` → `data/repository` → `data/api|database|datastore`

---

## Mandatory Validation After Any Change

Run in the same order as CI. Fix all failures before considering the task done.

```shell
# 1. Full build including release shrinking
./gradlew assemble

# 2. Compile + unit tests (always run)
./gradlew test

# 3. Instrumented UI/integration tests (run when touching UI, Room, or Hilt wiring)
./gradlew pixel10Check

# 4. Lint
./gradlew lint
```

Tests live in `app/src/test/` (unit) and `app/src/androidTest/` (instrumented).

---

## Model Unit Test Standard

Follow the full **Model Unit Test Standard** in `.github/copilot-instructions.md`.
Use Course/Profile/Gear tests for compatibility, ActivityType/GearType tests for
type rules, and Version tests for parsing, formatting, and comparison examples.

- Place `<Model>Test` in `app/src/test/kotlin/paufregi/connectfeed/core/models/`
  with the matching package. Use plain JUnit4 and inline Truth assertions.
- Use real model objects and deterministic synthetic fixtures, including fixed
  dates. Share immutable baseline fixtures and vary relevant fields with `copy`.
- Pure synchronous model tests need no mocks, setup/teardown, factory/assertion
  helpers, coroutine runner, dispatcher rule, Turbine, or Hilt. ViewModel-specific
  test infrastructure requirements do not apply.
- Name tests by behavior and variation, such as `compatible - same type`,
  `Parse - fail invalid`, or `IsLowerThan - lower minor`.
- Test one logical scenario category at a time; multiple explicit assertions for
  representative inputs are encouraged. Assert outcomes directly, not via helpers
  that recompute production logic.
- Cover behavior rather than generated data-class plumbing. Compatibility coverage
  includes positive/negative cases, parent/subtype directions, sibling/unrelated
  types, and valid wildcard cases on both sides.
- Respect model-specific rules, including bike/shoe/unknown gear differences.
  Exercise model wrappers with real `Activity` values; type-level tests alone are
  insufficient. Cover meaningful defaults through observable behavior.
- For parsing, cover valid/prefixed and malformed/component-invalid inputs. For
  comparisons, cover equality and lower/higher components. Assert exact formatting
  output and parsed component values.

---

## ViewModel Unit Test Standard

Follow the full **ViewModel Unit Test Standard** in
`.github/copilot-instructions.md`. Use `SettingsViewModelTest` as the baseline and
the current Edit/QuickEdit suites for seeded state and compatibility guards.

- Use JUnit4 + MockK + Truth + Turbine, `runTest`, and `MainDispatcherRule`.
- Every suite must have a `createViewModel()` helper; create the ViewModel in each
  test. Avoid other helpers, especially assertion and scenario-stubbing helpers.
- `setup()` contains only invariant mock configuration. Keep varying results and
  scenario setup inside individual tests.
- `tearDown()` must call `confirmVerified` with **all** mocks, including getters,
  relaxed mocks, and object mocks, then `clearAllMocks()`. Restore object mocks
  when applicable.
- Name tests `Initial state`, `Initial state - <variation>`, or
  `<Action name> - <variation>` (omit the suffix for a simple action).
- Cover initial-state variations and each `onAction` action in isolation, with one
  action invocation per test. If `onAction` is absent, test public functions and
  exposed flows instead.
- Seed pre-existing edited state with
  `internal fun injectState(state: FeatureState) { _state.value = state }`.
  This function only replaces backing state. Do not use reflection or setup-action
  sequences. Subscribe, finish startup loading, inject, and consume/assert the
  seeded emission before invoking the action.
- Account for StateFlow equality suppression: do not await unchanged state, and
  seed a non-loading status before expecting a loading transition.
- Assert state inline. Use `CompletableDeferred` for async loading/success/failure
  transitions; assert notifications/navigation and relevant fallback/guard cases.
- For an incompatible selection that is ignored, seed a compatible selection,
  invoke the incompatible action, and assert no emission and unchanged state.
- Verify all actual mock calls, including initialization getters. Each `verify`
  or `coVerify` block must verify **exactly one function call**. Use `coVerify` for
  suspend functions. Never weaken `confirmVerified` to hide unverified calls.

---

## Critical Rules

### Never do
- Hardcode API keys/secrets — use `local.properties` + `buildConfigField` in `app/build.gradle.kts`.
- Import `data/` classes directly from `presentation/` — route through a use case.
- Call Retrofit interfaces or Room DAO from a `ViewModel` — repositories and use cases mediate.
- Add a library with a raw version string in `build.gradle.kts` — use `gradle/libs.versions.toml` catalog aliases.
- Use `!!` (non-null assertion) — prefer safe calls, `requireNotNull`, or `?.let`.
- Use `GlobalScope` — use `viewModelScope` or a test coroutine scope.
- Log tokens, user credentials, or body-composition values.

### Always do
- Add a **Room migration** and update the schema JSON in `app/schemas/` when changing any `@Entity`.
- Use `ProcessState` for result-carrying UI state.
- Keep navigation changes in `Navigation.kt` using sealed `Route` objects — no string-literal routes.
- Wire new injectable classes in a Hilt module under `di/`; use `@HiltViewModel` for ViewModels.
- Add or update tests for every behaviour change (use case unit test + ViewModel state test + screen smoke test).
- Use `MockK` for mocking in tests, not Mockito.

---

## Common Extension Recipes

### Add a new Activity Type
1. Extend `ActivityType` in `core/models/ActivityType.kt`.
2. Ensure all `when(activityType)` expressions remain exhaustive (compiler enforces this).
3. Update Room TypeConverters if the type is persisted.
4. Adjust UI dropdowns in affected screens.

### Add a new Profile Field
1. Add the field to `ProfileEntity` in `data/database/`.
2. Write a Room migration and update the schema JSON in `app/schemas/`.
3. Update profile UI + ViewModel in `presentation/profiles/`.
4. Update any gear or activity screens that surface profile-derived values (for example `presentation/gears/`).

### Add a new Screen
1. Create `presentation/<feature>/` with `Screen.kt`, `ViewModel.kt`, `State.kt`, `Action.kt` (and `StatePreview.kt` if you add previews).
2. Add a `Route` object in `presentation/Navigation.kt`.
3. Register the composable in the `NavHost` inside `presentation/main/MainActivity.kt`.
4. Wire the ViewModel with `@HiltViewModel`; add new use cases to the appropriate `di/` module.
5. If the screen belongs in the drawer, update `Navigation.topItems` / `Navigation.bottomItems` in `presentation/Navigation.kt`.
6. Add a `@HiltAndroidTest` smoke test in `app/src/androidTest/`.

### Add a new API Endpoint
- **Garmin**: add to the Retrofit interface in `data/api/garmin/`; all requests must go through the OAuth 1.0a signed pipeline.
- **Strava**: add to `data/api/strava/`; use the authenticated `StravaApi` interface with token-refresh support.
- **GitHub**: used only for update checks — avoid calls in hot paths (rate-limited, unauthenticated).
- All endpoints must use `suspend` functions; no callbacks.

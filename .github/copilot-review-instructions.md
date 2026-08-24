# Copilot Code Review Instructions — ConnectFeed

These guidelines are used by the Copilot code-review agent to give context-aware,
project-tailored feedback on pull requests in this repository.

---

## Project at a Glance

| Aspect | Detail |
|---|---|
| Language | Kotlin (JVM 17, `kotlinPlugin 2.4.10`) |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt 2.60.1 |
| Local storage | Room 2.8.4 (`GarminDatabase`, `GarminDao`, `ProfileEntity`) |
| Network | Retrofit 3 / OkHttp 5 (Garmin OAuth1, Strava OAuth2) |
| State | `StateFlow` + `stateIn(SharingStarted.WhileSubscribed)` |
| Navigation | Compose Navigation, sealed `Route` objects |
| UI process state | `ProcessState` sealed class (`Idle`, `Processing`, `Success`, `Failure`) |
| Tests (unit) | JUnit4 + MockK + Turbine + Truth (`./gradlew test`) |
| Tests (UI/instrumented) | `@HiltAndroidTest` Compose tests (`./gradlew pixel9ProCheck`) |

---

## Architecture Rules — Flag Violations

### Layer boundaries
- **Presentation** (`presentation/<feature>/`) must not import from `data/` directly.
  Use cases (`core/usecases/`) are the only bridge between presentation and data.
- **Use cases** (`core/usecases/`) contain business logic only; they must not depend
  on Android framework classes or Compose types.
- **Repositories** (`data/repository/`) are the only classes that talk to APIs
  (`data/api/`) or the Room DAO (`data/database/`).
- **DataStore** (`data/datastore/`) is accessed exclusively through repositories,
  never directly from ViewModels or use cases.

### Feature structure — each feature folder must have:
```
presentation/<feature>/
  ├── <Feature>Screen.kt      (Compose UI)
  ├── <Feature>ViewModel.kt   (state + actions, no business logic)
  ├── <Feature>State.kt       (immutable data class)
  └── <Feature>Action.kt      (sealed class / event type)
```
Flag any ViewModel that reaches out to APIs/DAOs directly or contains decision logic
that belongs in a use case.

---

## Kotlin / Compose Patterns — Flag Anti-Patterns

### Kotlin
- Prefer `data class` with immutable `val` fields for state/model objects.
- Use `sealed class` / `sealed interface` for result/state hierarchies (not `enum`
  when variants carry data).
- Avoid `!!` (non-null assertion); use safe calls, `requireNotNull`, or `?.let`.
- Avoid mutable shared state; all mutations go through `MutableStateFlow` inside a
  `ViewModel`.
- Suspend functions should be called from `viewModelScope` or test coroutine scopes,
  not from `GlobalScope`.

### Jetpack Compose
- Composables should be stateless; lift state to the `ViewModel` or a parent
  composable.
- Large composables (> ~100 lines) should be split into smaller `@Composable` helpers.
- `LaunchedEffect` and `SideEffect` should have correct keys to avoid unintended
  recompositions.
- Color/typography must come from `MaterialTheme`; hard-coded `Color(0xFF…)` values
  in UI code should use the existing theme tokens instead.
- Preview composables (`@Preview`) should use the app theme wrapper so they render
  with the correct tokens.

### Hilt
- New injectable classes must be wired in an appropriate module inside `di/`.
- Do not use `@Inject` on `ViewModel` constructors directly — use `@HiltViewModel`.
- Avoid `@Singleton` on objects that hold mutable UI state.

---

## Room / Database — Always Check

- **Every schema change to `ProfileEntity` (or any `@Entity`) requires a Room
  migration.** Verify a corresponding migration exists in the database class and that
  the schema JSON in `app/schemas/` has been updated.
- TypeConverters must be registered on `@Database`; flag any new custom type that
  lacks a converter.
- DAO methods returning `Flow` must be in the Room DAO, not faked in the repository.
- Avoid `@Query("SELECT *")` without a `WHERE` clause on large tables; prefer
  targeted queries.

---

## Security — Always Flag

- **No secrets, API keys, or tokens in source code.** All credentials must be loaded
  from `local.properties` via `buildConfigField` in `app/build.gradle.kts`.
- OAuth tokens must be persisted through `AuthStore` / `StravaStore` (DataStore),
  not in SharedPreferences or plain files.
- Do not log sensitive fields (tokens, user credentials, body-composition values).
- Retrofit clients that call external APIs should use HTTPS endpoints only.

---

## Testing — Coverage Expectations

- **New use cases** must have unit tests covering happy path + at least one failure path.
- **New ViewModel actions** should have unit tests using `Turbine` to assert state
  transitions.
- **New Compose screens** should have at least a smoke test (`@HiltAndroidTest`) that
  verifies the screen renders without crashing.
- Tests should use `MockK` for mocking, not Mockito; check that existing `mockk {}`
  patterns are followed.
- Instrumented tests use a custom `TestRunner` (`paufregi.connectfeed.TestRunner`);
  new tests must be compatible with this runner.
- Flag any PR that deletes tests without a clear justification.

---

## Dependencies — Always Check

- New libraries must be added to `gradle/libs.versions.toml` using the version
  catalog pattern (`[versions]`, `[libraries]`, `[plugins]`).
- Do **not** hardcode version strings in `app/build.gradle.kts` when a catalog alias
  exists.
- Check for duplicate dependencies already provided transitively (OkHttp, Kotlin
  stdlib, etc.).
- The project uses `dependencySubstitution` in `build.gradle.kts`; new dependencies
  that conflict with substituted versions should be flagged.

---

## Activity Types & Profiles — Domain-Specific Checks

- Changes to `ActivityType` (in `core/models/ActivityType.kt`) ripple through
  compatibility logic. Verify that all `when` expressions that `ActivityType` are
  exhaustive after any addition.
- Profile field additions must update: `ProfileEntity`, the profile UI/ViewModel, the
  quick-edit UI/ViewModel, and include a Room migration.
- The `ProfileEntity` `userId` field links profiles to a specific Garmin user; new
  profile operations must respect this scoping.

---

## API / Network Clients

- **Garmin API** uses OAuth 1.0a (OkHttp Signpost); any new endpoint must respect
  the signed request pipeline.
- **Strava API** uses OAuth 2.0 with token refresh; new Strava calls must go through
  the authenticated `StravaApi` Retrofit interface.
- **GitHub API** is used for update checks; it is unauthenticated and rate-limited —
  do not add calls in hot paths.
- Retrofit interfaces should use `suspend` functions; callback-based approaches are
  not consistent with the codebase.

---

## General Quality Checks

- Public functions and classes should have KDoc comments when their purpose is not
  immediately obvious from naming.
- Avoid deeply nested lambdas; prefer named functions or `run`/`let` blocks with
  clear intent.
- PRs touching `Navigation.kt` must keep the sealed `Route` pattern; do not
  introduce string-literal routes.
- Avoid `Thread.sleep()` or blocking calls on the main thread; use coroutine delays
  or `Dispatchers.IO` appropriately.
- Release builds use ProGuard (`proguard-rules.pro`); new reflection-based or
  serialization classes may need `-keep` rules.


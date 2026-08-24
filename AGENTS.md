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
  presentation/<feature>/   # Screen, ViewModel, State, Action per feature
  core/usecases/            # Business logic only — no Android/Compose imports
  core/models/              # Domain models (ActivityType, Profile, …)
  data/repository/          # Mediate between use cases ↔ APIs/DB
  data/api/                 # Retrofit interfaces (garmin/, strava/, github/)
  data/database/            # Room DB, DAO, entities, migrations
  data/datastore/           # DataStore (AuthStore, StravaStore)
  di/                       # Hilt modules
```

**Layer rule** (never skip): `presentation` → `core/usecases` → `data/repository` → `data/api|database|datastore`

---

## Mandatory Validation After Any Change

Run in order. Fix all failures before considering the task done.

```shell
# 1. Compile + unit tests (always run)
./gradlew test

# 2. Full build including release shrinking
./gradlew assemble

# 3. Lint
./gradlew lint

# 4. Instrumented UI/integration tests (run when touching UI, Room, or Hilt wiring)
./gradlew pixel9ProCheck
```

Tests live in `app/src/test/` (unit) and `app/src/androidTest/` (instrumented).

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
3. Update profile UI + ViewModel (`presentation/profile/`, `presentation/profiles/`).
4. Update quick-edit UI + ViewModel (`presentation/quickedit/`).

### Add a new Screen
1. Create `presentation/<feature>/` with `Screen.kt`, `ViewModel.kt`, `State.kt`, `Action.kt`.
2. Add a `Route` object in `Navigation.kt`.
3. Register the composable in the `NavHost` inside `Navigation.kt`.
4. Wire the ViewModel with `@HiltViewModel`; add new use cases to the appropriate `di/` module.
5. Add a `@HiltAndroidTest` smoke test in `app/src/androidTest/`.

### Add a new API Endpoint
- **Garmin**: add to the Retrofit interface in `data/api/garmin/`; all requests must go through the OAuth 1.0a signed pipeline.
- **Strava**: add to `data/api/strava/`; use the authenticated `StravaApi` interface with token-refresh support.
- **GitHub**: used only for update checks — avoid calls in hot paths (rate-limited, unauthenticated).
- All endpoints must use `suspend` functions; no callbacks.


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
  - Instrumented tests: `./gradlew pixel9ProCheck`
  - Compose UI tests with Hilt where relevant (`@HiltAndroidTest`)
- Keep tests near the feature/module being changed (`app/src/test` or `app/src/androidTest`).

## Practical Editing Rules

- Avoid broad refactors unless requested.
- Preserve existing public behavior unless task explicitly changes it.
- Keep API changes minimal and backward-compatible when possible.
- If uncertain, follow patterns from nearest existing implementation in the same feature folder.



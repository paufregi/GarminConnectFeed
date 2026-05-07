# AGENTS.md

## Project Overview

**ConnectFeed** is an Android app for synchronizing Renpho Health data with Garmin Connect and Strava, and for editing activity details using custom profiles. The codebase is Kotlin, using Jetpack Compose, Hilt for DI, Room for local storage, and Retrofit for network APIs.

## Architecture & Key Components

- **Presentation Layer**: Compose-based UI in `app/src/main/kotlin/paufregi/connectfeed/presentation/`. Main navigation is in `MainActivity.kt` and `Navigation.kt`. Each feature (e.g., quick edit, profiles, settings) has its own subdirectory with `Screen`, `ViewModel`, and state/action classes.
- **Domain/Use Cases**: Business logic is in `core/usecases/` (e.g., `IsLoggedIn`, `GetActivities`).
- **Data Layer**:
  - **Repositories**: In `data/repository/`, mediate between use cases and data sources.
  - **APIs**: Retrofit interfaces in `data/api/` for Garmin, Strava, and Github.
  - **Local Storage**: Room database (`data/database/`), with `ProfileEntity` as the main entity. Preferences/DataStore for auth and user data (`data/datastore/`).
- **Dependency Injection**: Hilt modules in `di/` and `@HiltAndroidApp` in `ConnectFeed.kt`.

## Developer Workflows

- **Build**: `./gradlew build`
- **Unit Tests**: `./gradlew test`
- **Instrumented Tests**: `./gradlew pixel9ProCheck` (uses managed device config in `build.gradle.kts`)
- **Test Entry Point**: Instrumented tests use custom runner `paufregi.connectfeed.TestRunner`.
- **Test Patterns**: Instrumented tests (e.g., `MainActivityTest.kt`) use Compose Test APIs and Hilt for setup/teardown.

## Project-Specific Conventions

- **Profiles**: Profiles are stored in Room (`ProfileEntity`), keyed by user, and support custom fields (water, feel/effort, etc.).
- **Activity Types**: Rich hierarchy in `core/models/ActivityType.kt` for compatibility checks between Garmin/Strava types.
- **Navigation**: Uses Compose Navigation with sealed `Route` objects.
- **State Management**: ViewModels expose state as `StateFlow`, often using `stateIn` and `SharingStarted.WhileSubscribed`.
- **Error Handling**: Uses `ProcessState` sealed class for UI state (Idle, Processing, Success, Failure).
- **API Substitution**: `build.gradle.kts` enforces dependency versions via `dependencySubstitution`.
- **Sensitive Data**: API keys/secrets are loaded from `local.properties` and injected via `buildConfigField`.

## Integration Points

- **Garmin/Strava APIs**: See `data/api/garmin/` and `data/api/strava/` for endpoints and auth flows.
- **DataStore**: Used for persisting user/auth tokens (`AuthStore`, `StravaStore`).
- **Room**: All profile data is persisted locally and accessed via `GarminDao`.
- **Hilt**: All major components are injected; tests use `@HiltAndroidTest`.

## Key Files/Directories

- `app/src/main/kotlin/paufregi/connectfeed/presentation/` — UI and navigation
- `app/src/main/kotlin/paufregi/connectfeed/core/usecases/` — Business logic
- `app/src/main/kotlin/paufregi/connectfeed/data/repository/` — Data access
- `app/src/main/kotlin/paufregi/connectfeed/data/api/` — Network APIs
- `app/src/main/kotlin/paufregi/connectfeed/data/database/` — Room DB
- `app/src/main/kotlin/paufregi/connectfeed/data/datastore/` — DataStore
- `app/build.gradle.kts` — Build config, managed device test setup
- `README.md` — Feature and workflow summary

## Example: Adding a New Activity Type
- Extend `ActivityType` in `core/models/ActivityType.kt`
- Update Room converters if needed
- Adjust UI dropdowns and compatibility logic in relevant screens

## Example: Adding a New Profile Field
- Add field to `ProfileEntity`
- Update UI and ViewModel logic in `profiles/` and `quickedit/`
- Migrate DB schema if necessary


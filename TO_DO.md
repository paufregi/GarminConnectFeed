# Audit matrix: unit test coverage by package

Legend:
- **Missing** = no meaningful unit test exists
- **Partial** = unit test exists, but important branches/angles are uncovered
- **Good** = test exists and covers most meaningful logic paths
- **Low priority** = likely not worth unit testing unless logic grows

---

## `paufregi.connectfeed.presentation.app`

### Missing tests

| Symbol | File | Status | Why it matters |
|---|---|---:|---|
| `AppViewModel` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/AppViewModel.kt` | Missing | Maps `GetUser` flow into app auth state; simple but central startup behavior |
| `ActivitiesViewModel` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/activities/list/ActivitiesViewModel.kt` | Missing | Loads activities, handles failure notification, toggles loading |
| `EditViewModel` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/activities/edit/EditViewModel.kt` | Missing | One of the highest-value stateful screens; many actions and async save/load paths |
| `QuickEditViewModel` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/activities/quickedit/QuickEditViewModel.kt` | Missing | Important quick-edit flow with profile/workout/gear behavior |
| `ProfilesViewModel` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/profiles/list/ProfilesViewModel.kt` | Missing | Deletes profiles and merges state with profile flow |
| `ProfileViewModel` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/profiles/edit/ProfileViewModel.kt` | Missing | Non-trivial editing logic including course/type compatibility and value parsing |

### Low-priority / likely not worth unit tests
| Symbol | File | Status | Notes |
|---|---|---:|---|
| `Navigation` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/Navigation.kt` | Low priority | Only worth testing if route parsing/building has custom logic |
| `AppState` | `app/src/main/kotlin/paufregi/connectfeed/presentation/app/AppState.kt` | Low priority | Usually sealed/data-only |
| Compose screens / previews / actions / effects / states | various under `presentation/app/...` | Low priority | Most should be UI/instrumentation or snapshot-style coverage instead |

---

## `paufregi.connectfeed.presentation.app.login`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `LoginViewModel` | `app/src/test/kotlin/paufregi/connectfeed/presentation/app/login/LoginViewModelTest.kt` | Partial |

### Useful additions
- Test `LoginAction.SaveUser` success path
- Test `LoginAction.SaveUser` when `state.user == null`
- Assert intermediate `Status.Loading` before success/failure
- Test failure fallback when throwable message is null
- Test reset after a successful sign-in
- Test sign-in overwrites previous error/success state cleanly

### Coverage note
`LoginViewModel` itself is small, but one branch is completely untested:
- `saveUserAction()` in `LoginViewModel.kt:44-46`

---

## `paufregi.connectfeed.presentation.app.gears`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `GearsViewModel` | `app/src/test/kotlin/paufregi/connectfeed/presentation/app/gears/GearsViewModelTest.kt` | Partial |

### Useful additions
- Replace the near-duplicate `Sync gears - processing` / `Sync gears - success` split with a more distinct edge case
- Test null-message fallback for sync failure (`"Sync failed"`)
- Test multiple emissions from `getGears()` flow
- Test repeated sync invocation behavior
- Verify notifications are sent only after completion

### Coverage note
The current suite is decent, but slightly redundant and misses reactive-flow behavior.

---

## `paufregi.connectfeed.presentation.app.settings`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `SettingsViewModel` | `app/src/test/kotlin/paufregi/connectfeed/presentation/app/settings/SettingsViewModelTest.kt` | Partial |
| `SettingsState` | `app/src/test/kotlin/paufregi/connectfeed/presentation/app/settings/SettingsStateTest.kt` | Partial |

### Useful additions for `SettingsViewModel`
- Test `SettingsAction.Reset`
- Test `Update` when `latestRelease == null`
- Test `downloadApk(...)` failure handling
- Assert `updating = true` during initial `load()`
- Test refresh failure with null throwable message
- Test whether `signOut()` / `disconnectStrava()` failure behavior is acceptable

### Useful additions for `SettingsState`
`SettingsState.kt` contains:
- `hasUpdate = latestRelease?.version?.let { currentVersion?.isLowerThan(it) } ?: false`

Current tests only cover:
- lower-than => `true`
- equal => `false`

Add:
- `latestRelease == null` => `false`
- `currentVersion == null` => `false`
- current version greater than latest => `false`
- minor/patch comparison cases if you want to validate delegated comparison expectations

### Coverage note
`SettingsViewModelTest` is pretty solid, but it stops short of exposing an important behavior in `updateAction()`:
- `updating` is set to `true`
- never reset to `false` in the ViewModel

That may be intentional, but the current tests don’t clarify it.

---

## `paufregi.connectfeed.presentation.strava`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `StravaViewModel` | `app/src/test/kotlin/paufregi/connectfeed/presentation/strava/StravaViewModelTest.kt` | Partial |

### Useful additions
- Assert explicit `ProcState.Running` emission when `exchangeToken()` starts
- Test null-message fallback (`"unknown error"`)
- Test `user` flow exposed by `val user = getUser().stateIn(...)`
- Test multiple consecutive exchange attempts

---

## `paufregi.connectfeed.presentation.syncweight`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `SyncWeightViewModel` | `app/src/test/kotlin/paufregi/connectfeed/presentation/syncweight/SyncWeightViewModelTest.kt` | Partial |

### Useful additions
- Test `RenphoReader.read(...)` failure branch  
  This is a real missing branch in `SyncWeightViewModel.kt:36-41`
- Test null-message fallback from `syncWeight(...)`
- Test re-entry `ProcState.Running` while operation begins
- Test exposed `user` flow
- Optional: assert stream is consumed/closed correctly

---

## `paufregi.connectfeed.core.usecases`

### Overall status
This package is the strongest one overall.

| Group | Status |
|---|---:|
| CRUD/query use cases | Good |
| activity update use cases | Partial-to-good |
| sync use cases | Partial-to-good |
| simple delegating use cases | Good |

### Class-by-class notes

| Symbol | Test file | Status | Notes |
|---|---|---:|---|
| `GetActivityTypesForProfile` | `app/src/test/kotlin/paufregi/connectfeed/core/usecases/GetActivityTypesForProfileTest.kt` | Partial | Covers exact list only; okay for now since logic is tiny |
| `UpdateActivity` | `app/src/test/kotlin/paufregi/connectfeed/core/usecases/UpdateActivityTest.kt` | Good | Broad matrix of success/failure and description/workout/training-effect cases |
| `QuickUpdateActivity` | `app/src/test/kotlin/paufregi/connectfeed/core/usecases/QuickUpdateActivityTest.kt` | Good | Similar breadth to `UpdateActivity` |
| `SyncWeight` | `app/src/test/kotlin/paufregi/connectfeed/core/usecases/SyncWeightTest.kt` | Good | Strong coverage of Garmin/Strava combinations |
| `SyncGear` | `app/src/test/kotlin/paufregi/connectfeed/core/usecases/SyncGearTest.kt` | Good | Strong coverage including no-user, empty DB/lists, Strava failure fallback |

### Useful additions package-wide

#### `GetActivityTypesForProfile`
- If the use case is intended to be stable API, current test is okay.
- If ordering matters by design, keep the exact-order assertion.
- If ordering is incidental, consider clarifying that in tests.

#### `UpdateActivity`
Current coverage is strong. A few optional additions:
- blank name vs null name
- incompatible course = null path if the use case supports silent sanitization instead of validation
- gear omitted / null gear if supported
- commute/eventType edge mapping for Strava
- verify behavior when `activity.stravaId == null` and Strava update should definitely not be called

#### `QuickUpdateActivity`
Also strong. Optional additions:
- null profile fields with partially filled profile
- rename flag false semantics
- `profile.eventType == null`
- `profile.course == null`
- `gear == null` if allowed

#### `SyncWeight`
Current coverage is strong. Optional additions:
- empty weights input
- duplicate same-day entries selection behavior
- no Strava call when connected but “today” match is absent is already indirectly covered; explicit no-call assertion could help
- file path/name expectations if those are part of contract

#### `SyncGear`
Current coverage is strong. Optional additions:
- duplicate/similar names matching ambiguity with Strava gear
- user exists but DB emits later updates
- failure from `appRepo.saveGear(...)` / `deleteGear(...)`
- casing/whitespace normalization for gear name matching

---

## `paufregi.connectfeed.core.models`

### Missing tests

| Symbol | File | Status | Notes |
|---|---|---:|---|
| `Activity` | `app/src/main/kotlin/paufregi/connectfeed/core/models/Activity.kt` | Missing | Only worth testing if helper/default logic exists |
| `Course` | `app/src/main/kotlin/paufregi/connectfeed/core/models/Course.kt` | Missing | Test if there’s compatibility/helper logic |
| `EventType` | `app/src/main/kotlin/paufregi/connectfeed/core/models/EventType.kt` | Missing | Worth it if commute/display logic exists |
| `Gear` | `app/src/main/kotlin/paufregi/connectfeed/core/models/Gear.kt` | Missing | Usually low value unless helper logic |
| `Profile` | `app/src/main/kotlin/paufregi/connectfeed/core/models/Profile.kt` | Missing | Could be worth it if defaults/derived behavior are important |
| `Release` | `app/src/main/kotlin/paufregi/connectfeed/core/models/Release.kt` | Missing | Low value unless helper logic |
| `User` | `app/src/main/kotlin/paufregi/connectfeed/core/models/User.kt` | Missing | Low value if pure data |
| `Weight` | `app/src/main/kotlin/paufregi/connectfeed/core/models/Weight.kt` | Missing | Low value if pure data |
| `Workout` | `app/src/main/kotlin/paufregi/connectfeed/core/models/Workout.kt` | Missing | Low value if pure data |

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `ActivityType` | `app/src/test/kotlin/paufregi/connectfeed/core/models/ActivityTypeTest.kt` | Likely good |
| `GearType` | `app/src/test/kotlin/paufregi/connectfeed/core/models/GearTypeTest.kt` | Likely good |
| `Version` | `app/src/test/kotlin/paufregi/connectfeed/core/models/VersionTest.kt` | Likely good |

---

## `paufregi.connectfeed.core.utils`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `Extension` | `app/src/test/kotlin/paufregi/connectfeed/core/utils/ExtensionTest.kt` | Good |
| `FitWriter` | `app/src/test/kotlin/paufregi/connectfeed/core/utils/FitWriterTest.kt` | Good |
| `Formatter` | `app/src/test/kotlin/paufregi/connectfeed/core/utils/FormatterTest.kt` | Good |
| `RenphoReader` | `app/src/test/kotlin/paufregi/connectfeed/core/utils/RenphoReaderTest.kt` | Good |

### Useful additions
Mostly hardening:
- malformed CSV rows in `RenphoReader`
- locale variance in `Formatter`
- empty/invalid output file handling in `FitWriter`

---

## `paufregi.connectfeed.data.repository`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `AppRepository` | `app/src/test/kotlin/paufregi/connectfeed/data/repository/AppRepositoryTest.kt` | Partial-to-good |
| `AuthRepository` | `app/src/test/kotlin/paufregi/connectfeed/data/repository/AuthRepositoryTest.kt` | Partial-to-good |
| `GarminRepository` | `app/src/test/kotlin/paufregi/connectfeed/data/repository/GarminRepositoryTest.kt` | Partial-to-good |
| `GithubRepository` | `app/src/test/kotlin/paufregi/connectfeed/data/repository/GithubRepositoryTest.kt` | Partial-to-good |
| `StravaRepository` | `app/src/test/kotlin/paufregi/connectfeed/data/repository/StravaRepositoryTest.kt` | Partial-to-good |

### Useful additions
- failure propagation from API/store/DAO
- mapping of nullable fields
- empty source data
- multi-source precedence rules
- write-call argument verification for transformed payloads

These are likely already partly covered, but repo tests are where subtle mapping bugs often live.

---

## `paufregi.connectfeed.data.api.garmin`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `GarminAuth` | `app/src/test/kotlin/paufregi/connectfeed/data/api/garmin/GarminAuthTest.kt` | Good |
| `GarminConnect` | `app/src/test/kotlin/paufregi/connectfeed/data/api/garmin/GarminConnectTest.kt` | Good |
| `GarminSSO` | `app/src/test/kotlin/paufregi/connectfeed/data/api/garmin/GarminSSOTest.kt` | Good |
| `AuthInterceptor` | `app/src/test/kotlin/paufregi/connectfeed/data/api/garmin/interceptors/AuthInterceptorTest.kt` | Good |
| serializers | serializer test files exist | Good |
| converters | converter test files exist | Good |
| `models/AuthToken` | `app/src/test/kotlin/paufregi/connectfeed/data/api/garmin/models/AuthTokenTest.kt` | Good |

### Missing / low-priority model tests
Most `models/*` files are DTO-like and likely not worth unit tests unless they contain behavior.

### Useful additions
- malformed HTML parsing in converters
- missing CSRF/ticket markers
- null/unknown enum/date serializer values
- auth interceptor missing-token edge behavior

---

## `paufregi.connectfeed.data.api.strava`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `Strava` | `app/src/test/kotlin/paufregi/connectfeed/data/api/strava/StravaTest.kt` | Good |
| `StravaAuth` | `app/src/test/kotlin/paufregi/connectfeed/data/api/strava/StravaAuthTest.kt` | Good |
| `AuthInterceptor` | `app/src/test/kotlin/paufregi/connectfeed/data/api/strava/interceptors/AuthInterceptorTest.kt` | Good |
| serializers | serializer tests exist | Good |
| `models/AuthToken` | `app/src/test/kotlin/paufregi/connectfeed/data/api/strava/models/AuthTokenTest.kt` | Good |

### Missing / low-priority model tests
Likely low priority unless behavior exists:
- `Activity`
- `Athlete`
- `SummaryGear`
- `SportType`
- `UpdateActivity`
- `UpdateAthlete`

### Useful additions
- unknown sport type values
- auth edge cases
- invalid date/time payloads
- missing refresh/access token scenarios

---

## `paufregi.connectfeed.data.api.github`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `Github` | `app/src/test/kotlin/paufregi/connectfeed/data/api/github/GithubTest.kt` | Good |
| `GithubRepository` | `app/src/test/kotlin/paufregi/connectfeed/data/repository/GithubRepositoryTest.kt` | Good |

### Missing / low-priority model tests
- `models/Asset`
- `models/Release`

Likely low value unless parsing helpers/derived properties exist.

---

## `paufregi.connectfeed.data.database`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| converters | matching test files exist | Good |
| `GarminDao` | `app/src/androidTest/kotlin/paufregi/connectfeed/data/database/GarminDaoTest.kt` | Covered via androidTest |

### Missing unit tests
| Symbol | File | Status | Notes |
|---|---|---:|---|
| `GarminDatabase` | `app/src/main/kotlin/paufregi/connectfeed/data/database/GarminDatabase.kt` | Missing | Low priority unless migration/provider logic exists |
| `GearEntity` | `app/src/main/kotlin/paufregi/connectfeed/data/database/entities/GearEntity.kt` | Missing | Low value unless mapping helpers exist |
| `ProfileEntity` | `app/src/main/kotlin/paufregi/connectfeed/data/database/entities/ProfileEntity.kt` | Missing | Low value unless mapping helpers/defaults exist |

---

## `paufregi.connectfeed.data.datastore`

### Existing coverage
| Symbol | Test file | Status |
|---|---|---:|
| `AuthSerializer` | `app/src/test/kotlin/paufregi/connectfeed/data/datastore/serializers/AuthSerializerTest.kt` | Good |
| `AuthStore` | `app/src/androidTest/kotlin/paufregi/connectfeed/data/datastore/AuthStoreTest.kt` | Covered via androidTest |

### Missing unit tests
| Symbol | File | Status |
|---|---|---:|
| `AuthStore` | `app/src/main/kotlin/paufregi/connectfeed/data/datastore/AuthStore.kt` | Missing |
| `Auth` | `app/src/main/kotlin/paufregi/connectfeed/data/datastore/models/Auth.kt` | Missing / low priority |

---

## `paufregi.connectfeed.data.utils`

### Missing tests
| Symbol | File | Status |
|---|---|---:|
| `Updater` | `app/src/main/kotlin/paufregi/connectfeed/data/utils/Updater.kt` | Missing |
| `SecurityManager` | `app/src/main/kotlin/paufregi/connectfeed/data/utils/SecurityManager.kt` | No unit test; androidTest exists |

---

## `paufregi.connectfeed.system`

### Missing tests
| Symbol | File | Status |
|---|---|---:|
| `CleanUp` | `app/src/main/kotlin/paufregi/connectfeed/system/CleanUp.kt` | Missing |
| `Downloader` | `app/src/main/kotlin/paufregi/connectfeed/system/Downloader.kt` | Missing |
| `Installer` | `app/src/main/kotlin/paufregi/connectfeed/system/Installer.kt` | Missing |

These are likely platform-heavy, so unit tests may require abstraction or wrappers.

---

## `paufregi.connectfeed.di`

### Missing tests
All modules are untested:
- `AppModule`
- `AuthModule`
- `DatabaseModule`
- `GarminModule`
- `GithubModule`
- `StravaModule`

### Recommendation
Treat these as **not needed** unless they include conditional construction logic. Provider wiring is usually validated indirectly by app startup/tests.

---

# Priority shortlist

## Add these missing unit tests first
1. `EditViewModel`
2. `QuickEditViewModel`
3. `ProfileViewModel`
4. `ActivitiesViewModel`
5. `ProfilesViewModel`
6. `AppViewModel`

## Expand these existing test suites first
1. `LoginViewModelTest`
2. `SettingsViewModelTest`
3. `SyncWeightViewModelTest`
4. `StravaViewModelTest`
5. `GearsViewModelTest`

---

# Bottom line

## Missing high-value unit tests
The main gaps are in:
- `presentation.app.activities.*`
- `presentation.app.profiles.*`
- `presentation.app.AppViewModel`

## Existing tests that still leave meaningful blind spots
The most notable partial suites are:
- `LoginViewModelTest`
- `SettingsViewModelTest`
- `SettingsStateTest`
- `StravaViewModelTest`
- `SyncWeightViewModelTest`
- `GearsViewModelTest`

## Best-covered packages
- `core.usecases`
- `core.utils`
- API serializers/converters/interceptors
- repository layer, generally

If you want, I can next turn this into a **copy-pasteable implementation backlog** with:
- package
- test class name to create/update
- exact test method names to add
- short rationale for each test.
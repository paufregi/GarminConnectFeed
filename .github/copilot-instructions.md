# Coding Guidelines

Write code that fits the surrounding feature rather than imposing a new pattern.
Prefer direct, readable implementations and small, complete changes. Existing
behavior and explicit task requirements take precedence over stylistic cleanup.

## Architecture

- The app uses Kotlin, Jetpack Compose with Material 3, Hilt, Room, DataStore,
  Retrofit, OkHttp, and Kotlin serialization.
- Organize presentation by feature, with nested packages for distinct flows such
  as listing and editing. Keep reusable UI separate from feature-specific code.
- Keep responsibilities separated:
  - Presentation renders state, handles user actions, and coordinates UI effects.
  - Use cases expose application operations and orchestrate domain behavior.
  - Repositories coordinate remote and local data, authentication, and mapping.
  - APIs, persistence, and platform services handle their respective boundaries.
- ViewModels use use cases instead of calling APIs, DAOs, or stores directly.
  Keep domain models and business rules independent of Compose and Android UI.
- Keep platform-dependent work in the platform/infrastructure layer and inject it
  where needed. Preserve existing platform integrations rather than hiding them
  behind unnecessary new abstractions.
- Do not require every feature to have identical files or types. Add state,
  actions, effects, previews, or navigation containers when the feature needs them.

## Kotlin Style

- Use four-space indentation, descriptive names, small functions, and immutable
  `val` properties where possible. Match nearby formatting and visibility.
- Prefer data classes for value/state objects, sealed types for closed variants,
  and enums for simple fixed values. Do not introduce parallel representations of
  an existing concept.
- Use constructor injection. Keep dependencies explicit and avoid service locators
  or mutable global state.
- Use `operator fun invoke` for use cases where that is the established convention.
  Prefer expression bodies for simple delegation or transformations.
- Use nullability and meaningful defaults deliberately. Prefer safe calls,
  `let`, `takeIf`, and explicit validation over non-null assertions or unsafe casts.
- Use `copy` for immutable updates and existing collection/scope helpers when they
  improve readability. Avoid deeply nested scope functions and clever one-liners.
- Reuse shared domain helpers instead of repeating compatibility, mapping, or
  formatting rules in presentation code.
- Add comments only for non-obvious intent, constraints, or tradeoffs. Do not
  document self-explanatory code or introduce boilerplate documentation.

## State, Actions, and Effects

- Keep mutable state private and expose read-only flows. Update immutable state
  with `copy`, using flow update helpers for transformations.
- Combine repository/use-case flows with local editing state where needed.
  Follow the existing subscription-driven `stateIn` sharing strategy.
- Dispatch user actions through a feature's action type and `onAction` when that
  pattern exists. Simple standalone flows may expose focused public methods.
- Keep simple field updates in the action dispatcher; extract asynchronous
  operations into focused functions.
- Use the existing shared process state or feature-specific status type. Do not
  invent extra states or replace local status types merely for uniformity.
- Deliver one-off navigation through effects rather than persistent state when
  the feature uses an effect stream. Use the existing notification mechanism for
  user-facing messages.
- Launch ViewModel work in `viewModelScope`; use structured concurrency elsewhere.
  Do not use `GlobalScope`, block the main thread, or swallow cancellation.
- Follow existing `Result` handling and completion helpers. Preserve error messages,
  fallback behavior, and deliberate no-op guards; never disguise unexpected
  failures as successful empty data.

## Compose and Navigation

- Separate stateful screen wiring from reusable content where useful. Pass state
  and action callbacks to content rather than passing ViewModels through the tree.
- Collect state with lifecycle awareness. Keep side effects out of composition and
  use correctly keyed effect handlers.
- Reuse shared components, theme typography, colors, spacing conventions, and
  preview patterns. Extract UI by responsibility, not an arbitrary line limit.
- Use typed, serializable Navigation 3 keys and feature-owned back stacks. Preserve
  route arguments and assisted ViewModel construction for navigation inputs.
- Use `@HiltViewModel` with constructor injection or assisted injection as needed.
  Add module bindings/providers only when construction requires them.

## Data and Integrations

- Keep domain models distinct from transport and persisted representations where
  the existing design separates them. Map at the appropriate data boundary.
- Preserve user scoping in persisted queries and mutations.
- Persisted schema changes require a migration, exported schema updates, and
  appropriate integration coverage. Maintain converter and serialization behavior.
- Extend domain compatibility rules centrally and wire affected editors,
  filtering, persistence, and mappings consistently.
- Keep network operations suspending and use existing authenticated clients,
  signing, and token-refresh pipelines. Preserve rate-limit and update-check usage.
- Never hardcode credentials or log tokens, credentials, or sensitive health data.
  Use the existing build-time configuration and encrypted persistence mechanisms.
- Use dependency catalog aliases and respect existing dependency substitutions.
  Do not duplicate libraries or add abstractions/dependencies without a clear need.

## Testing and Validation

- Add or update tests at the layer whose behavior changes. Use JVM tests for pure
  logic and instrumented tests for Android, persistence, and UI integration.
- Unit tests use JUnit4, MockK, Truth, Turbine, and coroutine test utilities as
  appropriate. Compose integration tests use Hilt and the configured test runner.
- Mirror production packages in the corresponding test source set.
- Validate in this order: `./gradlew assemble`, `./gradlew test`, instrumented
  `./gradlew pixel10Check` when UI, Room, or Hilt wiring changes, then `./gradlew lint`.
  Documentation-only edits do not require application builds.
- Read [the testing guidelines](copilot-testing-instructions.md) for test structure
  and behavior, and [the review guidelines](copilot-review-instructions.md) when
  reviewing changes.

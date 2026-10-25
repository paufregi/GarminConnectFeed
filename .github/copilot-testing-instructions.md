# Testing Guidelines

Use these conventions when creating, changing, or reviewing tests. General
libraries and validation commands are in [the coding guidelines](copilot-instructions.md).
Test observable behavior with the simplest infrastructure appropriate to the layer.

## General Rules

These rules apply to every module. Mock and coroutine rules apply only when the
test needs those facilities; pure tests should remain lightweight.

### Structure, Naming, and Fixtures

- Mirror the production package and name the class `<Subject>Test`.
- Put JVM tests in the unit-test source set and Android-dependent tests in the
  instrumented source set. Package names below describe responsibilities, not
  separate Gradle modules.
- Use JUnit4 and inline Truth assertions. Use `runTest` for coroutine behavior, not
  for every synchronous test.
- Use descriptive backtick names: a behavior followed by an optional ` - <variation>`,
  such as `Initial state`, `Set name`, `Save - failure`, or `compatible - same type`.
- Keep arrangement, the operation under test, assertions, and verification visible
  inside each test. Separate these phases with blank lines, not obvious comments.
- Keep each test focused on one behavior/scenario. Multiple assertions are useful
  when they describe that behavior; do not combine unrelated operations.
- Prefer small real value objects and immutable fixtures. Use `copy` to vary the
  relevant fields. Keep inputs synthetic and expectations explicit.
- Make time, locale, and timezone assumptions deliberate. Prefer fixed values for
  pure behavior; use controlled relative time only where the contract requires it.
- Avoid assertion helpers and scenario-stubbing helpers. Reuse existing protocol
  fixtures, resource builders, and lifecycle rules when they remove genuinely
  complex setup rather than hiding the behavior being tested.
- Assert exact meaningful values, error messages, mappings, or wire formats.
  Do not compute expectations with the production helper being tested.
- Cover relevant positive, negative, empty, null, invalid, and boundary cases.
  Test the existing contract; do not change unrelated production behavior to make
  a test pass or add impossible failure cases solely for symmetry.
- Organize new cases next to the behavior they exercise. Preserve existing
  coverage when refactoring and avoid near-duplicate tests with different names.

### Setup, Cleanup, and Verification

- Use MockK for dependencies, not for ordinary domain values or the subject itself.
  Prefer strict mocks; reserve relaxed mocks for appropriate side-effect boundaries.
- Stub ordinary functions with `every` and suspend functions with `coEvery`.
  Arrange changing outcomes in the test that needs them.
- `setup()` should contain only invariant mock configuration. Do not hide
  scenario-dependent results or actions there. Usecase/repository suites may
  construct their subject there; ViewModels must be created in each test.
  Infrastructure tests may also initialize real resources; pure tests need no
  empty lifecycle methods.
- Verify actual dependency interactions, including calls made during initialization.
  Prefer meaningful arguments over broad matchers; use matchers for genuinely
  incidental values.
- Each `verify` or `coVerify` block must contain **exactly one mocked function call**.
  Use `verify` for ordinary functions and `coVerify` for suspend functions. Put
  multiple verifications in separate blocks.
- Use explicit counts when count or non-invocation matters; do not mechanically
  add counts to every verification. Assert that guarded paths avoid side effects.
- In mock-bearing suites, `tearDown()` must call `confirmVerified(...)` with all
  mocks, including getters, relaxed mocks, and object mocks, then `clearAllMocks()`.
  Restore object/static mocks and close real resources when applicable.
- Do not bypass verification with cleared call histories, broad unrelated
  verifications, or unnecessarily relaxed mocks.

### Results, Flows, and Resources

- Assert both the operation's outcome and its meaningful side effects. For
  `Result`, distinguish success from failure and inspect the value or error as
  relevant; do not assert only that the wrapper exists.
- Use Turbine for observable flow behavior. Assert emitted values, not merely
  that a flow getter was called. Use controlled source emissions when testing
  reactivity and finish subscriptions after checking the expected behavior.
- Control asynchronous dependencies instead of using sleeps or arbitrary delays.
  Account for StateFlow equality suppression and process scheduled work before
  asserting that no event or interaction occurred.
- Keep tests independent. Restore object/static mocks, isolate persistent data,
  and close owned streams, servers, databases, and temporary resources.
- Reuse shared resource rules and protocol fixtures; do not add competing test
  infrastructure for a single suite. Never call live services or use real secrets.

## Models (`core/models`)

- Use plain synchronous tests with real instances and direct assertions. Do not add
  ViewModel factories, mock lifecycle, coroutine dispatchers, or UI infrastructure.
- Share simple immutable baseline fixtures if useful; construct scenario-specific
  values locally. Avoid fixture factories and assertion wrappers for simple values.
- Test behavior: compatibility, derived properties, meaningful defaults, parsing,
  formatting, validation, and comparisons. Avoid tests solely for generated
  data-class getters, equality, or `copy`.
- Group representative inputs for one logical category in a test where readable.
  Pure model tests may invoke the same function several times.
- For compatibility, cover accepted/rejected types, parent/subtype directions,
  sibling types, unrelated groups, and wildcards wherever supported. Do not assume
  every compatibility rule is symmetric or shares the same domain exceptions.
- Test public model-level wrappers with real inputs even if their lower-level
  helper also has tests. Exercise meaningful defaults through observable behavior.
- For parsing, distinguish valid forms, supported prefixes, invalid structure,
  invalid components, and the actual failure contract. Check parsed components.
- For comparisons, cover equality, lower/higher components, and precedence where
  relevant. For formatting, assert exact text including prefixes and separators.

## Usecases (`core/usecases`)

### Structure and Delegation

- Construct a real usecase with mocked repositories or collaborating usecases and
  real input models. Declare dependencies privately and keep reusable fixtures
  immutable.
- Direct construction in `setup()` is appropriate when the constructor is
  scenario-independent. Do not add a ViewModel-style creation helper by default.
- Keep authentication state, optional connections, returned data, and errors local
  to each test unless their stub is truly invariant throughout the suite.
- Invoke the public operation once for the scenario under test. Assert its result
  and verify dependency arguments separately; delegation alone does not establish
  correct mapping or orchestration.
- For success, assert `isSuccess` and the meaningful payload. For failure, assert
  failure and the expected error contract. For simple write-only delegation,
  precise interaction verification may be the complete observable assertion.

### Coverage

- Separate validation and authentication guard cases. Verify reads needed to
  evaluate a guard and ensure rejected operations do not reach downstream writes.
- Test required dependency failures and meaningful optional inputs/flags, including
  missing identifiers and disconnected integrations.
- For operations spanning services, cover each independent failure and combined
  failures when outcomes are aggregated. Check the actual policy rather than
  assuming every error short-circuits or every optional failure must fail the result.
- For synchronization, assert the exact inserted/updated and removed records,
  ownership, external-ID matching, unmatched records, and empty-source behavior.
  A successful result alone is insufficient.
- Collect flow-returning operations with Turbine and assert emitted values.
  Cover populated, empty, missing-user, and later-source-update cases where the
  usecase supports them.

## API (`data/api`)

### Endpoints and Requests

- Use the real service/client against the shared local mock server. Reuse the
  existing server rule, response fixtures, and request-decoding helpers.
- Set up the client/resource lifecycle once where invariant; enqueue scenario
  responses inside each test.
- Assert decoded response values and the meaningful outbound contract: method,
  path, query/form fields, headers, and serialized body. A successful HTTP status
  alone does not verify a request or response mapping.
- Cover populated/empty responses, unsuccessful statuses, and absent bodies where
  supported. Assert HTTP responses at this boundary rather than imposing a
  repository's result/fallback policy on the API.

### Authentication and Helpers

- Exercise interceptors through real client requests, mocking token storage or
  refresh dependencies only where needed.
- Cover valid, missing, and expired tokens; refresh success/failure; correct refresh
  arguments; and persistence of replacement tokens where the client supports them.
- Assert authentication headers/signing behavior and guarded non-calls. Do not
  require refresh, token writes, or outbound requests when the path should stop.
- Test HTTP helpers with real requests/responses. Assert authorization construction,
  URL preservation, and synthetic failure status/message/body/request association.
- Test token expiry helpers with deliberately controlled issuance/expiry times.
  Distinguish access-token expiry from refresh-token expiry according to the model.

### Serialization and Converters

- Test encoding and decoding independently against explicit wire representations,
  not only through round trips.
- Cover identifiers/keys, enum spelling and case, date-time formats, and epoch units.
  Distinguish unknown values from unsupported-but-preserved remote values when
  wrappers retain upstream identifiers.
- Test converter selection separately from conversion: supported types select the
  intended converter, and unsupported types return the documented absence result.
- For text/HTML extraction, use small representative inputs, assert extracted
  values and supported string conversion, and cover missing/malformed markers
  according to the converter's actual contract.
- Pure serializers/helpers need no server or mocks; use the HTTP harness only when
  testing transport or client integration.

## Database (`data/database`)

### Converters and Mapping Helpers

- Use synchronous JVM tests with real persisted values, entities, and core models.
  Do not create a Room database merely to exercise pure conversion.
- Test conversion in each direction with explicit expected stored IDs/names and
  model values. Round trips may supplement but must not replace these assertions.
- Cover all meaningful mapping branches, including null and unknown/unmapped
  inputs where supported. Preserve converter-specific differences: one converter
  may return null while another deliberately maps to an unknown value.
- For entity/model helpers, compare complete values to protect identifiers,
  nested objects, distances, optional values, and flags.
- When entity creation takes user/ownership context, pass it explicitly and assert
  the resulting ownership fields as well as unchanged domain fields.
- Do not add mock verification, dispatcher rules, or persistence lifecycle to a
  converter suite.

### Persistence Integration

- Test actual DAO behavior with a real test database in instrumented tests using
  the configured injection/resource setup. Do not introduce a second database
  harness without need.
- Check save/read/update/delete, missing rows, user-filtered collections, and
  reactive emissions after writes as applicable.
- For schema changes, add migration coverage and maintain exported schema
  expectations. Keep records isolated and close the owned database.
- A meaningful persistence scenario may perform several operations. The
  one-action-per-test rule is specific to ViewModel actions, not DAO round trips.

## Datastore (`data/datastore`)

### Serializers and Helpers

- Exercise stream reads/writes with in-memory byte streams and explicit text
  encoding. Use the coroutine runner required by the suspending serializer rather
  than adding ViewModel dispatcher infrastructure.
- Assert the default value, decoded fields, and serialized content independently.
  Use explicit payloads or independent expected values so symmetric JSON errors
  cannot hide in a round trip.
- Cover malformed/empty input and missing/optional fields where relevant. Assert
  the actual read fallback or failure contract; do not assume writes share it.
- Keep pure serialization tests separate from encrypted-storage integration.
  Do not mock encryption into a serializer that does not own it.
- Test helper transformations directly with real values; verify preservation of
  unrelated fields for selective updates/clears.

### Store and Security Integration

- Use the real configured store for Android-dependent integration and Turbine for
  exposed flows. Observe initial, saved, replaced, and cleared values as relevant.
- Subscribe to the flow for the value being written, not an unrelated projection.
- Test full clearing and selective clearing separately, including preservation of
  unrelated user/token values.
- Clear persistent state in cleanup so suites do not depend on execution order.
- For framework-backed encryption helpers, assert meaningful ciphertext behavior
  and successful decryption with the real test resource. Test error paths only
  where they belong to that helper's contract; do not invent serializer behavior.

## Repository (`data/repository`)

- Construct the real repository with mocked APIs, DAOs, stores, or other boundary
  dependencies. Use real DTOs, entities, response wrappers, and domain models.
  These unit tests do not need a live service or real database.
- Construct the subject in `setup()` when construction does not vary by scenario.
  Stub response content and failure branches within individual tests.
- For reads, use explicit source and expected destination values. Assert mapped
  fields and meaningful defaults/nulls, or preservation when the method deliberately
  returns an upstream model unchanged.
- Cover populated, empty, nullable, and unsuccessful responses according to the
  method's actual policy. Do not impose a universal null-body fallback.
- Verify forwarded limits, identifiers, credentials, and ownership values as well
  as the returned result.
- For writes/deletes, verify exact mapped entities and user-scoping fields, not
  just that some write occurred.
- For authentication and multi-step operations, cover prerequisite failure,
  downstream failure, successful progression, persisted tokens, and suppressed
  downstream calls. Preserve the intended fallback/error translation.
- Collect stored/derived flows with Turbine and assert values as well as verifying
  the boundary getter. Drive source changes when updates are part of the contract.
- For methods whose only observable behavior is a write, exact delegation
  verification is sufficient; do not invent a return value to assert.

## ViewModels (`presentation/`)

### Structure

- Declare dependencies and reusable fixtures as private properties, with
  `private lateinit var viewModel`.
- Every suite must have a `private fun createViewModel()` helper. Construct the
  ViewModel explicitly in each test after arranging the needed dependency behavior.
- Keep `setup()` limited to mocks whose behavior is invariant across the suite.
  Do not construct the ViewModel, inject state, or invoke actions there.
- Use the shared main-dispatcher test rule and coroutine opt-in convention.
  Do not create a competing dispatcher setup for each suite.
- Avoid extra helpers, especially assertion helpers. Follow the mock teardown and
  single-call verification rules above for every dependency.

### Initial State and Actions

- Start with `Initial state` and `Initial state - <variation>` cases: loading,
  connected/disconnected users, missing data, empty lists, and failure as relevant.
  Exercise genuine startup behavior through dependency stubs.
- Test each `onAction` branch in isolation, invoking **one action once per test**.
  Name the test for the action, with a variation suffix when needed.
- If there is no action dispatcher, test the public methods and exposed flows.
  Flow-update tests may drive multiple source emissions to check reactivity.
- Do not prepare a save/reset test by invoking several editing actions first.
  For prerequisite edited state, expose only the following internal hook, using
  the feature's actual state type:

  ```kotlin
  internal fun injectState(state: FeatureState) {
      _state.value = state
  }
  ```

- The hook only replaces backing state; it must not invoke dependencies or add
  behavior. Do not use reflection or test-only constructor parameters.
- Subscribe to exposed state, consume startup emissions and finish initial loading,
  inject the prerequisite state, then consume/assert the seeded emission before
  invoking the action. Startup work must not overwrite the seed.
- StateFlow suppresses equal values. Do not await a new item when injecting an
  identical state. Seed a non-loading status before expecting an action to emit
  loading, and keep combined source values in mind when asserting seeded state.

### State Transitions and Effects

- Use Turbine subscriptions and inline assertions on relevant state fields or the
  complete state. Assert preservation of unrelated values when useful.
- Use `CompletableDeferred` to hold asynchronous dependencies so the actual
  loading/running transition can be asserted before success/failure completes.
- Do not rely on sleeps, arbitrary delays, or fragile counts of transient emissions.
  Await the transitions under test; skip only understood initial emissions.
- Cover success/failure, absent error messages, null inputs, reset behavior,
  missing data, and guard clauses where applicable.
- Assert notification messages and one-off effects separately from persistent state.
  Verify absence of effects and dependency calls on paths that must not perform them.
- For an incompatible selection that is deliberately ignored, seed a valid current
  selection, send the incompatible action, assert `expectNoEvents()`, and confirm
  the exposed state still equals the seeded emitted state.
- Do not await an emission from a no-op. Ensure scheduled work has actually been
  processed before asserting absence when a path is asynchronous.
- Finish subscriptions with `cancelAndIgnoreRemainingEvents()` after asserting the
  behavior under test, not as a substitute for checking important transitions.

## Utilities (`core/utils` and Other Helper Packages)

- Apply pure-model rules to deterministic helpers and mock rules only where the
  helper crosses a boundary.
- Assert exact formatting, separators, prefixes, numeric conversions, and null
  behavior. Make locale/timezone explicit and restore global settings if changed.
- Test result/response helpers for preserved payloads, error propagation, fallback
  values, and whether subsequent work runs or short-circuits.
- Use small in-memory samples for readers; assert complete parsed values and
  supported header/language variations.
- For binary writers, decode the output with a format reader and assert meaningful
  records/fields, not just file existence. Assert that records were observed so
  assertions inside callbacks cannot pass without being executed.

## Compose and Other Integration Tests

- Compose tests should supply state and callbacks directly where possible and
  assert semantics, visibility, enabled states, and interaction outcomes.
  Use Hilt and the configured runner when testing injected screen/app wiring.
- Keep lifecycle helpers specific to resource ownership. Real-resource integration
  tests need cleanup, not artificial mock verification or one-action restrictions
  that prevent testing a meaningful persistence sequence.

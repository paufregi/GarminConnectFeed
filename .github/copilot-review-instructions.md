# Code Review Guidelines

Apply [the coding guidelines](copilot-instructions.md) and
[the testing guidelines](copilot-testing-instructions.md) to changed behavior.
Review for concrete defects and regressions, not opportunities to redesign the app.

## Review Approach

- Read the changed implementation and relevant callers, data mappings, and tests.
  Explain a finding with the affected behavior, triggering conditions, and impact.
- Distinguish correctness requirements from local stylistic preferences.
  Do not enforce an abstraction merely because another feature uses it.
- Prioritize reproducible logic errors, broken contracts, missing integration,
  data loss, lifecycle mistakes, and security issues. Avoid speculative findings.
- Do not request unrelated cleanup or turn pre-existing inconsistencies into
  mandatory refactors. Check whether a change introduces or worsens the issue.

## Architecture and Style

- Check feature-local organization and separation of presentation, use-case,
  repository, and data/platform responsibilities.
- ViewModels should not call APIs, DAOs, or stores directly. Simple input guards
  and UI-derived state belong in presentation; shared domain rules should be reused.
- Do not demand interface-per-repository, dedicated mapper classes, separate
  Gradle modules, or identical feature templates where the design does not need them.
- Check immutable value/state updates, explicit dependencies, null safety, and
  readable control flow. Prefer established helpers over duplicate implementations.
- Hilt ViewModels may use ordinary injected constructors or assisted factories.
  Check providers, qualifiers, and lifetimes where construction requires them;
  do not demand module providers for every injectable class.

## UI, State, and Concurrency

- Check lifecycle-aware collection, subscription-driven flow behavior, initial
  loading, action handling, and terminal success/failure states.
- Verify nullable/default state semantics, compatibility guards, and derived
  selections. A deliberate no-op must preserve the user's existing selection.
- Check that transient effects are not replayed as persistent state, and that
  notifications/navigation occur only on the intended paths.
- Confirm structured coroutine ownership, cancellation behavior, and cleanup.
  Flag blocking main-thread work and swallowed failures or cancellation.
- Check Compose effect keys, state hoisting, callbacks, and shared theme/component
  usage. Do not prescribe a fixed composable length or require every screen to
  expose the same action/status structure.
- Preserve typed Navigation 3 keys, route arguments, back-stack behavior, and
  assisted ViewModel inputs.

## Data and Security

- Verify correct request/result mapping, nullable values, serialization, caching,
  and early exits. Do not assume all repository outputs have the same model shape.
- Check user scoping on reads and writes, schema migration coverage, exported schema
  updates, and converters when persisted representations change.
- Follow field/type additions through affected domain rules, editors, filters,
  mappings, persistence, and integrations.
- Preserve authenticated request signing and token-refresh behavior. Check
  configuration, encrypted credential persistence, and network boundary handling.
- Flag hardcoded secrets, sensitive logging, unsafe credential storage, and
  bypassed authentication. Use synthetic credentials in tests.
- Check dependency catalog usage, substitutions, duplicate dependencies, and
  release-shrinking impact for new reflective or serialized behavior.

## Tests and Validation

- Require tests for changed behavior at the appropriate layer, not an automatic
  use-case/ViewModel/UI test trio for every change.
- Apply the detailed testing standards without duplicating them here. In particular,
  check isolated actions, deterministic state seeding, inline assertions, strict
  mock teardown, and one mocked function call per verification block.
- Keep pure model tests lightweight; do not impose ViewModel factories or mock
  lifecycle on synchronous logic tests.
- Check positive/negative cases, relevant boundaries, asynchronous transitions,
  notifications/effects, and non-calls on guarded paths.
- Ensure tests assert behavior rather than only setup interactions or self-derived
  expectations. Do not accept deleted coverage without a clear justification.
- Check applicable build, unit, integration, and lint results. State unavailable
  validation plainly; do not claim a failure without evidence or request behavior
  changes solely to silence a stale expectation.

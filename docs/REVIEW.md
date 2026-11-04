# Source and validation review

Reviewed 4 November 2026 against the supplied Integratieproject2 archive. This is a targeted source/build review, not a full security audit or end-to-end acceptance test.

## Executed checks

| Component | Check | Result |
| --- | --- | --- |
| Platform frontend | `npm ci --ignore-scripts`; `npm run build`; `npm test -- --run` | Build passed; 43 tests in 12 files passed |
| Go frontend | Same commands | Build passed; 12 tests in 3 files passed |
| Tic-Tac-Toe frontend | `npm ci --ignore-scripts`; `npm run build` | Build passed; no test script in package.json |
| Analytics | `python3 -m pytest tests/unit -q` | 9 passed; tests are shallow and do not establish pipeline correctness |
| Configuration | Parse 20 JSON files (excluding JSON-with-comments tsconfig files), 18 Python source files | Passed |
| Java | Platform `bash gradlew --version` | Blocked: Gradle distribution network access unavailable; installed JDK is 17, project requests 21 |
| Docker and live services | Full stack / integration tests | Not run: Docker unavailable |

Platform build warns about a large JavaScript bundle (~850 kB minified). Some frontend tests log expected error-boundary/mock warnings while passing.

## Important findings

1. **Two independent brokers / port conflict.** `infrastructure/docker-compose.yaml` and `platform-analytics/docker-compose.yml` both publish 5672 and 15672, with different demo credentials and networks. They cannot be started unchanged together. Select one shared broker and align the application, Logstash and setup scripts before validating live end-to-end analytics.
2. **Routing-key mismatch for user/social events.** The platform `application.properties` publishes `user.session.logged_in`, `user.session.logged_out`, `user.account.registered`, and `user.social.friend_added`. Analytics `setup/setup_rabbitmq_bindings.py` binds different `platform.user.*` and `platform.friend.*` keys. Purchase/payment and game-session keys do match by inspection. User/social bindings need alignment before claiming all events reach dashboards.
3. **Incomplete event coverage.** Platform `AnalyticsMessagePublisher` explicitly marks logout, registration, page-visit and system-error publication as unavailable in the current architecture. Definitions alone do not prove these events are emitted.
4. **Development-only persistence/security.** Backend properties use `spring.jpa.hibernate.ddl-auto=create-drop`; Compose and scripts include demo credentials and plaintext local endpoints. The platform security configuration ends with `anyRequest().permitAll()`. Review all routes and ownership checks before any public deployment. The upload is a source publication, not a deployment.
5. **Analytics tests are weak.** `test_generate_events.py` includes `assert True` instead of importing the generator. Other tests validate hardcoded example dictionaries. Passing these nine tests does not prove real event ingestion or retention calculations. `pytest.ini` uses `[tool:pytest]` rather than `[pytest]`, so its intended pytest options are not applied.
6. **External dependencies.** AI/chatbot services and the course Chess images are referenced but their full implementations are not in this archive. Frontend production URLs such as `0.0.0.0:8090` are deployment placeholders, not public demo URLs.
7. **Simplified Go rules.** `Board.java` implements captures and territory/stone scoring, but the inspected placement logic also removes a player's own group without liberties. A standard-rules claim requires explicit suicide/ko policy and dedicated tests; no such full-rules certification is made here.
8. **Analytics bootstrap is destructive.** Inherited analytics startup documentation/scripts include Docker volume resets. Use a disposable local environment; do not run those scripts against valuable service data.

## Changes made for publication

- Combined the supplied components under one root, with a project README, attribution and this review.
- Excluded runtime database/broker state, private key material, original Git databases, generated artifacts, HTTP collections and the course assignment PDF.
- Removed users and credential-bearing fields from all three Keycloak realm templates.
- Added a root `.gitignore`, a standalone analytics `.env.example`, and replaced two SQL sample email addresses with `example.invalid` addresses.
- Preserved application behavior and existing tests; the integration issues above are documented, not silently marked fixed.

## CV wording evidence

The existing CV identifies the analytics subsystem as Said's project. Original backend commits under Said's name additionally establish event-model and RabbitMQ configuration work. The CV therefore attributes those contributions to Said and describes React/TypeScript, Go and Tic-Tac-Toe as the wider team-project context. It does not claim that Said personally implemented every game or frontend.

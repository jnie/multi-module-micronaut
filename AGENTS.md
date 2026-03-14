# AGENTS.md guide

## 1. Repository Overview
- The multi-module-architecture is a multi-module Maven project demonstrating Clean Architecture and Hexagonal Architecture patterns.
- This is to showcase how separation of concerns is kept strict in separate modules, to give a low cognitive experience for humans.
- Low cognitive load is mostly relevant in integrated development environment(IDE).

**Technology Stack**
- Java 21
- Micronaut 4.4.0
- Maven 3.9+
- In-memory caching (ConcurrentHashMap)
- CompletableFuture (async/await pattern)

---

## 2. Project Structure, Code style & patterns

```
multi-module-architecture/
├── app/
│   ├── inbound/rest       # REST controllers, DTOs
│   ├── application/       # Micronaut application entry point
│   ├── domain/           # Domain models, interfaces, repository contracts
│   ├── repository/       # Caching infrastructure
│   ├── service/          # Business logic orchestration
│   └── outbound/        # External API adapters
├── doc/                  # Documentation & diagrams
├── AGENTS.md             # Agents guidelines
├── .github/              # GitHub workflows
└── pom.xml               # Parent POM
```

### Module Dependencies (Critical)
```
domain     ← (no dependencies, core)
service    ← domain + repository
outbound   ← domain
rest       ← domain + service
application ← all modules
```
**Important**: Service can depend on repository ONLY through domain interfaces. Never import repository implementations in service layer.

### General principles

1. **Always check existing modules first** - Understand the architecture before making changes
2. **Keep the module boundaries clean** - Don't mix concerns across modules
3. **Domain layer should be vendor-agnostic** - No Micronaut annotations in domain/
4. **Use Lombok and MapStruct** - Already configured in POMs
5. **main branch** - is off limits, when changing code always create a branch
6. **Run tests before committing** - Use `mvn test`
7. **Port/adapter pattern** - External APIs go in `app/outbound/`, interfaces in `app/domain/`
8. **Port/adapter pattern** - Inbound APIs go in `app/inbound/`, no Interfaces

### Type safety & Code quality

- Language: Java 21; use strict typing; avoid raw types and unchecked casts.
- Formatting: Use 2 spaces for indentation, Google Java Style
- Verification: `./mvnw verify` runs all checks; fix violations, don't suppress.
- Never use `@SuppressWarnings` without justification; fix root causes instead.
- Never use reflection or runtime bytecode manipulation to share class behavior; use inheritance/composition.
- If reflection is needed, stop and get explicit approval; prefer interfaces and DI.
- In tests, use Mockito `@Mock` per-instance stubs; avoid static state mutation.
- Add brief comments for tricky logic (explain WHY, not WHAT).
- Keep files under ~500 LOC; extract helpers instead of "V2" copies.
- MapStruct: use interfaces with `componentModel = "jakarta"` (NOT "spring" or "cdi"); don't manually implement mappers.
- Lombok: avoid `@Data`; prefer `@Value`, `@Builder`, `@RequiredArgsConstructor`.
- Async: use `CompletableFuture` consistently throughout; avoid blocking calls.
- Naming: follow Micronaut conventions (`@Singleton`, `@Inject`, `@Controller`).

### Formatting
- Use 2 spaces for indentation (not tabs)
- Always include curly braces, even for single-line `if` statements
- No trailing whitespace
- Maximum line length: 120 characters

---

## 3. Agentic workflow (The "How-To")
When you are tasked with a feature or bug fix, follow this exact sequence:

### Phase 1: Exploration & Plan
1. **Search:** Locate relevant logic using `grep` or symbol search.
2. **Propose:** Briefly summarize your plan in the chat before writing code.

### Phase 2: Implementation & Testing
1. **Branch from a clean main** - Make sure you have all the latest from main branch, create new branch from here
2. **Execute:** Modify files. Do not delete comments unless they are obsolete.
3. **Local Validation:** 
   - Build: `mvn clean compile`
   - Test: `mvn test`
4. **Self-Correction:** If tests fail, analyze the logs, fix the code, and re-run tests until green. **Do not ask for help until you have attempted 2 logical fixes.**

---

## ⚡ Quick Reference

| Action | Command |
|--------|---------|
| Full build | `mvn clean package` |
| Run app | `mvn spring-boot:run -pl app/application` |
| Run tests | `mvn test` |
| Run single test | `mvn test -Dtest=ClassName#methodName` |
| Run single test class | `mvn test -Dtest=ClassName` |
| Build specific module | `mvn install -pl <module> -am` |
| Check dependencies | `mvn dependency:tree` |
| Default port | 8080 |
| OpenAPI spec | http://localhost:8080/openapi |

---

## 🔧 Common Issues & Solutions

**Problem:** "Unable to find main class"
```bash
# Make sure to run from application module
mvn spring-boot:run -pl app/application
```

**Problem:** MapStruct/Lombok conflicts
```bash
# Ensure annotation processor order is correct (already configured in pom.xml)
# If issues persist, run:
mvn clean compile
```

**Problem:** Port already in use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
# Or run on different port
mvn spring-boot:run -Dspring-boot.run.arguments='--server.port=8082'
```

**Problem:** OpenAPI returns empty `{}`
- Ensure `micronaut-openapi` annotation processor runs in each module that has controllers
- Check that REST module generates OpenAPI spec in `target/classes/META-INF/swagger/`


## Testing Guidelines

### Test Framework & Coverage
- Framework: JUnit 5 (Jupiter) with Mockito and AssertJ
- Unit tests: `*Test.java` suffix; integration tests: `*IT.java` suffix
- Run `./mvnw test` before pushing when you touch logic

### Test Execution
- Single test: `mvn test -Dtest=OurServiceImplTest#getAnAdvice_ReturnsAdviceSuccessfully`
- Single test class: `mvn test -Dtest=OurServiceImplTest`
- Memory pressure: `MAVEN_OPTS="-Xmx1g -XX:MaxMetaspaceSize=512m" ./mvnw test`
- Test profiles: use `-Dspring.profiles.active=test`

### Test Patterns
- Use `@ExtendWith(MockitoExtension.class)` for unit tests
- Use `@MicronautTest` for integration tests
- Use `MockitoExtension(strictness = Strictness.LENIENT)` when needed
- Mock return types must Also proceed with the chnages to the agents.mdmatch: `CompletableFuture.completedFuture(...)` for async methods


## 4. Pull Request (PR) Requirements

- **Repo:** https://github.com/jnie/multi-module-architecture
- **File references:** Use repo-root relative paths (e.g., `app/inbound/rest/src/main/java/...`)
- **Breaking Changes:** Explicitly state if any APIs were modified
- **Risk Assessment:** Label as [Low/Medium/High] risk
- **Commit messages:** Focus on "why", not "what"


## 5. Constraints & Boundaries
- **Dependencies:** Do not add new external libraries without explicit user approval
- **Secrets:** Never commit `.env` files or API keys
- **Async:** Use `CompletableFuture` consistently - don't mix with Reactor `Mono`/`Flux`
- **Module boundaries:** Never import across module boundaries except through domain interfaces
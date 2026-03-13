# Multi-Module Micronaut

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Micronaut](https://img.shields.io/badge/Micronaut-4.4.0-green)
![Maven](https://img.shields.io/badge/Maven-3.9%2B-purple)

A **multi-module architecture proof-of-concept** demonstrating that clean architecture is **framework-independent**. This project is a complete migration from Spring Boot to Micronaut while preserving the same module structure.

## 🎯 Purpose

- Demonstrate that multi-module architecture is **framework-agnostic**
- Showcase Micronaut's advantages: **faster startup**, **lower memory usage**
- Provide a **reference implementation** for Spring Boot → Micronaut migrations

## 📦 Architecture

The project follows a **Clean Architecture** approach with clear separation of concerns:

```
app/
├── inbound/rest/          # REST controllers, DTOs, mappers
├── application/           # Main class, configuration, beans
├── domain/                # Domain models, interfaces (business logic contracts)
├── service/               # Business logic implementation and Orchestration layer
└── outbound/              # External adapters (HTTP clients, repositories)
    └── advice-slip-api/   # Integration with Advice Slip API
```

### Module Responsibilities

| Module | Description                                                                     |
|--------|---------------------------------------------------------------------------------|
| **inbound/rest** | REST controllers, DTOs, exception handling                                      |
| **application** | Entry point (Application.main), configuration, dependency wiring                |
| **domain** | Business models, interfaces (contracts) for services, no framework dependencies |
| **service** | Orchestration and business logic, implements domain services                    |
| **outbound/advice-slip-api** | External API integration (HTTP client)                                          |

## Constraints for module dependencies
- inbound/rest cannot depend on any other module than Domain
- application depends on all modules to wire the full application
- domain cannot depend on any other module
- service cannot depend on any other module than Domain
- outboudn/*api* cannot depend on any other module than Domain

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9+

### Build

```bash
./mvnw clean install
```

### Run

```bash
cd app/application
./mvnw spring-boot:run  # For Spring Boot version
java -jar target/application-*.jar  # For Micronaut version (when built)
```

### Test

```bash
./mvnw test
```

## 🔄 Migration: Spring Boot → Micronaut

This project demonstrates the key differences and migration steps:

### Annotations Mapping

| Spring Boot | Micronaut | Notes |
|-------------|-----------|-------|
| `@RestController` | `@Controller` | Direct replacement |
| `@GetMapping` | `@Get` | Direct replacement |
| `@PostMapping` | `@Post` | Direct replacement |
| `@Autowired` | `@Inject` | Recommended (Spring still works) |
| `@Service` | `@Singleton` | Micronaut only has singleton scope |
| `@Component` | `@Singleton` | Same as @Service |
| `SpringApplication.run()` | `Micronaut.run()` | Entry point change |

### Dependencies

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Micronaut -->
<dependency>
    <groupId>io.micronaut</groupId>
    <artifactId>micronaut-http-server-netty</artifactId>
</dependency>
```

### Key Features Preserved

- ✅ **MapStruct** - Same annotation processor usage
- ✅ **Lombok** - Works with Micronaut
- ✅ **Immutables** - Works with Micronaut
- ✅ **Multi-module structure** - 100% preserved (this is the point!)

## 📊 Comparison

| Aspect | Spring Boot | Micronaut |
|--------|-------------|-----------|
| **Startup Time** | Slow (reflection) | Fast (compile-time DI) |
| **Memory** | High | Low |
| **DI** | Runtime proxy | Compile-time proxy |
| **Config** | `@Value`, properties | Type-safe config |

## 📚 API Documentation

Once running, access the REST API at:
- **Swagger UI**: `http://localhost:8080/swagger-ui`

## 🔧 Configuration

The application uses these configurable properties:

```yaml
mma:
  outbound:
    advice-slip-api:
      url: "https://api.adviceslip.com"
```

## 📝 License

See [LICENSE](LICENSE) for details.

---

*This project demonstrates that clean architecture is about **module design**, not framework choice. The same multi-module structure that works with Spring Boot works just as well with Micronaut.*# Build debug

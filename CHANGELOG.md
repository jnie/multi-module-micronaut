# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added

- OpenAPI/Swagger UI integration for REST API documentation
  - OpenAPI specification exposed at `/swagger/service-1.0.0.yml`
  - Swagger UI available at `/swagger-ui/index.html`
  - Works both when running as JAR and from IntelliJ IDE

### Changed

#### Application Module (`app/application`)

- **Application.java**: Added `@OpenAPIDefinition` annotation with API metadata (title, version, description, contact, license)
- **application.yml**: Added static resource mappings for OpenAPI spec and Swagger UI:
  - `/swagger/**` → `classpath:META-INF/swagger`
  - `/swagger-ui/**` → `classpath:META-INF/swagger/views/swagger-ui`

#### REST Module (`app/inbound/rest`)

- **openapi.properties**: Created configuration file to enable Swagger UI generation with spec URL

### Technical Details

The OpenAPI specification is generated at compile time by the `micronaut-openapi` annotation processor. The generated spec file (`service-1.0.0.yml`) is placed in `META-INF/swagger/` and included in the JAR.

Key files:
- `app/inbound/rest/target/classes/META-INF/swagger/service-1.0.0.yml` - Generated OpenAPI spec
- `app/inbound/rest/target/classes/META-INF/swagger/views/swagger-ui/` - Generated Swagger UI assets

### Usage

After starting the application:

```bash
./mvnw mn:run -pl app/application
```

Access the API documentation:

| Resource | URL |
|----------|-----|
| OpenAPI YAML Spec | http://localhost:8080/swagger/service-1.0.0.yml |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
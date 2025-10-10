# GitHub Copilot Instructions for API Hammer

## Project Overview

API Hammer is a stress-tested REST API built with Java Spring Boot, designed to manage "warrior" entities while maintaining resilience under CPU and memory constraints. The project focuses on high concurrency, performance optimization, and load testing.

## Tech Stack

- **Language:** Java 17 (upgrading to Java 21)
- **Framework:** Spring Boot 3.1.4
- **Build Tool:** Gradle 8
- **Database:** PostgreSQL 15
- **Load Testing:** Gatling
- **Containerization:** Docker Compose with resource limits (CPU: 1.5, Memory: 3GB)
- **Reverse Proxy:** Nginx
- **Testing:** JUnit 5, Mockito

## Project Structure

```
api-hammer/
├── src/main/java/com/example/api/
│   ├── ApiHammerApplication.java
│   ├── controller/          # REST controllers
│   ├── service/             # Business logic
│   ├── repository/          # JPA repositories
│   ├── entity/              # JPA entities
│   ├── dto/                 # Data transfer objects
│   └── exception/           # Custom exceptions
├── src/test/java/           # Unit and integration tests
├── build.gradle             # Gradle build configuration
├── docker-compose.yml       # Multi-container setup
└── DOCS/                    # Project documentation
```

## Core Domain: Warrior Entity

### Warrior Schema

```java
{
  "id": "UUID (auto-generated)",
  "name": "string (required, 1-100 chars)",
  "dob": "LocalDate (required)",
  "fightSkills": "List<String> (optional)"
}
```

### Required API Endpoints

1. **POST /warrior** - Create a new warrior

   - Request: Warrior DTO (without ID)
   - Response: Created warrior with generated UUID
   - Status: 201 Created

2. **GET /warrior/:id** - Get warrior by ID

   - Response: Warrior entity
   - Status: 200 OK or 404 Not Found

3. **GET /warrior?t=[:term]** - Search warriors by term

   - Query param: `t` (search term for name or skills)
   - Response: List of matching warriors
   - Status: 200 OK

4. **GET /counting-warriors** - Get total warrior count

   - Response: `{ "count": number }`
   - Status: 200 OK

5. **GET /health** - Health check endpoint (already implemented)
   - Response: `{ "status": "UP" }`
   - Status: 200 OK

## Coding Guidelines

### 1. Code Style

- Use **Spring Boot best practices** and conventions
- Follow **RESTful API design principles**
- Use **constructor injection** for dependencies (avoid @Autowired on fields)
- Implement **proper exception handling** with @ControllerAdvice
- Add **validation** using Jakarta Bean Validation (@Valid, @NotNull, etc.)

### 2. Entity Design

- Use **UUID** for primary keys (not auto-increment integers)
- Add JPA annotations: @Entity, @Table, @Id, @GeneratedValue
- Include **timestamps** (createdAt, updatedAt) using @CreatedDate, @LastModifiedDate
- Use **Lombok** annotations to reduce boilerplate (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)

### 3. Repository Layer

- Extend **JpaRepository<Warrior, UUID>**
- Use **Spring Data JPA query methods** (findByNameContaining, etc.)
- Add custom queries with @Query when needed
- Implement **pagination** for list endpoints

### 4. Service Layer

- Keep business logic in @Service classes
- Use @Transactional for database operations
- Implement **DTO pattern** for request/response objects
- Add proper error handling (throw custom exceptions)

### 5. Controller Layer

- Use **@RestController** and **@RequestMapping**
- Add **@Valid** for request body validation
- Use proper HTTP status codes (201, 200, 404, 400, 500)
- Implement **ResponseEntity** for flexible responses
- Add **Swagger/OpenAPI** annotations for documentation

### 6. Testing

- Write **unit tests** for services with Mockito
- Write **integration tests** for controllers with @WebMvcTest
- Use **@DataJpaTest** for repository tests
- Target **>80% code coverage**
- Use **AssertJ** for fluent assertions

### 7. Performance Considerations

- Implement **connection pooling** (HikariCP - default in Spring Boot)
- Add **database indexes** on frequently queried fields
- Use **caching** where appropriate (Caffeine or Redis)
- Configure **JVM heap size** appropriately
- Implement **circuit breakers** with Resilience4j for fault tolerance

### 8. Error Handling

- Create custom exception classes (WarriorNotFoundException, ValidationException)
- Use @ControllerAdvice for global exception handling
- Return consistent error response format:
  ```json
  {
  	"timestamp": "ISO-8601",
  	"status": 404,
  	"error": "Not Found",
  	"message": "Warrior not found with id: xyz",
  	"path": "/warrior/xyz"
  }
  ```

## Dependencies to Add

When suggesting code that requires new dependencies, include these in build.gradle:

```groovy
dependencies {
    // Database
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.postgresql:postgresql'

    // Validation
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Utilities
    implementation 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'com.h2database:h2' // For in-memory testing

    // Optional: Performance & Resilience
    implementation 'io.github.resilience4j:resilience4j-spring-boot3:2.1.0'
    implementation 'com.github.ben-manes.caffeine:caffeine:3.1.8'
}
```

## Configuration Guidelines

### application.yml / application.properties

```yaml
spring:
  application:
    name: api-hammer

  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:5432/${DB_NAME:warriors}
    username: ${DB_USER:warrior_user}
    password: ${DB_PASSWORD:warrior_pass}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate # Use Flyway/Liquibase for migrations
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

  jackson:
    serialization:
      write-dates-as-timestamps: false
    time-zone: UTC

server:
  port: 8080
  tomcat:
    threads:
      max: 200
      min-spare: 10
```

## Docker & Deployment

- Application runs in Docker containers with **resource limits**
- Nginx acts as a **load balancer** between multiple app instances
- PostgreSQL runs as a separate container
- Use **environment variables** for configuration (12-factor app)
- Implement **health checks** in docker-compose.yml

## Performance Targets

When implementing features, keep these targets in mind:

- **p95 latency:** < 200ms at 500 concurrent users
- **Error rate:** < 1% under load
- **Throughput:** Maximize requests/second within resource constraints
- **Recovery time:** < 10s after simulated failure

## Current Project State

### ✅ Completed

- Basic Spring Boot structure
- Gradle build configuration
- Docker containerization
- Nginx load balancer
- Health check endpoint

### 🔄 Next Priorities

1. Add PostgreSQL dependencies and configuration
2. Create Warrior entity with JPA annotations
3. Implement WarriorRepository, WarriorService, WarriorController
4. Add request validation and error handling
5. Write unit tests (JUnit + Mockito)
6. Integrate PostgreSQL in docker-compose.yml
7. Implement Gatling load tests

## Special Instructions

- **Always validate input** using Jakarta Bean Validation
- **Use UUIDs** for entity IDs, not Long/Integer
- **Implement pagination** for list endpoints to handle large datasets
- **Add logging** with SLF4J for debugging and monitoring
- **Consider thread safety** when implementing caching or shared state
- **Use CompletableFuture** or reactive patterns for async operations if needed
- **Document APIs** with OpenAPI/Swagger annotations
- **Write tests first** when implementing new features (TDD approach preferred)

## Common Patterns

### Creating a REST endpoint:

1. Define DTO classes for request/response
2. Create repository method if needed
3. Implement service method with business logic
4. Add controller endpoint with proper annotations
5. Write unit tests for service and integration tests for controller
6. Update API documentation

### Adding a new feature:

1. Update domain model (entity)
2. Create/update repository
3. Implement service layer
4. Expose via controller
5. Add validation
6. Write tests
7. Update documentation

## Notes

- This is a **performance-focused** project - always consider impact on throughput and latency
- The system will be tested under **resource constraints** (1.5 CPU, 3GB RAM)
- Focus on **resilience** and graceful degradation under load
- All code should be **production-ready** with proper error handling and logging

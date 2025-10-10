# Project Plan: API Under Stress (Java Implementation)

---

## Overview

This project focuses on designing, building, and stress-testing a resilient REST API using Java.  
The system will manage "warriors," support high concurrency, and sustain performance under CPU and memory constraints.

- **Team:** 2 engineers
- **Language:** Java (Spring Boot)
- **Database:** PostgreSQL (primary)
- **Load Testing:** Gatling
- **Containerization:** Docker Compose (CPU 1.5, Memory 3 GB)
- **Optional:** Nginx reverse proxy

## Current Project Status (Updated: October 7, 2025)

### ✅ **Completed**

- [x] Basic Spring Boot application structure
- [x] Gradle build configuration with Java 17
- [x] Docker containerization setup
- [x] Nginx load balancer configuration
- [x] Health check endpoint (`/health`)
- [x] GitHub Copilot instructions file
- [x] Project documentation structure

### 🔄 **In Progress**

- [ ] Warrior entity and database schema
- [ ] PostgreSQL integration
- [ ] Core API endpoints implementation
- [ ] Unit testing setup

### ⏳ **Not Started**

- [ ] Load testing with Gatling
- [ ] Performance optimization
- [ ] Resource constraint implementation
- [ ] API specification (OpenAPI)

---PI Under Stress (Java Implementation)

---

## Overview

This project focuses on designing, building, and stress-testing a resilient REST API using Java.  
The system will manage “warriors,” support high concurrency, and sustain performance under CPU and memory constraints.

- **Team:** 2 engineers
- **Language:** Java (Spring Boot)
- **Database:** PostgreSQL (primary)
- **Load Testing:** Gatling
- **Containerization:** Docker Compose (CPU 1.5, Memory 3 GB)
- **Optional:** Nginx reverse proxy

---

## Phase 1: Requirements and Design

### Status: 🟡 **Partially Complete**

### Objectives

- Define API routes and schema.
- Establish data model and architecture.
- Prepare system constraints and test goals.

### Key Tasks

- ✅ Select stack: Java + Spring Boot + PostgreSQL.
- ✅ Plan Docker Compose file with resource limits.
- ✅ Create basic project structure and Copilot instructions.
- 🔄 Create warrior schema: `id`, `name`, `dob`, `fight_skills`.
- ❌ Define endpoints:
  - `POST /warrior`
  - `GET /warrior/:id`
  - `GET /warrior?t=[:term]`
  - `GET /counting-warriors`
- ❌ Define Gatling test parameters (users, duration, ramp-up).

### Deliverables

- ✅ Initial Docker Compose skeleton
- ❌ System design document
- ❌ ER diagram
- ❌ API specification (OpenAPI YAML)

**Effort:** ~2 days per teammate

---

## Phase 2: Development and Unit Testing

### Status: 🔴 **Not Started** (Ready to Begin)

### Objectives

- Build and test functional endpoints.
- Validate correctness and database operations.

### Key Tasks

- ❌ Add PostgreSQL and JPA dependencies to build.gradle
- ❌ Implement Warrior entity with JPA annotations
- ❌ Create WarriorRepository interface
- ❌ Implement WarriorService for business logic
- ❌ Create WarriorController with REST endpoints
- ❌ Configure UUID generation
- ❌ Add request validation and error handling
- ❌ Implement unit tests with JUnit and Mockito

### Current Gaps

- **Missing Dependencies:** PostgreSQL driver, Spring Data JPA, validation
- **No Entity Model:** Warrior class needs to be created
- **No Database Configuration:** Application properties for PostgreSQL
- **No REST Controllers:** Only health endpoint exists

### Deliverables

- Working REST API
- Unit test coverage >80%
- Verified POST and GET operations

**Effort:** ~3–4 days per teammate

---

## Phase 3: Containerization and Deployment

### Status: 🟡 **Partially Complete**

### Objectives

- Dockerize API and database.
- Apply CPU and memory limits.

### Key Tasks

- ✅ Create Dockerfile for Java app.
- 🔄 Create Docker Compose for:
  - ✅ API (multiple instances: app1, app2)
  - ✅ Nginx load balancer
  - ❌ PostgreSQL database service
- ✅ Add resource constraints:
  ```yaml
  deploy:
    resources:
      limits:
        cpus: "1.5"
        memory: "3g"
  ```

### Current Status

- **✅ Completed:** Basic Docker setup with dual app instances and Nginx
- **❌ Missing:** PostgreSQL service in docker-compose.yml
- **✅ Missing:** Resource limits and health checks
- **❌ Missing:** Environment variables and configuration

**Effort:** ~2 days total

---

## Phase 4: Load and Stress Testing with Gatling

### Status: 🔴 **Not Started**

### Objectives

- Evaluate performance under load.
- Identify bottlenecks.

### Key Tasks

- Develop Gatling scripts:
  - Simulate 100–1000 concurrent users.
  - Include spike and endurance tests.
- Capture metrics:
  - Response time (p95)
  - Throughput (req/s)
  - Error rates
- Visualize results via Gatling reports.

### Deliverables

- Gatling test scripts
- Performance report (charts, tables)
- Load test summary

**Effort:** ~3 days total

---

## Phase 5: Performance Tuning and Optimization

### Status: 🔴 **Not Started**

### Objectives

- Improve throughput and latency.
- Increase resilience under sustained load.

### Key Tasks

- Optimize database queries (indexes, connection pooling).
- Tune JVM and Spring Boot thread pools.
- Apply caching (e.g., Caffeine or Redis).
- Configure retries and circuit breakers (Resilience4j).
- Re-run Gatling tests post-optimization.

### Deliverables

- Optimized API version
- Before/after performance comparison
- Updated technical documentation

**Effort:** ~3 days total

---

## Phase 6: Final Review, Documentation, and Presentation

### Status: 🔴 **Not Started**

### Objectives

- Prepare final deliverables and reports.
- Present results and lessons learned.

### Key Tasks

- Consolidate documentation (README, API spec, Docker config).
- Prepare a presentation of architecture and performance metrics.
- Record final metrics summary and lessons learned.

### Deliverables

- Final README.md
- Full GitHub repository (code + docs)
- Presentation slides

**Effort:** ~2 days total

---

## 🎯 Immediate Next Steps (Priority Order)

### 1. **Complete Core API Development** (Phase 2)

```bash
# Add required dependencies to build.gradle
- spring-boot-starter-data-jpa
- postgresql
- spring-boot-starter-validation

# Create entities and controllers
- Warrior entity with JPA annotations
- WarriorRepository interface
- WarriorService class
- WarriorController with REST endpoints
```

### 2. **Integrate PostgreSQL** (Phase 3)

```yaml
# Add to docker-compose.yml
postgres:
  image: postgres:15
  environment:
    POSTGRES_DB: warriors
    POSTGRES_USER: warrior_user
    POSTGRES_PASSWORD: warrior_pass
```

### 3. **Add Resource Constraints**

```yaml
# Update docker-compose.yml with limits
deploy:
  resources:
    limits:
      cpus: "1.5"
      memory: "3g"
```

### 4. **Implement Unit Tests**

```bash
# Create test classes
- WarriorControllerTest
- WarriorServiceTest
- WarriorRepositoryTest
```

---

## Success Metrics

- R² > 0.70 for performance predictability.
- <1% error rate under load.
- p95 latency < 200ms at 500 concurrent users.
- Recovery time < 10s after simulated failure.

---

## Tooling Summary

| Category        | Tool                            |
| --------------- | ------------------------------- |
| Language        | Java (Spring Boot)              |
| Testing         | JUnit, Mockito, Gatling         |
| Database        | PostgreSQL                      |
| Deployment      | Docker Compose                  |
| Monitoring      | Prometheus + Grafana (optional) |
| Reverse Proxy   | Nginx                           |
| Version Control | Git + GitHub                    |

# Project Plan: API Under Stress (Java Implementation)

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

### Objectives

- Define API routes and schema.
- Establish data model and architecture.
- Prepare system constraints and test goals.

### Key Tasks

- Create warrior schema: `id`, `name`, `dob`, `fight_skills`.
- Define endpoints:
  - `POST /warrior`
  - `GET /warrior/:id`
  - `GET /warrior?t=[:term]`
  - `GET /counting-warriors`
- Select stack: Java + Spring Boot + PostgreSQL.
- Plan Docker Compose file with resource limits.
- Define Gatling test parameters (users, duration, ramp-up).

### Deliverables

- System design document
- ER diagram
- API specification (OpenAPI YAML)
- Initial Docker Compose skeleton

**Effort:** ~2 days per teammate

---

## Phase 2: Development and Unit Testing

### Objectives

- Build and test functional endpoints.
- Validate correctness and database operations.

### Key Tasks

- Implement CRUD logic using Spring Boot.
- Use JPA for PostgreSQL mapping.
- Configure UUID generation.
- Add request validation and error handling.
- Implement unit tests with JUnit and Mockito.

### Deliverables

- Working REST API
- Unit test coverage >80%
- Verified POST and GET operations

**Effort:** ~3–4 days per teammate

---

## Phase 3: Containerization and Deployment

### Objectives

- Dockerize API and database.
- Apply CPU and memory limits.

### Key Tasks

- Create Dockerfile for Java app.
- Create Docker Compose for:
  - API
  - PostgreSQL
  - Optional Nginx
- Add resource constraints:
  ```yaml
  deploy:
    resources:
      limits:
        cpus: "1.5"
        memory: "3g"
  ```

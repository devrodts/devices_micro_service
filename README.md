# Devices Service

> Version: 1.0 ‑ 14 Jun 2025

## Table of Contents
1. Introduction
2. Solution Overview
3. Architectural Decisions
4. Technology Stack
5. Project Structure
6. Getting Started
   1. Prerequisites
   2. Local Run (H2 profile)
   3. Local Run (PostgreSQL)
   4. Running Tests
   5. Code Quality & Linting
7. API Specification
8. Database & Migrations
9. Observability & Operations
10. Security Considerations
11. Packaging & Deployment
12. Continuous Integration
13. Contributing Guidelines
14. Licensing & Contact

---

## 1  Introduction
The **Devices Service** is a micro-service-oriented system that provides device lifecycle management for IoT/OT scenarios. This repository contains the **Devices Service**, implemented with Spring Boot 3 and Java 21. The service exposes a RESTful API, persists data in PostgreSQL, and offers full documentation through OpenAPI.

## 2  Solution Overview
```
┌──────────────────────────────┐
│  Client / Front-End Apps     │
└──────────────┬───────────────┘
               │HTTPS/REST
┌──────────────▼───────────────┐
│   Devices Service (this)     │
│ ──────────────────────────── │
│  • Controllers (REST)        │
│  • Service Layer             │
│  • Domain Entities           │
│  • JPA Repositories          │
│  • DTO/Mapper (MapStruct)    │
└──────────────┬───────────────┘
               │JDBC
┌──────────────▼───────────────┐
│     PostgreSQL 15.x          │
│   (managed via Flyway)       │
└──────────────────────────────┘
```

## 3  Architectural Decisions
* **Layered (Hexagonal-friendly)**: Presentation → Application → Domain → Infrastructure.
* **DTO boundary**: REST controllers exchange `DeviceDTO`, converted to/from entities with MapStruct to enforce separation.
* **Transactional boundaries**: Service layer annotated with `@Transactional` ensuring ACID semantics.
* **Database migrations**: Flyway tracks schema versions in `src/main/resources/db/migration`.
* **Profile-driven configuration**: `default` (PostgreSQL) and `test` (in-memory H2) profiles.

## 4  Technology Stack
| Area | Choice | Notes |
|------|--------|-------|
| Language | Java 21 | LTS & Loom-ready |
| Framework | Spring Boot 3.2 | Jakarta EE 10 namespace |
| Build | Maven 3.9 | Conventional directory layout |
| Persistence | Spring Data JPA, Hibernate 6 | |
| DB | PostgreSQL 15 / H2 2.x (tests) | |
| Migrations | Flyway 9.22 | SQL-based versioning |
| Mapping | MapStruct 1.5 | Compile-time mapper |
| Boilerplate | Lombok 1.18.38 | Requires IDE plugin |
| Tests | JUnit 5, Mockito, TestContainers | Integration tests spin up ephemeral PG |
| Docs | SpringDoc OpenAPI 2 | Swagger-UI on `/swagger-ui.html` |
| Observability | Spring Boot Actuator | Health, metrics |

## 5  Project Structure
```
.
├── devices/                          # Single gradable module
│   ├── pom.xml
│   ├── README.md                     # Service-specific notes (legacy)
│   └── src/
│       ├── main/
│       │   ├── java/com/devrodts/devices/
│       │   │   ├── application/      # DTO & Service layer
│       │   │   ├── domain/           # JPA entities & repository interfaces
│       │   │   └── infrastructure/   # Config, exception, http layer
│       │   └── resources/
│       │       ├── db/migration/     # Flyway SQL scripts
│       │       ├── application.yml   # Default (PostgreSQL) profile
│       │       └── application-test.yml
│       └── test/                     # Unit & integration tests
└── README.md (this file)
```

## 6  Getting Started
### 6.1  Prerequisites
* JDK 21+
* Maven 3.9+
* **Option A:** Docker (recommended) — simplifies DB provisioning.
* **Option B:** Local PostgreSQL instance (`devices` DB, `postgres/postgres`).

### 6.2  Local Run (H2 profile)
Fast start-up suited for demo/hacking:
```bash
mvn spring-boot:run \
     -Dspring-boot.run.profiles=test
```
### 6.3  Local Run (PostgreSQL)
1. Provision DB (Docker):
   ```bash
   docker-compose up -d
   ```
2. Start service:
   ```bash
   mvn -pl devices spring-boot:run
   ```
Flyway will apply migrations automatically at start-up.

### 6.4  Running Tests
```bash
mvn verify              # Unit + integration (TestContainers)
```
Report summary is printed by Surefire; coverage available in `target/site/jacoco`.

### 6.5  Code Quality & Linting
* Checkstyle & SpotBugs profiles can be activated with `-Pquality` (future roadmap).

## 7  API Specification
After the application boots, explore:
* Swagger-UI: `http://localhost:8080/swagger-ui.html`
* OpenAPI spec: `http://localhost:8080/v3/api-docs`

Core endpoints (`/api/devices`):
| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | List devices |
| GET | `/{id}` | Retrieve device |
| POST | `/` | Create device |
| PUT | `/{id}` | Update device |
| DELETE | `/{id}` | Remove device |
| GET | `/type/{type}` | Filter by type |
| GET | `/status/{status}` | Filter by status |

Refer to Swagger for full schemas & examples.

## 8  Database & Migrations
* All SQL scripts live in `db/migration` and follow Flyway’s `V<version>__description.sql` naming convention.
* Never modify existing migration files in history; always append a new version.

## 9  Observability & Operations
* **Actuator** endpoints enabled under `/actuator` (health, metrics, info).
* Logs: default `INFO`; tune via `logging.level.*` properties.
* Promote log aggregation via ELK / Grafana Loki (outside scope).

## 10  Security Considerations
* Validations enforce request integrity via Jakarta Validation annotations.
* Global exception handling standardises error payloads.
* For production, place the service behind an API-gateway with OAuth2/JWT; sample configs forthcoming.

## 11  Packaging & Deployment
* **Fat-Jar**: `mvn clean package` ➜ `target/devices-<version>.jar`.
* **Docker** (optional):
  ```dockerfile
  FROM eclipse-temurin:21-jre
  COPY target/devices-*.jar app.jar
  ENTRYPOINT ["java","-jar","/app.jar"]
  ```
* **Kubernetes**: Provide Helm chart in `/deploy/helm/devices` (TODO).

## 12  Continuous Integration
* GitHub Actions workflow `ci.yml` (planned) will cache Maven layers, run tests, generate coverage, and build Docker image.


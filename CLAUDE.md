# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JPA2 learning project demonstrating Java Persistence API 2 concepts with Spring Framework and Hibernate. Based on examples from "Pro JPA2 - Mastering the Java Persistence API" book.

## Technology Stack

- Java 17
- Spring Framework 6.2.11 (Context, WebMVC, ORM, Test)
- Hibernate 5.6.15.Final (ORM provider)
- JPA 2.2 (javax.persistence)
- JUnit 4.13.2 for testing
- Maven for build management
- HSQLDB for in-memory test database
- H2 database for runtime

## Build Commands

### Compile
```bash
mvn compile
```

### Run Tests
```bash
mvn test
```

### Run Single Test Class
```bash
mvn test -Dtest=JPA2Test
mvn test -Dtest=RequestTest
```

### Clean and Build
```bash
mvn clean install
```

### Package
```bash
mvn package
```

## Architecture

### Models Layer (`src/main/java/JPA2/models/`)
JPA entity classes demonstrating various relationship types:
- `Employee` - Main entity with examples of all JPA relationship types
  - `@ManyToOne` with Department
  - `@OneToOne` with ParkingSpace
  - `@ManyToMany` with Project
  - `@Embedded` Address
  - `@ElementCollection` for phone numbers map
  - `@Temporal` for date fields
  - `@Enumerated` for EmployeeType
- `Department`, `Project`, `ParkingSpace` - Related entities
- `Address` - Embeddable value object
- `Request` - Separate entity for request handling

### Configuration

#### Spring Configuration (`src/main/java/JPA2/config/MvcConfiguration.java`)
- Component scanning for "JPA2" package
- Spring MVC setup with JSP view resolver
- Views located at `/WEB-INF/views/*.jsp`

#### JPA Configuration (`src/main/resources/test-context.xml`)
- Spring XML-based configuration for JPA/Hibernate
- Transaction management with `@Transactional` support
- Entity scanning for `JPA2.models` package
- DataSource configuration from properties files
- Hibernate schema auto-generation (`hibernate.hbm2ddl.auto`)

#### Database Properties (`src/test/resources/db.properties`)
- HSQLDB in-memory database for tests
- Hibernate dialect: HSQLDialect
- Schema generation: create-drop
- SQL logging enabled

### Testing
- Spring Test Context framework
- `@RunWith(SpringJUnit4ClassRunner.class)` for integration tests
- `@ContextConfiguration` loads test-context.xml
- `@Transactional` for test transaction management
- EntityManager injected via `@PersistenceContext`
- Tests use in-memory HSQLDB database

## Development Notes

### Entity Manager Usage
Tests demonstrate core EntityManager operations:
- `em.persist()` - Create entities
- `em.find()` - Retrieve entities
- `em.remove()` - Delete entities
- `em.flush()` - Force synchronization with database
- Changes to managed entities automatically synchronized without explicit update calls

### Transaction Management
- Use `@Transactional` annotation for test methods
- Set `@Rollback(false)` to persist test data
- Configure propagation with `@Transactional(propagation = Propagation.REQUIRED|NESTED)`
# API Test Framework (RestAssured + TestNG)

## Requirements
- **Java**: JDK **24** (project is currently compiled with `maven.compiler.source/target=24` in `pom.xml`)
- **Maven**: 3.9+

## Configuration (environments)
Tests load environment config from:
- `src/test/resources/config-qa.properties`
- `src/test/resources/config-preprod.properties`

Select env via Maven system property `env`:
- `-Denv=qa` (default)
- `-Denv=preprod`

## Run tests
Run from project root (where `pom.xml` is).

### TestNG suites (smoke vs regression)
This project uses TestNG suite XML files:
- `src/test/resources/testng-smoke.xml` (smoke: only `CreateBookingTest`)
- `src/test/resources/testng-regression.xml` (regression: all tests in package `tests`)

Run via Maven profiles:
- **Smoke**:

```bash
mvn test -Psmoke -Denv=qa
```

- **Regression**:

```bash
mvn test -Pregression -Denv=qa
```

### Run all tests (per env)
- **QA**:

```bash
mvn test -Denv=qa
```

- **Preprod**:

```bash
mvn test -Denv=preprod
```

### Run all tests in a specific test class
Use Surefire `-Dtest`.

```bash
mvn test -Denv=qa -Dtest=tests.DeleteBookingTest
```

### Run a specific test scenario (single `@Test` method)
Use `ClassName#methodName`:

```bash
mvn test -Denv=qa -Dtest=tests.DeleteBookingTest#deleteBooking_should_remove_existing_booking
```

## Logging
Logging uses **SLF4J SimpleLogger** configured in `src/test/resources/simplelogger.properties`.

### Switch INFO ↔ DEBUG
- **Via Maven command (no file changes)**:

```bash
mvn test -Denv=qa -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

- **Via `simplelogger.properties`**:
  - Set `org.slf4j.simpleLogger.defaultLogLevel=debug` (or `info`)

### Enable DEBUG for specific packages only
Add lines like:
- `org.slf4j.simpleLogger.log.request_spec=debug`
- `org.slf4j.simpleLogger.log.tests=debug`

## Timeouts
HTTP client timeouts are externalized per environment in:
- `src/test/resources/config-qa.properties`
- `src/test/resources/config-preprod.properties`

Supported properties (milliseconds):
- `http.timeout.connectMs` (TCP connect timeout)
- `http.timeout.socketMs` (read/response timeout)
- `http.timeout.connectionRequestMs` (time to get a connection from the pool)


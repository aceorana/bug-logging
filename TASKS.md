## TASKS.md – Bug Logging System

A complete breakdown of tasks, stories, and deliverables completed across the 3-day sprint schedule.
This document acts as a historical record of the development process for onboarding developers and project reviewers.

### 🏁 Overview

This project followed a 3-Day Agile Sprint:

#### Day 1

Planning, Environment Setup, Project Skeleton, Domain + Persistence (TDD)

#### Day 2

Service + Controller (TDD), REST API, JSP + jQuery AJAX frontend

#### Day 3

Validation, UX polish, Hardening, Integration Tests, Dockerization, Documentation

### USER STORIES (From Requirements)

##### Story 1 – Create Bug

As a user, I want to submit a new bug so that it can be stored in the system.

##### Story 2 – List Bugs

As a user, I want to view all logged bugs so I can track issues.

##### Story 3 – Filter Bugs by Severity

As a user, I want to filter bugs based on severity to quickly find high-priority issues.

##### Story 4 – API Base URL Configuration

As a developer, I want the client to point to any server address to support separate VM environments.

##### Story 5 – Docker Setup (Nice-to-Have)

As a developer, I want to run the full stack (app + DB) using Docker so onboarding is easier.


### 📅 DAY 1 — Planning, Environment, Domain Layer (TDD)

(Based on Day 1 sprint plan)

##### ✔ 1. Sprint Planning

Reviewed core requirements and the required features.

Defined User Stories + Acceptance Criteria.

Prioritized create → list → filter → config → docker.

##### ✔ 2. Environment Setup

Installed/configured:
* Java 17
* Maven 3.8+
* MySQL local instance
* IntelliJ IDEA
* Verified all installations via command line.

##### ✔ 3. Database Setup

Executed SQL:

    CREATE DATABASE bugtracker CHARSET utf8mb4;
    CREATE USER 'buguser'@'%' IDENTIFIED BY 'bugpass';
    GRANT ALL PRIVILEGES ON bugtracker.* TO 'buguser'@'%';

##### ✔ 4. Project Initialization

Generated Spring Boot skeleton.

Added required dependencies:
* Spring Web
* Spring Data JPA
* MySQL Driver
* JSP support (Jasper)
* Hibernate Validator
* JUnit 5
* Mockito

##### ✔ 5. TDD: Domain + Persistence

###### Tests written first:
* createBug_shouldSaveViaRepository()
* getAllBugs_shouldReturnAllViaRepository()
* getBugsBySeverity_shouldReturnBugsBySeverityViaRepository()

###### Implementation:

* Bug JPA entity w/ fields:
  * id, title, description, severity, status, createdDate
* BugRepository:
  * List<Bug> findBySeverity(String severity)
* Hibernate auto-DDL setup
* Confirmed all JPA tests pass

##### ✔ 6. Version Control Setup

* Initialized Git repo
* Added .gitignore
* First commit
* Connected and pushed to GitHub

#### 📅 DAY 2 — Service Layer, Controller, JSP + AJAX (TDD)

(Based on Day 2 sprint plan)

##### ✔ 1. TDD: Service Layer
Tests:
* createBug_shouldSaveViaRepository
* getAllBugs_shouldReturnAll
* getBugsBySeverity_shouldCallRepository

Implementation:
* BugService + BugServiceImpl

##### ✔ 2. TDD: Controller Layer

Tests using MockMvc:
* POST /api/bugs → returns 201 + response body
* GET /api/bugs → returns all bugs
* GET /api/bugs?severity=HIGH → filters

Implementation:
* BugController (REST)
* BugViewController (JSP loader)

✔ 3. JSP Frontend + jQuery AJAX
Implemented:
* bug-list.jsp
* Bug submission form
* Table for bug listing
* Severity dropdown filter
* AJAX:
  * submit bug
  * refresh bug table
  * filter bugs without page reload

##### ✔ 4. Server URL Configuration

* apiBaseUrl injected into JSP
* Enables running client and server on different VMs

##### ✔ 5. Manual E2E Testing

* Submissions work without reload
* Filters work
* Table updates dynamically

##### ✔ 6. Commit & Push

### 📅 DAY 3 — Validation, UX, Integration, Docker, Documentation

(Day 3 sprint plan)

##### ✔ 1. Validation & Error Handling

Backend:
* Added JSR-303:
  * @NotBlank: title, description
* Added @PrePersist defaults:
  * createdDate
  * default status OPEN
* Added global exception handler:
  * Returns 400 w/ field-level errors

Frontend:
* Displays validation errors from AJAX response
* Submit button disabled while request is running

##### ✔ 2. Additional Tests

* Negative tests:
  * missing title = 400
* Integration test:
  * @SpringBootTest
  * Database interaction validation
* Added manual regression test flow

##### ✔ 3. Dockerization

Created:
* Dockerfile
* docker-compose.yml (MySQL + App)
* Health checks for DB
* Environment variables for DB connection inside container

Verified:

    mvn clean package
    docker-compose up --build

##### ✔ 4. Documentation

* Created README.md
* Created TASKS.md (this file)
* Confirmed onboarding steps

##### ✔ 5. Final Regression Testing

* Verified form → AJAX → DB → reload cycle
* Tested filtering
* Tested Docker build/run
* Tagged release: v1.0.0

### Completed Acceptance Criteria

    Create bug          ✔ Completed
    List bugs           ✔ Completed
    Filter by severity  ✔ Completed
    Client/server configurable URL  ✔ Completed
    Docker setup        ✔ Completed
    Validation          ✔ Completed
    Unit tests          ✔ Completed
    Integration tests   ✔ Completed
    Documentation       ✔ Completed


### Notes for Future Developers

Suggested enhancements:
* Add priority-based sorting
* Add pagination
* Enhance UI styling
* Add login/authentication
* Add status transitions (open → in progress → resolved)
* Add Testcontainers for CI (optional)
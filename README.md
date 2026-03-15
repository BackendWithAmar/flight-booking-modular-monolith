# Flight Booking Platform Backend

A production-style backend system that simulates the architecture of large-scale travel booking platforms.

Examples of similar systems include global travel platforms used by millions of users.

This project demonstrates backend engineering concepts such as:

* Modular Monolith Architecture
* Spring Security Authentication
* JWT-based Authorization
* REST API Design
* Domain-driven service design
* Event-driven architecture (future phase)

---

## Technology Stack

Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA

Database

* PostgreSQL

Future Infrastructure

* Kafka
* Redis
* Docker
* Kubernetes

---

## Current Architecture

The system currently follows a **Modular Monolith architecture**.

Each domain is implemented as an independent module inside a single Spring Boot application.

Modules:

* auth
* user
* search
* booking
* payment
* rewards
* notification

---

## Implemented Features

### Sprint 1 — Authentication System

* User registration
* Password hashing with BCrypt
* Login authentication
* JWT token generation
* JWT token validation
* JWT authentication filter
* Protected API endpoints

---

## Available APIs

User Registration

POST /api/users/register

User Login

POST /api/auth/login

Authenticated Endpoint

GET /api/users/profile

---

## Documentation

Detailed system documentation is available in the `docs` directory.

---

## Current Milestone

Sprint 1 — Authentication System completed.

Next Milestone:

Sprint 2 — Flight Search Module

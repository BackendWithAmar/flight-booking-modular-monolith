# 02 – Architecture Philosophy

This project follows several important backend architecture principles used in production systems.

---

## Domain-Based Architecture

Instead of organizing code by technical layers, the system is organized by **business domains**.

Example modules:

* auth
* user
* search
* booking
* payment
* rewards
* notification

Each domain contains its own:

* controllers
* services
* repositories
* entities
* DTOs

This structure allows easier scaling and later extraction into microservices.

---

## Modular Monolith

The current system runs as a **Modular Monolith**.

Characteristics:

* Single deployable application
* Clear domain boundaries
* Independent business modules
* Shared infrastructure

Benefits:

* Faster development
* Easier debugging
* Clear separation of responsibilities

This approach allows the system to evolve into microservices without major refactoring.

---

## Stateless Authentication

The platform uses **JWT-based authentication**.

Advantages:

* No server-side sessions
* Easy horizontal scaling
* Suitable for microservices architecture

---

## Clean API Design

The API design follows these best practices:

* Use DTOs for request and response objects
* Never expose entities directly in APIs
* Use validation annotations for request validation
* Implement centralized exception handling

---

## Future Scalability

The system is designed so that modules can later become independent services.

Example transformation:

auth module → auth-service
booking module → booking-service
payment module → payment-service

This ensures the architecture remains scalable.

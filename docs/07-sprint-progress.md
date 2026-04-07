# Sprint Progress

## Sprint 0 — Project Foundation

Completed:

* Spring Boot project setup
* Modular project structure
* Domain-based architecture
* PostgreSQL configuration
* Initial repository setup

---

## Sprint 1 — Authentication System

Completed:

* User entity
* User repository
* DTO layer
* Service layer
* Controller layer
* Password hashing
* Login API
* JWT token generation
* JWT authentication filter
* SecurityContext integration
* Protected endpoints

Working APIs:

POST /api/users/register
POST /api/auth/login
GET /api/users/profile

Authentication pipeline fully functional.

---

## Next Sprint

Sprint 2 — Flight Search Module

Planned features:

* Flight entity
* Flight repository
* Flight search API
* Filtering
* Pagination
* Database query optimization

## Sprint 2 — Flight Search Service (Completed)

Features:

- Flight entity and schema design
- Repository with pagination support
- Flight search API (public access)
- DTO-based response mapping
- Basic validation implemented
- End-to-end API testing completed

Known Improvements:

- Global exception handling refinement pending

Status:

✔ Completed and tested

## Next : Enhancement in search API
### Sorting by:
- price
- departureTime
- arrivalTime
- airline
- GET /api/flights/search?departureAirport=BLR&arrivalAirport=DEL&sortBy=price&sortDir=asc

### Data Flow

`
Client -> FlightController(receives params) -> FlightService(validates + builds Sort) -> FlightRepository(executes sorted query) -> Database(returns sorted data) -> Service(maps DTO) -> FlightController(return response)
`
## Sprint 2.1 — Search Sorting (Completed)

Enhancements:

- Added sorting support (price, departureTime, airline)
- Implemented whitelist-based validation for sort fields
- Sorting handled at database level using Pageable
- Verified sorting correctness via API testing

Status:

✔ Completed and tested

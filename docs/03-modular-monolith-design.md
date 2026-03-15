# 03 – Modular Monolith Design

The system currently operates as a **Modular Monolith** implemented with Spring Boot.

All modules exist within a single application but are logically separated.

---

## Package Structure

com.flightbooking

* auth
* user
* search
* booking
* payment
* rewards
* notification
* common
* config

---

## Module Responsibilities

### Auth Module

Handles authentication logic.

Responsibilities:

* Login API
* JWT token generation
* JWT validation
* Authentication filters

---

### User Module

Manages user data and identity.

Responsibilities:

* User registration
* Profile management
* User data storage

---

### Search Module (Upcoming)

Handles flight discovery.

Responsibilities:

* Flight search
* Filtering
* Sorting
* Pagination

---

### Booking Module (Upcoming)

Handles reservation logic.

Responsibilities:

* Create bookings
* Cancel bookings
* Retrieve booking history

---

### Payment Module (Upcoming)

Handles payment processing.

Responsibilities:

* Payment authorization
* Payment success/failure handling

---

### Reward Module (Upcoming)

Handles loyalty programs.

Responsibilities:

* Reward point calculation
* Reward redemption

---

### Notification Module (Upcoming)

Handles communication with users.

Responsibilities:

* Email notifications
* Booking confirmations
* Payment alerts

---

## Shared Components

Common utilities and configurations are placed in shared packages.

Examples:

* exception handling
* JWT utilities
* security configuration

# 04 – Authentication System

The authentication system is implemented during **Sprint 1**.

It provides identity management for users of the flight booking platform.

---

## Features Implemented

* User registration
* Password hashing with BCrypt
* Login authentication
* JWT token generation
* JWT validation
* Authentication filter
* Protected endpoints

---

## APIs

User Registration

POST /api/users/register

User Login

POST /api/auth/login

User Profile

GET /api/users/profile

---

## Password Security

Passwords are stored using **BCrypt hashing**.

Benefits:

* Prevents password leaks
* Resistant to brute-force attacks
* Industry standard approach

---

## Authentication Strategy

The system uses **JWT (JSON Web Tokens)**.

Workflow:

1. User registers
2. User logs in
3. Server generates JWT token
4. Client stores the token
5. Client sends the token in future requests

Header format:

Authorization: Bearer <token>

---

## Security Context

Once a token is validated, an Authentication object is created and stored in:

SecurityContextHolder

This allows Spring Security to treat the request as authenticated.

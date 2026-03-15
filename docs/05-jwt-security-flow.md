# 05 – JWT Security Flow

The platform uses a **JWT-based security filter** to authenticate requests.

---

## Request Lifecycle

Client Request
↓
JWT Authentication Filter
↓
Token Validation
↓
Authentication Object Creation
↓
SecurityContext Update
↓
Controller Execution

---

## Authentication Header

Every authenticated request must include:

Authorization: Bearer <JWT_TOKEN>

---

## JWT Structure

A JWT token consists of three parts:

HEADER
PAYLOAD
SIGNATURE

Example:

header.payload.signature

---

## Token Validation

The filter performs the following steps:

1. Extract Authorization header
2. Check for Bearer token
3. Validate token signature
4. Extract user identity
5. Create Authentication object
6. Store authentication in SecurityContext

---

## Security Outcome

Valid Token

User is authenticated and the request proceeds.

Invalid Token

The request is rejected with:

401 Unauthorized

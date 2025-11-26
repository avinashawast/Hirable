# Authentication and Security Setup

This document describes the authentication and security implementation for the Hirable platform.

## Overview

The authentication system uses JWT (JSON Web Tokens) for stateless authentication. Users authenticate with hardcoded credentials (for demo purposes) and receive a JWT token that is used for subsequent API requests.

## Backend Implementation

### Components

1. **User Entity** (`com.hirable.entity.User`)
   - Stores user information: username, password hash, role, email, active status
   - Supports three roles: ADMIN, JOB_SEEKER, RECRUITER

2. **JWT Utility** (`com.hirable.security.JwtUtil`)
   - Generates JWT tokens with user ID, username, and role
   - Validates tokens and extracts claims
   - Uses HS512 algorithm for signing

3. **JWT Authentication Filter** (`com.hirable.security.JwtAuthenticationFilter`)
   - Intercepts requests and extracts JWT token from Authorization header
   - Validates token and sets Spring Security authentication context
   - Allows unauthenticated access to `/api/auth/**` and `/ws/**` endpoints

4. **Security Configuration** (`com.hirable.config.SecurityConfig`)
   - Configures Spring Security with stateless session management
   - Registers JWT authentication filter
   - Defines public and protected endpoints
   - Enables method-level security with `@PreAuthorize` annotations

5. **Authentication Service** (`com.hirable.service.AuthenticationService`)
   - Handles login logic with hardcoded credential validation
   - Initializes demo users on application startup
   - Updates last login timestamp

6. **Authentication Controller** (`com.hirable.controller.AuthenticationController`)
   - Exposes `/api/auth/login` endpoint
   - Accepts LoginRequest with username and password
   - Returns LoginResponse with JWT token and user details

### Demo Credentials

The system initializes three demo users on startup:

- **Admin**: admin@hirable.com / admin123
- **Recruiter**: recruiter@techcorp.com / recruiter123
- **Job Seeker**: jobseeker@email.com / jobseeker123

### Configuration

JWT settings in `application.properties`:
```properties
hirable.jwt.secret=demo-secret-key-change-in-production-environment
hirable.jwt.expiration=86400000  # 24 hours in milliseconds
```

## Frontend Implementation

### Components

1. **Auth Service** (`src/app/services/auth.service.ts`)
   - Handles login requests to backend
   - Stores JWT token and user information in localStorage
   - Provides methods to check authentication status and user role
   - Manages logout functionality

2. **Auth Interceptor** (`src/app/interceptors/auth.interceptor.ts`)
   - Automatically adds JWT token to Authorization header for all HTTP requests
   - Handles 401 Unauthorized responses by logging out user and redirecting to login

3. **Auth Guard** (`src/app/guards/auth.guard.ts`)
   - Protects routes that require authentication
   - Supports role-based route protection
   - Redirects unauthenticated users to login page

4. **Login Component** (`src/app/components/login/login.component.ts`)
   - Provides login form with username and password fields
   - Displays demo credentials for reference
   - Shows loading spinner during authentication
   - Displays error messages on failed login
   - Redirects to dashboard on successful login

### Token Storage

JWT token and user information are stored in browser localStorage:
- `auth_token`: JWT token
- `user_role`: User role (ADMIN, JOB_SEEKER, RECRUITER)
- `user_id`: User ID
- `username`: Username

### HTTP Interceptor

The AuthInterceptor automatically:
1. Adds `Authorization: Bearer <token>` header to all HTTP requests
2. Handles 401 responses by clearing stored credentials and redirecting to login

## API Endpoints

### Authentication

**POST /api/auth/login**
- Request: `{ "username": "string", "password": "string" }`
- Response: `{ "token": "string", "role": "string", "userId": number, "username": "string" }`
- Status: 200 OK on success, 500 on error

### Protected Endpoints

All other endpoints require a valid JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

## Role-Based Access Control

### Backend

Use `@PreAuthorize` annotation on controller methods:
```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() { }

@PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
public void recruiterMethod() { }
```

### Frontend

Check user role in components:
```typescript
if (this.authService.hasRole('ADMIN')) {
  // Show admin features
}

if (this.authService.hasAnyRole('RECRUITER', 'ADMIN')) {
  // Show recruiter features
}
```

## Security Features

1. **Password Hashing**: Passwords are hashed using BCrypt before storage
2. **JWT Signing**: Tokens are signed with HS512 algorithm
3. **Token Expiration**: Tokens expire after 24 hours
4. **CORS Configuration**: Configured to allow requests from frontend
5. **CSRF Protection**: Disabled for stateless JWT authentication
6. **Stateless Sessions**: No server-side session storage

## Testing

Run authentication tests:
```bash
cd backend
./mvnw test -Dtest=AuthenticationControllerTest
```

Tests cover:
- Valid login with all three user roles
- Invalid credentials
- Non-existent user
- Token generation and validation

## Security Considerations for Production

1. **Change JWT Secret**: Update `hirable.jwt.secret` to a strong, random value
2. **Use HTTPS**: Ensure all communication is encrypted
3. **Implement Real Authentication**: Replace hardcoded credentials with database validation
4. **Add Password Reset**: Implement secure password reset flow
5. **Add Rate Limiting**: Prevent brute force attacks on login endpoint
6. **Add Audit Logging**: Log all authentication attempts
7. **Implement Refresh Tokens**: Add token refresh mechanism for better security
8. **Add Two-Factor Authentication**: Implement 2FA for enhanced security

## Troubleshooting

### Token Not Being Sent

Ensure the Authorization header is being added by the interceptor. Check browser DevTools Network tab to verify the header is present.

### 401 Unauthorized Errors

1. Check if token has expired (24 hours)
2. Verify token is stored in localStorage
3. Check if Authorization header is being sent correctly
4. Verify backend is validating token correctly

### Login Fails

1. Verify credentials match demo credentials exactly
2. Check backend is running on port 8080
3. Check CORS configuration allows frontend origin
4. Check browser console for error messages

## Next Steps

After authentication is working:
1. Implement role-based dashboards
2. Add protected routes for each role
3. Implement user profile management
4. Add logout functionality to navigation
5. Implement password change functionality

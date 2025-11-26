# Hirable - Talent Pool Management Solution

A web-based job matching platform that connects Job Seekers with Recruiters under Admin oversight. The system enables resume management, job postings, candidate search, and real-time chat communication.

## Table of Contents

- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Backend Setup](#backend-setup)
- [Frontend Setup](#frontend-setup)
- [Running the Application](#running-the-application)
- [Configuration](#configuration)
- [Demo Credentials](#demo-credentials)
- [Database Setup](#database-setup)
- [Building for Production](#building-for-production)
- [Troubleshooting](#troubleshooting)

## Project Overview

Hirable is a three-tier web application that provides:

- **Job Seeker Features**: Profile management, resume upload, job search, applications, real-time chat
- **Recruiter Features**: Job posting, candidate search with relevance ranking, shortlist management, real-time chat
- **Admin Features**: User management, job moderation, talent pool oversight, system configuration, analytics, chat moderation

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL 14+
- **Build Tool**: Maven
- **Key Libraries**:
  - Spring Security with JWT authentication
  - Spring WebSocket for real-time chat
  - Apache PDFBox and POI for resume parsing
  - Flyway for database migrations

### Frontend
- **Framework**: Angular 17
- **Language**: TypeScript
- **UI Library**: Angular Material
- **Real-time Communication**: WebSocket (SockJS + STOMP)
- **Build Tool**: Angular CLI

### Database
- PostgreSQL 14 or higher

## Prerequisites

### System Requirements
- Java 17 or higher
- Node.js 18+ and npm 9+
- PostgreSQL 14+
- Maven 3.8+
- Git

### Installation

#### Java
```bash
# Verify Java installation
java -version
```

#### Node.js and npm
```bash
# Verify Node.js and npm installation
node --version
npm --version
```

#### PostgreSQL
```bash
# Verify PostgreSQL installation
psql --version
```

#### Maven
```bash
# Verify Maven installation
mvn --version
```

## Project Structure

```
hirable/
├── backend/                          # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/hirable/    # Java source code
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── application-dev.properties
│   │   │       ├── application-prod.properties
│   │   │       └── db/migration/    # Flyway migrations
│   │   └── test/                    # Test files
│   └── pom.xml                      # Maven configuration
├── frontend/                         # Angular frontend
│   ├── src/
│   │   ├── app/                     # Angular components and services
│   │   └── assets/                  # Static assets
│   ├── angular.json                 # Angular configuration
│   ├── package.json                 # npm dependencies
│   └── tsconfig.json                # TypeScript configuration
├── docker-compose.yml               # Docker Compose for PostgreSQL
├── README.md                        # This file
└── LICENSE                          # License information
```

## Backend Setup

### 1. Navigate to Backend Directory
```bash
cd backend
```

### 2. Build the Backend
```bash
# Build with Maven
mvn clean install

# Or skip tests during build (faster)
mvn clean install -DskipTests
```

### 3. Run the Backend

#### Development Environment
```bash
# Using Maven with dev profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or using Java directly after building
java -jar target/hirable-backend-1.0.0.jar --spring.profiles.active=dev
```

#### Production Environment
```bash
# Using Maven with prod profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Or using Java directly after building
java -jar target/hirable-backend-1.0.0.jar --spring.profiles.active=prod
```

The backend will start on `http://localhost:8080`

## Frontend Setup

### 1. Navigate to Frontend Directory
```bash
cd frontend
```

### 2. Install Dependencies
```bash
npm install
```

### 3. Run the Frontend

#### Development Server
```bash
# Start the development server
npm start

# Or using Angular CLI directly
ng serve

# Access the application at http://localhost:4200
```

#### Production Build
```bash
# Build for production
npm run build

# Or using Angular CLI directly
ng build --configuration production

# Output will be in dist/ directory
```

## Running the Application

### Prerequisites
1. PostgreSQL must be running
2. Database must be created and initialized

### Quick Start (Development)

#### Option 1: Using Docker Compose for Database
```bash
# Start PostgreSQL using Docker Compose
docker-compose up -d

# Wait for PostgreSQL to be ready (about 10 seconds)
```

#### Option 2: Manual PostgreSQL Setup
```bash
# Create database and user
psql -U postgres -c "CREATE DATABASE hirable;"
psql -U postgres -c "CREATE USER hirable_user WITH PASSWORD 'hirable_pass';"
psql -U postgres -c "ALTER ROLE hirable_user WITH CREATEDB;"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE hirable TO hirable_user;"
```

### Start Backend
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Start Frontend (in a new terminal)
```bash
cd frontend
npm start
```

### Access the Application
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- WebSocket: ws://localhost:8080/ws

## Configuration

### Backend Configuration Files

#### application.properties (Default)
Main configuration file with default settings. Loads environment-specific profiles.

#### application-dev.properties (Development)
Development environment configuration with:
- Local PostgreSQL database
- Debug logging enabled
- Relaxed CORS settings for localhost
- Development JWT secret

#### application-prod.properties (Production)
Production environment configuration with:
- Production database settings (must be updated)
- Minimal logging
- Strict CORS settings
- Security headers enabled
- **Important**: Update the following before deploying:
  - `spring.datasource.url`: Production database URL
  - `spring.datasource.username`: Production database user
  - `spring.datasource.password`: Production database password
  - `hirable.jwt.secret`: Strong secret key
  - `spring.websocket.allowed-origins`: Your production domain
  - `spring.web.cors.allowed-origins`: Your production domain

### Key Configuration Properties

#### File Upload
```properties
hirable.file.upload-dir=./uploads/resumes    # Directory for resume storage
hirable.file.max-size=5242880                # Max file size in bytes (5MB)
```

#### JWT Authentication
```properties
hirable.jwt.secret=your-secret-key           # Secret key for JWT signing
hirable.jwt.expiration=86400000              # Token expiration in milliseconds (24 hours)
```

#### WebSocket
```properties
spring.websocket.allowed-origins=http://localhost:4200  # Allowed origins for WebSocket
```

#### Database
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hirable
spring.datasource.username=hirable_user
spring.datasource.password=hirable_pass
```

### Changing Active Profile

#### At Runtime
```bash
# Backend
java -jar target/hirable-backend-1.0.0.jar --spring.profiles.active=prod

# Or with Maven
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

#### Via Environment Variable
```bash
export SPRING_PROFILES_ACTIVE=prod
java -jar target/hirable-backend-1.0.0.jar
```

#### Via application.properties
Edit `backend/src/main/resources/application.properties`:
```properties
spring.profiles.active=prod
```

## Demo Credentials

The system includes sample data with the following demo credentials:

### Admin
- **Username**: admin@hirable.com
- **Password**: admin123

### Recruiter
- **Username**: recruiter@techcorp.com
- **Password**: recruiter123

### Job Seeker
- **Username**: jobseeker@email.com
- **Password**: jobseeker123

**Note**: These are hardcoded credentials for demo purposes only. Replace with proper authentication in production.

## Database Setup

### Automatic Setup (Recommended)
Database schema and sample data are automatically created on first run using Flyway migrations:
- `V1__initial_schema.sql`: Creates all tables and indexes
- `V2__sample_data.sql`: Inserts sample data for demo

### Manual Setup
If you need to manually set up the database:

```bash
# Connect to PostgreSQL
psql -U hirable_user -d hirable

# Run migration scripts manually
\i backend/src/main/resources/db/migration/V1__initial_schema.sql
\i backend/src/main/resources/db/migration/V2__sample_data.sql
```

### Resetting the Database
```bash
# Drop and recreate the database
psql -U postgres -c "DROP DATABASE IF EXISTS hirable;"
psql -U postgres -c "CREATE DATABASE hirable;"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE hirable TO hirable_user;"

# Restart the backend to run migrations
```

## Building for Production

### Backend Production Build

```bash
cd backend

# Build JAR file
mvn clean package -DskipTests

# JAR file will be created at: target/hirable-backend-1.0.0.jar

# Run with production profile
java -jar target/hirable-backend-1.0.0.jar --spring.profiles.active=prod
```

### Frontend Production Build

```bash
cd frontend

# Install dependencies
npm install

# Build for production
npm run build

# Output will be in dist/hirable-frontend/ directory

# Serve with a web server (e.g., nginx, Apache, or Node.js)
# Example with Node.js http-server:
npx http-server dist/hirable-frontend/ -p 80
```

### Docker Deployment (Optional)

Create a `Dockerfile` for the backend:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/hirable-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

Build and run:
```bash
docker build -t hirable-backend .
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/hirable hirable-backend
```

## Troubleshooting

### Backend Issues

#### Port 8080 Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change the port in application.properties
server.port=8081
```

#### Database Connection Failed
```
Error: org.postgresql.util.PSQLException: Connection to localhost:5432 refused
```

**Solution**:
1. Verify PostgreSQL is running: `psql -U postgres`
2. Check database exists: `psql -U postgres -l | grep hirable`
3. Verify credentials in application.properties
4. Check PostgreSQL is listening on port 5432

#### JWT Token Expired
- Tokens expire after 24 hours by default
- Change expiration in application.properties: `hirable.jwt.expiration=<milliseconds>`
- Re-login to get a new token

### Frontend Issues

#### Port 4200 Already in Use
```bash
# Use a different port
ng serve --port 4201
```

#### CORS Errors
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solution**:
1. Verify backend CORS configuration in application.properties
2. Check `spring.web.cors.allowed-origins` includes your frontend URL
3. Restart backend after configuration changes

#### WebSocket Connection Failed
```
WebSocket connection to 'ws://localhost:8080/ws' failed
```

**Solution**:
1. Verify backend is running
2. Check WebSocket endpoint is accessible: `curl http://localhost:8080/ws`
3. Verify `spring.websocket.allowed-origins` in application.properties

### Database Issues

#### Flyway Migration Failed
```
Error: Flyway migration failed
```

**Solution**:
1. Check migration files in `backend/src/main/resources/db/migration/`
2. Verify database user has proper permissions
3. Check PostgreSQL logs for detailed error messages
4. Reset database and restart backend

#### File Upload Issues

#### Resume Upload Fails
```
Error: File upload directory not found
```

**Solution**:
1. Create upload directory: `mkdir -p ./uploads/resumes`
2. Verify directory permissions: `chmod 755 ./uploads/resumes`
3. Check `hirable.file.upload-dir` in application.properties

#### File Size Limit Exceeded
```
Error: File size exceeds maximum allowed size
```

**Solution**:
- Increase `hirable.file.max-size` in application.properties (value in bytes)
- Default is 5242880 bytes (5MB)

## Support and Documentation

For more information, see:
- [Design Document](.kiro/specs/hirable-platform/design.md)
- [Requirements Document](.kiro/specs/hirable-platform/requirements.md)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Angular Documentation](https://angular.io/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## License

See LICENSE file for details.

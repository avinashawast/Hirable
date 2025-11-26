# Hirable Platform - Setup and Build Instructions

## Prerequisites

- Java 17 or higher
- Node.js 18+ and npm
- PostgreSQL 14+
- Maven 3.8+
- Angular CLI 17+

## Database Setup

1. Create PostgreSQL database and user:
```sql
CREATE USER hirable_user WITH PASSWORD 'hirable_pass';
CREATE DATABASE hirable OWNER hirable_user;
```

2. Ensure PostgreSQL is running on localhost:5432

## Backend Setup

1. Navigate to backend directory:
```bash
cd backend
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

Database migrations will run automatically on startup using Flyway.

## Frontend Setup

1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will be available at `http://localhost:4200`

## Project Structure

### Backend
```
backend/
├── src/main/java/com/hirable/
│   ├── config/              # Configuration classes
│   ├── controller/          # REST API endpoints
│   ├── service/             # Business logic
│   ├── repository/          # Data access layer
│   ├── entity/              # JPA entities
│   ├── dto/                 # Data transfer objects
│   └── exception/           # Custom exceptions
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/        # Flyway SQL migrations
└── pom.xml
```

### Frontend
```
frontend/
├── src/
│   ├── app/
│   │   ├── components/      # Angular components
│   │   ├── services/        # Angular services
│   │   ├── models/          # TypeScript interfaces
│   │   ├── app.component.ts
│   │   └── app.routes.ts
│   ├── assets/              # Static assets
│   ├── styles.scss          # Global styles
│   └── main.ts
├── angular.json
├── tsconfig.json
└── package.json
```

## Configuration

### Backend Configuration (application.properties)
- Database URL: `jdbc:postgresql://localhost:5432/hirable`
- Database User: `hirable_user`
- Database Password: `hirable_pass`
- JWT Secret: `demo-secret-key-change-in-production-environment`
- File Upload Directory: `./uploads/resumes`
- WebSocket Allowed Origins: `http://localhost:4200,http://localhost:3000`

### Frontend Configuration
- API Base URL: `http://localhost:8080`
- WebSocket URL: `http://localhost:8080/ws`

## Demo Credentials

After sample data is loaded, use these credentials:

**Admin:**
- Username: `admin@hirable.com`
- Password: `admin123`

**Recruiter:**
- Username: `recruiter@techcorp.com`
- Password: `recruiter123`

**Job Seeker:**
- Username: `jobseeker@email.com`
- Password: `jobseeker123`

## Build for Production

### Backend
```bash
cd backend
./mvnw clean package -DskipTests
```

### Frontend
```bash
cd frontend
npm run build
```

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database credentials in `application.properties`
- Check that the database `hirable` exists

### Port Already in Use
- Backend: Change `server.port` in `application.properties`
- Frontend: Use `ng serve --port 4201`

### CORS Issues
- Verify frontend URL is in `spring.websocket.allowed-origins`
- Check CORS configuration in `CorsConfig.java`

## Next Steps

1. Load sample data by running the backend (migrations run automatically)
2. Access the frontend at `http://localhost:4200`
3. Login with demo credentials
4. Explore the platform features

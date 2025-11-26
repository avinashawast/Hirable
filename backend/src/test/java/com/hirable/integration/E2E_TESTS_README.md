# End-to-End Tests for Hirable Platform

## Overview

The `UserJourneyE2ETest` class contains comprehensive end-to-end tests that simulate complete user journeys across all three user roles in the Hirable platform: Job Seeker, Recruiter, and Admin.

## Test Coverage

### 1. Job Seeker Complete Journey
**Test Method:** `testJobSeekerCompleteJourney()`

This test simulates a realistic Job Seeker workflow:
1. **Login** - Job Seeker authenticates with credentials
2. **Resume Upload** - Job Seeker uploads a PDF resume
3. **Job Search** - Job Seeker searches for jobs by keyword and location
4. **Job Application** - Job Seeker applies to a job posting
5. **Chat Reception** - Job Seeker receives and views chat messages from recruiter

**Requirements Covered:** 1.1, 1.2, 2.1, 2.3, 3.1, 3.3, 7.1, 7.4

### 2. Recruiter Complete Journey
**Test Method:** `testRecruiterCompleteJourney()`

This test simulates a realistic Recruiter workflow:
1. **Login** - Recruiter authenticates with credentials
2. **Job Posting** - Recruiter creates a new job posting
3. **Job Approval** - Admin approves the job posting
4. **Candidate Search** - Recruiter searches for candidates by skills and location
5. **Chat Initiation** - Recruiter initiates chat with a candidate
6. **Candidate Shortlist** - Recruiter shortlists a candidate for future reference

**Requirements Covered:** 1.1, 4.1, 4.2, 5.1, 5.2, 6.1, 7.1, 7.2

### 3. Admin Complete Journey
**Test Method:** `testAdminCompleteJourney()`

This test simulates a realistic Admin workflow:
1. **Login** - Admin authenticates with credentials
2. **Job Review** - Admin retrieves pending job postings
3. **Job Approval** - Admin approves a job posting
4. **Analytics Viewing** - Admin views platform analytics and metrics
5. **Chat Moderation** - Admin views all chats and flags inappropriate conversations

**Requirements Covered:** 1.1, 10.1, 10.2, 13.1, 14.1, 14.3

### 4. Responsive Design Tests

#### Mobile Viewport Test
**Test Method:** `testResponsiveDesignMobileViewport()`

Verifies that all API endpoints work correctly for mobile clients:
- Login functionality
- Job search
- Profile retrieval

#### Tablet Viewport Test
**Test Method:** `testResponsiveDesignTabletViewport()`

Verifies that all API endpoints work correctly for tablet clients:
- Login functionality
- Candidate search
- Recruiter dashboard data

#### Desktop Viewport Test
**Test Method:** `testResponsiveDesignDesktopViewport()`

Verifies that all API endpoints work correctly for desktop clients:
- Login functionality
- Admin analytics
- User management

**Requirements Covered:** 17.1, 17.2, 17.3, 17.4, 17.5

### 5. Multi-Step User Journeys

#### Complete Job Application Workflow
**Test Method:** `testCompleteJobApplicationWorkflow()`

Tests the complete job application process:
1. Recruiter posts a job
2. Admin approves the job
3. Job Seeker searches and finds the job
4. Job Seeker applies to the job
5. Verify application is created in database

#### Candidate Search and Shortlist Workflow
**Test Method:** `testCandidateSearchAndShortlistWorkflow()`

Tests the candidate management process:
1. Recruiter searches for candidates
2. Recruiter shortlists a candidate
3. Recruiter views shortlist
4. Recruiter removes candidate from shortlist
5. Verify shortlist is updated

#### Chat Conversation Workflow
**Test Method:** `testChatConversationWorkflow()`

Tests the chat messaging system:
1. Create a chat between recruiter and job seeker
2. Add multiple messages to the chat
3. Retrieve chat messages
4. Verify message persistence in database

## Test Architecture

### Technology Stack
- **Framework:** Spring Boot Test with MockMvc
- **Testing Library:** JUnit 5
- **Assertion Library:** JUnit Assertions
- **Database:** H2 in-memory database (test profile)

### Test Setup
Each test class uses:
- `@SpringBootTest` - Loads full application context
- `@AutoConfigureMockMvc` - Configures MockMvc for HTTP testing
- `@ActiveProfiles("test")` - Uses test configuration profile
- `@BeforeEach` - Initializes test data before each test

### Data Initialization
- All repositories are cleaned before each test
- Demo users are initialized (Admin, Recruiter, Job Seeker)
- JWT tokens are obtained for each role
- Entity IDs are extracted for use in test scenarios

## Running the Tests

### Run all E2E tests
```bash
mvn test -Dtest=UserJourneyE2ETest
```

### Run specific test method
```bash
mvn test -Dtest=UserJourneyE2ETest#testJobSeekerCompleteJourney
```

### Run with coverage report
```bash
mvn test -Dtest=UserJourneyE2ETest jacoco:report
```

## Test Assertions

Each test verifies:
1. **HTTP Status Codes** - Correct response status (200, 201, 400, 401, etc.)
2. **Response Structure** - JSON response contains expected fields
3. **Data Persistence** - Data is correctly stored in database
4. **Business Logic** - Workflows execute in correct order
5. **Authorization** - Role-based access control is enforced

## Key Features Tested

### Authentication
- Login with valid credentials
- JWT token generation and validation
- Role-based access control

### Job Management
- Job posting creation
- Job approval workflow
- Job search and filtering
- Job application process

### Candidate Management
- Candidate search with relevance ranking
- Candidate shortlisting
- Candidate profile viewing

### Chat System
- Chat creation between users
- Message sending and persistence
- Message retrieval and history
- Chat moderation and flagging

### File Management
- Resume upload (PDF, DOC, DOCX formats)
- File validation and size limits
- Resume download

### Analytics
- Platform metrics retrieval
- User activity statistics
- Job posting statistics

## Responsive Design Testing

The tests simulate different client types by setting User-Agent headers:
- **Mobile:** "Mobile Safari" - Tests mobile client compatibility
- **Tablet:** "iPad Safari" - Tests tablet client compatibility
- **Desktop:** "Chrome Desktop" - Tests desktop client compatibility

All endpoints return consistent API responses regardless of client type, ensuring the frontend can adapt the UI appropriately.

## Notes

- Tests use MockMvc for HTTP testing without requiring a running server
- Database is reset before each test to ensure test isolation
- Tests use hardcoded demo credentials for authentication
- WebSocket functionality is tested through database message persistence
- File uploads are tested with mock file objects
- All tests are independent and can run in any order

## Future Enhancements

- Add Cypress E2E tests for actual UI testing
- Add performance testing for search and analytics endpoints
- Add load testing for concurrent user scenarios
- Add security testing for authentication and authorization
- Add API contract testing with OpenAPI/Swagger

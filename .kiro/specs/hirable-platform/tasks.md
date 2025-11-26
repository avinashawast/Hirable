# Implementation Plan

- [x] 1. Set up project structure and dependencies





  - Create Spring Boot backend project with Maven dependencies (Spring Web, Spring Security, Spring Data JPA, Spring WebSocket, PostgreSQL driver, Apache PDFBox, Apache POI)
  - Create Angular frontend project with Angular CLI and install dependencies (Angular Material, RxJS, SockJS, STOMP)
  - Configure database connection properties and create initial database schema
  - Set up CORS configuration to allow frontend-backend communication
  - _Requirements: 1.1, 1.3, 17.1, 17.2, 17.3_

- [x] 2. Implement authentication and security foundation





  - Create User entity with username, password hash, role, email, and active status fields
  - Implement JWT token generation and validation utilities
  - Create authentication endpoint `/api/auth/login` with hardcoded credentials validation
  - Implement JWT authentication filter to validate tokens on protected endpoints
  - Create role-based access control annotations for ADMIN, JOB_SEEKER, and RECRUITER roles
  - Build LoginComponent in Angular with form validation and token storage
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 18.1, 18.3_

- [x] 3. Build Job Seeker profile and resume management





  - Create JobSeeker, Experience, and Education entities with relationships to User
  - Implement JobSeekerService with methods to create, update, and retrieve profiles
  - Create REST endpoints for Job Seeker profile operations (`GET/PUT /api/jobseekers/{id}/profile`)
  - Implement file upload endpoint for resume (`POST /api/jobseekers/{id}/resume`) with validation for PDF/DOC formats and 5MB size limit
  - Create ResumeService with resume parsing logic using Apache PDFBox and POI to extract skills, experience, and education
  - Implement relevance score update logic based on resume upload timestamp
  - Build ProfileComponent in Angular for editing personal information, skills, experience, and education
  - Build ResumeUploadComponent with file selection, upload progress, and success/error handling
  - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 15.1, 15.2, 15.3, 15.4, 15.5, 18.2_

- [x] 4. Implement job posting and management for recruiters





  - Create Recruiter and Job entities with status field (PENDING, APPROVED, REJECTED, REMOVED)
  - Implement RecruiterService and JobService with CRUD operations
  - Create REST endpoints for recruiter profile (`GET/PUT /api/recruiters/{id}/profile`)
  - Create REST endpoints for job management (`GET/POST /api/recruiters/{id}/jobs`, `PUT/DELETE /api/recruiters/{id}/jobs/{jobId}`)
  - Implement job approval workflow endpoints for Admin (`GET /api/jobs/pending`, `PUT /api/jobs/{id}/approve`, `PUT /api/jobs/{id}/reject`)
  - Build CompanyProfileComponent in Angular for editing recruiter and company information
  - Build JobPostingFormComponent with form fields for title, description, skills, location, experience level, and industry
  - Build RecruiterDashboardComponent displaying list of posted jobs with status indicators
  - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 10.1, 10.2, 10.3, 10.4, 10.5_

- [x] 5. Create job search and application functionality





  - Implement job search endpoint (`GET /api/jobs`) with query parameters for keyword, location, industry, and experience level
  - Create Application entity linking JobSeeker and Job with application status
  - Implement job application endpoint (`POST /api/jobseekers/{id}/applications`) that creates application record
  - Add database indexes on Job.status and Job.postedAt for search performance
  - Implement pagination for search results (20 items per page)
  - Build JobSearchComponent in Angular with search filters and results display
  - Build JobDetailsComponent showing job information with apply button
  - Build JobSeekerDashboardComponent displaying list of submitted applications
  - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5_

- [x] 6. Implement candidate search with relevance ranking





  - Create CandidateSearchService with relevance score calculation algorithm
  - Implement keyword matching logic comparing candidate skills with search criteria (50% weight)
  - Implement recency scoring based on resume upload timestamp (30% weight)
  - Implement location matching logic (20% weight)
  - Create candidate search endpoint (`GET /api/candidates/search`) with skills, experience, and location parameters
  - Add database indexes on JobSeeker.skills and JobSeeker.resumeUploadedAt for search performance
  - Build CandidateSearchComponent in Angular with search filters and ranked results display
  - Build CandidateProfileComponent showing candidate details with resume download link
  - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

- [x] 7. Build shortlist management for recruiters





  - Create Shortlist entity linking Recruiter and JobSeeker with timestamp
  - Implement shortlist endpoints (`GET/POST /api/recruiters/{id}/shortlist`, `DELETE /api/recruiters/{id}/shortlist/{candidateId}`)
  - Add database index on Shortlist.recruiterId for query performance
  - Build ShortlistComponent in Angular displaying shortlisted candidates with remove functionality
  - Add shortlist action buttons to CandidateSearchComponent and CandidateProfileComponent
  - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

- [x] 8. Implement real-time chat system





  - Create Chat and Message entities with relationships to User, Recruiter, and JobSeeker
  - Configure WebSocket with STOMP protocol and message broker for `/queue` and `/topic` destinations
  - Implement ChatService with methods to create chats, send messages, and retrieve chat history
  - Create WebSocket message handler for `/app/chat.send` that persists messages and broadcasts to recipients
  - Create REST endpoints for chat operations (`GET /api/chats/{userId}`, `POST /api/chats`, `GET /api/chats/{chatId}/messages`)
  - Add database indexes on Message.chatId and Message.sentAt for message retrieval performance
  - Build ChatComponent in Angular with WebSocket client, message list, and send message form
  - Implement message persistence and real-time delivery to recipient's queue `/user/{userId}/queue/messages`
  - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5_

- [x] 9. Create notification system




  - Create Notification entity with user reference, type, message, read status, and timestamp
  - Implement NotificationService with methods to create, retrieve, and mark notifications as read
  - Create notification endpoints (`GET /api/notifications/{userId}`, `PUT /api/notifications/{id}/read`, `GET /api/notifications/{userId}/unread-count`)
  - Add notification triggers for job applications, recruiter interest, and chat messages
  - Add database index on Notification.userId and Notification.read for query performance
  - Build NotificationComponent in Angular displaying notification list with unread count badge
  - Implement auto-refresh of notifications every 30 seconds
  - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

- [x] 10. Build admin user management





  - Implement admin endpoints for user CRUD operations (`GET /api/users`, `POST /api/users`, `PUT /api/users/{id}`, `DELETE /api/users/{id}`)
  - Add role-based access control to restrict user management endpoints to ADMIN role only
  - Implement user deactivation logic that sets active flag to false and prevents login
  - Build UserManagementComponent in Angular with user list, create user form, edit user form, and deactivate button
  - Add user search and filter functionality by role and status
  - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5_

- [x] 11. Implement admin job management and moderation





  - Create JobManagementComponent in Angular displaying pending jobs with approve/reject actions
  - Implement job approval logic that updates status to APPROVED and sets approval timestamp
  - Implement job rejection logic that updates status to REJECTED and stores rejection reason
  - Add ability for admin to remove active jobs (status change to REMOVED)
  - Display all jobs with status filters (pending, approved, rejected, removed)
  - _Requirements: 10.1, 10.2, 10.3, 10.4, 10.5_

- [x] 12. Create admin talent pool oversight





  - Implement admin endpoints for viewing all resumes and profiles (`GET /api/admin/resumes`, `GET /api/admin/profiles`)
  - Add flagging functionality for inappropriate content with flag reason storage
  - Create TalentPoolComponent in Angular displaying all Job Seeker profiles with resume links
  - Add flag/unflag actions with reason input dialog
  - Implement content removal functionality for flagged profiles
  - _Requirements: 11.1, 11.2, 11.3, 11.4, 11.5_

- [x] 13. Build admin system configuration





  - Create Category, Industry, and Skill entities for taxonomy management
  - Implement admin endpoints for taxonomy CRUD operations (`GET/POST/PUT /api/admin/categories`, `/api/admin/industries`, `/api/admin/skills`)
  - Build SystemConfigComponent in Angular with tabs for categories, industries, and skills
  - Implement add, edit, and delete functionality for each taxonomy type
  - Update job posting and profile forms to use taxonomy dropdowns
  - _Requirements: 12.1, 12.2, 12.3, 12.4, 12.5_

- [x] 14. Implement admin analytics and reporting









  - Create analytics endpoint (`GET /api/admin/analytics`) returning user counts, job counts, and activity metrics
  - Implement queries for recruiter activity (jobs posted, candidates contacted)
  - Implement queries for job seeker activity (applications submitted, profile updates)
  - Implement chat activity statistics (total chats, messages sent)
  - Build AnalyticsComponent in Angular displaying metrics with charts and date range filters
  - Add AdminDashboardComponent showing overview of key metrics
  - _Requirements: 13.1, 13.2, 13.3, 13.4, 13.5_

- [x] 15. Create admin chat moderation





  - Implement admin endpoint to view all chats (`GET /api/chats/all`)
  - Add chat flagging endpoint (`PUT /api/chats/{chatId}/flag`) with reason parameter
  - Implement user chat disable functionality that prevents sending messages
  - Build ChatModerationComponent in Angular displaying all chats with participants and message counts
  - Add flag/unflag actions and view chat history functionality
  - Maintain audit log of moderation actions in database
  - _Requirements: 14.1, 14.2, 14.3, 14.4, 14.5_

- [x] 16. Implement global error handling





  - Create custom exception classes (HirableException, UserNotFoundException, UnauthorizedException, InvalidFileFormatException, JobNotFoundException, ChatNotFoundException)
  - Implement GlobalExceptionHandler with @RestControllerAdvice to handle all exceptions
  - Return standardized error response format with timestamp, status, error, message, and path
  - Build error interceptor in Angular to catch HTTP errors
  - Display user-friendly error messages using Angular Material snackbar
  - Implement redirect to login page on 401 Unauthorized errors
  - _Requirements: 18.4_


- [x] 17. Build navigation and shared UI components




  - Create NavigationComponent with role-based menu items (different menus for Admin, Recruiter, Job Seeker)
  - Implement logout functionality that clears JWT token and redirects to login
  - Create shared UI components for loading spinners, confirmation dialogs, and error displays
  - Implement route guards to protect role-specific routes
  - Add responsive navigation with hamburger menu for mobile devices
  - _Requirements: 1.3, 17.1, 17.2, 17.3, 17.4, 17.5_

- [x] 18. Create sample data for demo





  - Write Flyway migration script `V2__sample_data.sql` with sample data inserts
  - Create 1 admin user with credentials (admin@hirable.com / admin123)
  - Create 5 recruiter users with company profiles and credentials
  - Create 10 job seeker users with complete profiles and credentials (including jobseeker@email.com / jobseeker123)
  - Create 15 job postings across various industries with mix of pending and approved status
  - Create 10 sample resume files in local filesystem with realistic content
  - Create 20 application records linking job seekers to jobs
  - Create 5 chat conversations with 10-15 messages each
  - Create 30 notifications for various events
  - Create sample taxonomy data (10 categories, 15 industries, 50 skills)
  - _Requirements: 16.1, 16.2, 16.3, 16.4, 16.5, 1.4_

- [x] 19. Implement file storage and retrieval




  - Configure local file storage directory path in application.properties
  - Create file storage utility service to save files with UUID-based names
  - Implement file download endpoint (`GET /api/jobseekers/{id}/resume/download`) with content-type headers
  - Add file validation for extensions (.pdf, .doc, .docx) and size limit (5MB)
  - Implement filename sanitization to prevent path traversal attacks
  - Create file cleanup service to remove orphaned resume files
  - _Requirements: 2.5, 18.2_

- [x] 20. Add database migrations and indexing





  - Create Flyway migration script `V1__initial_schema.sql` with all table definitions
  - Add foreign key constraints for entity relationships
  - Create indexes on Job.status and Job.postedAt for job search performance
  - Create indexes on JobSeeker.skills and JobSeeker.resumeUploadedAt for candidate search
  - Create indexes on Message.chatId and Message.sentAt for chat history retrieval
  - Create indexes on Notification.userId and Notification.read for notification queries
  - Create index on Shortlist.recruiterId for shortlist retrieval
  - _Requirements: 3.2, 5.5_

- [x] 21. Write integration tests for critical flows





  - Write integration tests for authentication flow with valid and invalid credentials
  - Write integration tests for job posting creation and approval workflow
  - Write integration tests for candidate search with relevance ranking
  - Write integration tests for chat message sending and retrieval
  - Write integration tests for file upload with various formats and sizes
  - _Requirements: 1.1, 1.2, 4.3, 5.2, 7.3, 2.3_

- [x] 22. Create end-to-end tests for user journeys





  - Write E2E test for Job Seeker flow: register, upload resume, search jobs, apply, receive chat message
  - Write E2E test for Recruiter flow: register, post job, search candidates, initiate chat, shortlist candidate
  - Write E2E test for Admin flow: login, approve job, view analytics, moderate chat
  - Test responsive design on mobile, tablet, and desktop viewports
  - _Requirements: 17.1, 17.2, 17.3, 17.4, 17.5_
-

- [x] 23. Configure application for deployment




  - Create application.properties with database connection settings
  - Configure JWT secret key and expiration time
  - Set file upload directory path and max file size
  - Configure WebSocket allowed origins for CORS
  - Create README with build and run instructions for backend and frontend
  - Add environment-specific configuration files (dev, prod)
  - _Requirements: 1.5, 18.5_

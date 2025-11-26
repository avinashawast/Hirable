# Requirements Document

## Introduction

Hirable is a web-based job matching platform that connects Job Seekers with Recruiters under Admin oversight. The system enables resume management, job postings, candidate search, and real-time chat communication. Built with Angular frontend and Spring Boot backend, it provides role-based access for three user types: Admin, Job Seeker, and Recruiter.

## Glossary

- **Hirable System**: The complete web-based job matching platform
- **Job Seeker**: A registered user seeking employment opportunities
- **Recruiter**: A registered user representing a company posting jobs and searching candidates
- **Admin**: A system administrator managing users, jobs, and platform operations
- **Resume**: A PDF or DOC format document containing candidate qualifications
- **Relevance Score**: A ranking metric based on resume update timestamp and keyword matching
- **Talent Pool**: The collection of all Job Seeker profiles and resumes in the system
- **Chat Service**: Real-time WebSocket-based messaging between Recruiters and Job Seekers
- **Job Posting**: A recruiter-created listing containing job details, requirements, and location

## Requirements

### Requirement 1: User Authentication

**User Story:** As a user, I want to securely log into the system with my role-specific credentials, so that I can access features appropriate to my role.

#### Acceptance Criteria

1. WHEN a user submits valid credentials, THE Hirable System SHALL authenticate the user and grant access to role-specific features
2. WHEN a user submits invalid credentials, THE Hirable System SHALL deny access and display an error message
3. THE Hirable System SHALL support three distinct user roles: Admin, Job Seeker, and Recruiter
4. THE Hirable System SHALL use hardcoded login credentials for demo purposes
5. WHEN a user successfully authenticates, THE Hirable System SHALL issue a JWT token for session management

### Requirement 2: Job Seeker Profile Management

**User Story:** As a Job Seeker, I want to create and maintain my professional profile, so that Recruiters can discover my qualifications.

#### Acceptance Criteria

1. WHEN a Job Seeker registers, THE Hirable System SHALL create a new profile with personal information, skills, experience, and education
2. THE Hirable System SHALL allow Job Seekers to update their profile information at any time
3. WHEN a Job Seeker uploads a resume, THE Hirable System SHALL accept PDF or DOC format files
4. WHEN a Job Seeker uploads a new resume, THE Hirable System SHALL replace the previous resume and update the relevance score timestamp
5. THE Hirable System SHALL store uploaded resumes in local file storage for demo purposes

### Requirement 3: Job Seeker Job Search and Application

**User Story:** As a Job Seeker, I want to search and apply for jobs matching my skills, so that I can find employment opportunities.

#### Acceptance Criteria

1. THE Hirable System SHALL allow Job Seekers to search jobs by keyword, location, industry, and experience level
2. WHEN a Job Seeker performs a search, THE Hirable System SHALL return results within 2 seconds
3. THE Hirable System SHALL allow Job Seekers to apply directly to job postings
4. WHEN a Job Seeker applies to a job, THE Hirable System SHALL notify the Recruiter who posted the job
5. THE Hirable System SHALL display job details including title, description, required skills, and location

### Requirement 4: Recruiter Profile and Job Posting

**User Story:** As a Recruiter, I want to create my company profile and post job openings, so that I can attract qualified candidates.

#### Acceptance Criteria

1. WHEN a Recruiter registers, THE Hirable System SHALL create a profile containing recruiter and company information
2. THE Hirable System SHALL allow Recruiters to create job postings with title, description, skills, location, and experience requirements
3. WHEN a Recruiter submits a job posting, THE Hirable System SHALL store the posting for Admin approval
4. THE Hirable System SHALL allow Recruiters to update their company profile information
5. THE Hirable System SHALL display all active job postings created by a Recruiter in their dashboard

### Requirement 5: Recruiter Candidate Search

**User Story:** As a Recruiter, I want to search the talent pool for candidates matching my requirements, so that I can proactively find qualified applicants.

#### Acceptance Criteria

1. THE Hirable System SHALL allow Recruiters to search candidates by skills, experience level, and location
2. WHEN a Recruiter performs a candidate search, THE Hirable System SHALL rank results by relevance score
3. THE Hirable System SHALL calculate relevance score based on resume update timestamp and keyword match strength
4. WHEN multiple candidates match search criteria, THE Hirable System SHALL display candidates with more recent resume updates higher in results
5. THE Hirable System SHALL return search results within 2 seconds

### Requirement 6: Recruiter Candidate Management

**User Story:** As a Recruiter, I want to shortlist and manage candidates, so that I can organize my hiring pipeline.

#### Acceptance Criteria

1. THE Hirable System SHALL allow Recruiters to add candidates to a shortlist
2. THE Hirable System SHALL allow Recruiters to remove candidates from their shortlist
3. THE Hirable System SHALL display all shortlisted candidates in the Recruiter dashboard
4. THE Hirable System SHALL persist shortlist data across user sessions
5. THE Hirable System SHALL allow Recruiters to view full candidate profiles from the shortlist

### Requirement 7: Real-Time Chat Communication

**User Story:** As a Recruiter or Job Seeker, I want to communicate through real-time chat, so that I can discuss job opportunities efficiently.

#### Acceptance Criteria

1. THE Hirable System SHALL allow Recruiters to initiate chat conversations with Job Seekers
2. THE Hirable System SHALL allow Job Seekers to respond to chat messages from Recruiters
3. WHEN a user sends a chat message, THE Hirable System SHALL deliver the message to the recipient in real-time using WebSocket protocol
4. THE Hirable System SHALL persist chat message history for future reference
5. WHEN a new chat message arrives, THE Hirable System SHALL notify the recipient through in-app notification

### Requirement 8: Notification System

**User Story:** As a user, I want to receive notifications about important events, so that I stay informed about platform activity.

#### Acceptance Criteria

1. WHEN a Recruiter shows interest in a Job Seeker, THE Hirable System SHALL send an in-app notification to the Job Seeker
2. WHEN a Job Seeker applies to a job, THE Hirable System SHALL send an in-app notification to the Recruiter
3. WHEN a new chat message arrives, THE Hirable System SHALL display an in-app notification to the recipient
4. THE Hirable System SHALL display unread notification count in the user interface
5. WHEN a user views a notification, THE Hirable System SHALL mark it as read

### Requirement 9: Admin User Management

**User Story:** As an Admin, I want to manage user accounts, so that I can maintain platform integrity and user quality.

#### Acceptance Criteria

1. THE Hirable System SHALL allow Admins to create new Job Seeker and Recruiter accounts
2. THE Hirable System SHALL allow Admins to update user account information
3. THE Hirable System SHALL allow Admins to deactivate user accounts
4. WHEN an Admin deactivates an account, THE Hirable System SHALL prevent that user from logging in
5. THE Hirable System SHALL display a list of all registered users with their roles and status

### Requirement 10: Admin Job Management

**User Story:** As an Admin, I want to review and manage job postings, so that I can ensure quality and compliance.

#### Acceptance Criteria

1. THE Hirable System SHALL display all pending job postings to Admins for review
2. THE Hirable System SHALL allow Admins to approve job postings
3. WHEN an Admin approves a job posting, THE Hirable System SHALL make the job visible to Job Seekers
4. THE Hirable System SHALL allow Admins to reject job postings with a reason
5. THE Hirable System SHALL allow Admins to remove active job postings

### Requirement 11: Admin Talent Pool Oversight

**User Story:** As an Admin, I want to monitor resumes and profiles, so that I can maintain content quality and flag inappropriate material.

#### Acceptance Criteria

1. THE Hirable System SHALL display all uploaded resumes to Admins
2. THE Hirable System SHALL allow Admins to view Job Seeker profiles
3. THE Hirable System SHALL allow Admins to flag resumes or profiles containing inappropriate content
4. WHEN an Admin flags content, THE Hirable System SHALL mark the associated profile for review
5. THE Hirable System SHALL allow Admins to remove flagged content

### Requirement 12: Admin System Configuration

**User Story:** As an Admin, I want to manage system taxonomies, so that I can maintain consistent categorization across the platform.

#### Acceptance Criteria

1. THE Hirable System SHALL allow Admins to create and update job categories
2. THE Hirable System SHALL allow Admins to create and update industry classifications
3. THE Hirable System SHALL allow Admins to create and update skills taxonomy
4. THE Hirable System SHALL apply taxonomy updates to search and filtering functionality
5. THE Hirable System SHALL display current taxonomy values in dropdown menus throughout the application

### Requirement 13: Admin Analytics and Reporting

**User Story:** As an Admin, I want to view platform analytics, so that I can understand usage patterns and system health.

#### Acceptance Criteria

1. THE Hirable System SHALL display total counts of Job Seekers, Recruiters, and job postings
2. THE Hirable System SHALL display Recruiter activity metrics including job postings created and candidates contacted
3. THE Hirable System SHALL display Job Seeker activity metrics including applications submitted and profile updates
4. THE Hirable System SHALL display chat activity statistics
5. THE Hirable System SHALL allow Admins to view analytics for custom date ranges

### Requirement 14: Admin Chat Moderation

**User Story:** As an Admin, I want to monitor chat conversations, so that I can ensure compliance and professional conduct.

#### Acceptance Criteria

1. THE Hirable System SHALL allow Admins to view all chat conversations between Recruiters and Job Seekers
2. THE Hirable System SHALL display chat participants and message timestamps
3. THE Hirable System SHALL allow Admins to flag inappropriate chat messages
4. THE Hirable System SHALL allow Admins to disable chat functionality for specific users
5. THE Hirable System SHALL maintain audit logs of Admin moderation actions

### Requirement 15: Resume Parsing

**User Story:** As a Job Seeker, I want my resume to be automatically parsed, so that my profile is populated with relevant information.

#### Acceptance Criteria

1. WHEN a Job Seeker uploads a resume, THE Hirable System SHALL extract skills from the document
2. WHEN a Job Seeker uploads a resume, THE Hirable System SHALL extract work experience information
3. WHEN a Job Seeker uploads a resume, THE Hirable System SHALL extract education details
4. WHEN resume parsing completes, THE Hirable System SHALL update the Job Seeker profile with extracted information
5. THE Hirable System SHALL allow Job Seekers to review and modify parsed information
6. Security checks for malware uploads should be taken care by Hirable system

### Requirement 16: Sample Data for Demo

**User Story:** As a developer, I want the system to include sample data, so that I can demonstrate platform functionality without manual data entry.

#### Acceptance Criteria

1. THE Hirable System SHALL include at least 10 sample Job Seeker profiles with resumes
2. THE Hirable System SHALL include at least 5 sample Recruiter profiles with company information
3. THE Hirable System SHALL include at least 15 sample job postings across various industries
4. THE Hirable System SHALL include sample chat conversations between Recruiters and Job Seekers
5. THE Hirable System SHALL load sample data during initial system setup

### Requirement 17: Responsive User Interface

**User Story:** As a user, I want the platform to work seamlessly on different devices, so that I can access it from desktop or mobile.

#### Acceptance Criteria

1. THE Hirable System SHALL render correctly on desktop browsers with minimum resolution 1024x768 pixels
2. THE Hirable System SHALL render correctly on tablet devices
3. THE Hirable System SHALL render correctly on mobile devices with minimum width 320 pixels
4. THE Hirable System SHALL adapt navigation and layout based on screen size
5. THE Hirable System SHALL maintain functionality across all supported device types

### Requirement 18: Security and Data Protection

**User Story:** As a user, I want my data to be secure, so that my personal information remains protected.

#### Acceptance Criteria

1. THE Hirable System SHALL encrypt all user passwords using industry-standard hashing algorithms
2. THE Hirable System SHALL validate file uploads to prevent malicious content
3. THE Hirable System SHALL enforce role-based access control for all features
4. THE Hirable System SHALL sanitize user inputs to prevent injection attacks
5. THE Hirable System SHALL use HTTPS for all client-server communication in production environments

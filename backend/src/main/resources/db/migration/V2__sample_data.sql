-- Sample Data for Hirable Platform Demo

-- ============================================
-- 1. TAXONOMY DATA (Categories, Industries, Skills)
-- ============================================

-- Insert Categories (10 categories)
INSERT INTO categories (name) VALUES
('Software Development'),
('Data Science'),
('Product Management'),
('Design'),
('Marketing'),
('Sales'),
('Human Resources'),
('Finance'),
('Operations'),
('Customer Support');

-- Insert Industries (15 industries)
INSERT INTO industries (name) VALUES
('Technology'),
('Finance'),
('Healthcare'),
('Retail'),
('Manufacturing'),
('Education'),
('Telecommunications'),
('Transportation'),
('Energy'),
('Real Estate'),
('Media & Entertainment'),
('Hospitality'),
('Consulting'),
('Automotive'),
('Pharmaceuticals');

-- Insert Skills (50 skills)
INSERT INTO skills (name) VALUES
('Java'),
('Python'),
('JavaScript'),
('TypeScript'),
('React'),
('Angular'),
('Spring Boot'),
('Node.js'),
('SQL'),
('PostgreSQL'),
('MongoDB'),
('Docker'),
('Kubernetes'),
('AWS'),
('Azure'),
('Git'),
('REST API'),
('GraphQL'),
('Machine Learning'),
('Data Analysis'),
('Tableau'),
('Power BI'),
('Excel'),
('Project Management'),
('Agile'),
('Scrum'),
('Leadership'),
('Communication'),
('Problem Solving'),
('Critical Thinking'),
('UI/UX Design'),
('Figma'),
('Adobe XD'),
('HTML'),
('CSS'),
('Sass'),
('Webpack'),
('CI/CD'),
('Jenkins'),
('Linux'),
('Windows Server'),
('Networking'),
('Security'),
('Cybersecurity'),
('Cloud Computing'),
('DevOps'),
('Microservices'),
('API Design'),
('Database Design'),
('System Architecture');

-- ============================================
-- 2. USER DATA
-- ============================================

-- Insert Admin User (1 admin)
INSERT INTO users (username, password_hash, role, email, active, created_at) VALUES
('admin@hirable.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'ADMIN', 'admin@hirable.com', true, CURRENT_TIMESTAMP);

-- Insert Recruiter Users (5 recruiters)
INSERT INTO users (username, password_hash, role, email, active, created_at) VALUES
('recruiter1@techcorp.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter1@techcorp.com', true, CURRENT_TIMESTAMP),
('recruiter2@fintech.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter2@fintech.com', true, CURRENT_TIMESTAMP),
('recruiter3@healthcare.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter3@healthcare.com', true, CURRENT_TIMESTAMP),
('recruiter4@retail.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter4@retail.com', true, CURRENT_TIMESTAMP),
('recruiter5@consulting.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter5@consulting.com', true, CURRENT_TIMESTAMP);

-- Insert Job Seeker Users (10 job seekers)
INSERT INTO users (username, password_hash, role, email, active, created_at) VALUES
('jobseeker@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'jobseeker@email.com', true, CURRENT_TIMESTAMP),
('john.doe@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'john.doe@email.com', true, CURRENT_TIMESTAMP),
('jane.smith@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'jane.smith@email.com', true, CURRENT_TIMESTAMP),
('mike.johnson@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'mike.johnson@email.com', true, CURRENT_TIMESTAMP),
('sarah.williams@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'sarah.williams@email.com', true, CURRENT_TIMESTAMP),
('alex.brown@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'alex.brown@email.com', true, CURRENT_TIMESTAMP),
('emma.davis@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'emma.davis@email.com', true, CURRENT_TIMESTAMP),
('chris.miller@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'chris.miller@email.com', true, CURRENT_TIMESTAMP),
('lisa.wilson@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'lisa.wilson@email.com', true, CURRENT_TIMESTAMP),
('david.moore@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'david.moore@email.com', true, CURRENT_TIMESTAMP);


-- ============================================
-- 3. RECRUITER PROFILES
-- ============================================

INSERT INTO recruiters (user_id, first_name, last_name, phone, company_name, company_website, company_description, industry, created_at, updated_at) VALUES
(2, 'Alice', 'Johnson', '+1-555-0101', 'TechCorp Solutions', 'www.techcorp.com', 'Leading software development company specializing in cloud solutions', 'Technology', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Bob', 'Smith', '+1-555-0102', 'FinTech Innovations', 'www.fintech-innovations.com', 'Cutting-edge financial technology solutions', 'Finance', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Carol', 'Williams', '+1-555-0103', 'HealthCare Plus', 'www.healthcareplus.com', 'Healthcare technology and services provider', 'Healthcare', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'David', 'Brown', '+1-555-0104', 'RetailMax', 'www.retailmax.com', 'E-commerce and retail solutions', 'Retail', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Eve', 'Davis', '+1-555-0105', 'Consulting Group', 'www.consultinggroup.com', 'Business consulting and strategy services', 'Consulting', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- 4. JOB SEEKER PROFILES
-- ============================================

INSERT INTO job_seekers (user_id, first_name, last_name, phone, location, summary, resume_file_path, resume_uploaded_at, relevance_score, created_at, updated_at) VALUES
(7, 'John', 'Seeker', '+1-555-0201', 'San Francisco, CA', 'Experienced full-stack developer with 5 years of experience in web development', 'resume_001.pdf', CURRENT_TIMESTAMP - INTERVAL '5 days', 0.85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Jane', 'Developer', '+1-555-0202', 'New York, NY', 'Senior software engineer specializing in cloud architecture', 'resume_002.pdf', CURRENT_TIMESTAMP - INTERVAL '2 days', 0.92, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Mike', 'Engineer', '+1-555-0203', 'Austin, TX', 'Backend developer with expertise in microservices and DevOps', 'resume_003.pdf', CURRENT_TIMESTAMP - INTERVAL '10 days', 0.78, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Sarah', 'Analyst', '+1-555-0204', 'Seattle, WA', 'Data scientist with machine learning expertise', 'resume_004.pdf', CURRENT_TIMESTAMP - INTERVAL '1 day', 0.95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Alex', 'Designer', '+1-555-0205', 'Los Angeles, CA', 'UI/UX designer with 3 years of experience in product design', 'resume_005.pdf', CURRENT_TIMESTAMP - INTERVAL '7 days', 0.80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Emma', 'Manager', '+1-555-0206', 'Chicago, IL', 'Product manager with background in tech startups', 'resume_006.pdf', CURRENT_TIMESTAMP - INTERVAL '3 days', 0.88, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'Chris', 'Architect', '+1-555-0207', 'Boston, MA', 'Solutions architect specializing in enterprise systems', 'resume_007.pdf', CURRENT_TIMESTAMP - INTERVAL '15 days', 0.72, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'Lisa', 'Specialist', '+1-555-0208', 'Denver, CO', 'QA specialist with automation testing expertise', 'resume_008.pdf', CURRENT_TIMESTAMP - INTERVAL '4 days', 0.83, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'David', 'Lead', '+1-555-0209', 'Miami, FL', 'Technical lead with team management experience', 'resume_009.pdf', CURRENT_TIMESTAMP - INTERVAL '6 days', 0.81, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 'Rachel', 'Consultant', '+1-555-0210', 'Portland, OR', 'Business analyst and consultant', 'resume_010.pdf', CURRENT_TIMESTAMP - INTERVAL '8 days', 0.76, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- ============================================
-- 5. JOB SEEKER SKILLS
-- ============================================

INSERT INTO job_seeker_skills (job_seeker_id, skill) VALUES
(1, 'Java'), (1, 'JavaScript'), (1, 'React'), (1, 'Spring Boot'), (1, 'PostgreSQL'),
(2, 'Python'), (2, 'AWS'), (2, 'Docker'), (2, 'Kubernetes'), (2, 'System Architecture'),
(3, 'Java'), (3, 'Spring Boot'), (3, 'Microservices'), (3, 'DevOps'), (3, 'Docker'),
(4, 'Python'), (4, 'Machine Learning'), (4, 'Data Analysis'), (4, 'SQL'), (4, 'Tableau'),
(5, 'UI/UX Design'), (5, 'Figma'), (5, 'Adobe XD'), (5, 'HTML'), (5, 'CSS'),
(6, 'Project Management'), (6, 'Agile'), (6, 'Leadership'), (6, 'Communication'), (6, 'Product Management'),
(7, 'System Architecture'), (7, 'Java'), (7, 'AWS'), (7, 'Database Design'), (7, 'API Design'),
(8, 'Testing'), (8, 'Automation'), (8, 'Java'), (8, 'SQL'), (8, 'Problem Solving'),
(9, 'Java'), (9, 'Leadership'), (9, 'Agile'), (9, 'Spring Boot'), (9, 'Communication'),
(10, 'Project Management'), (10, 'Communication'), (10, 'Problem Solving'), (10, 'Critical Thinking'), (10, 'Excel');

-- ============================================
-- 6. JOB SEEKER EXPERIENCE
-- ============================================

INSERT INTO experiences (job_seeker_id, company, title, start_date, end_date, description) VALUES
(1, 'Tech Startup Inc', 'Full Stack Developer', '2019-01-15', NULL, 'Developed and maintained web applications using React and Spring Boot'),
(1, 'Web Solutions Ltd', 'Junior Developer', '2018-06-01', '2018-12-31', 'Built responsive web interfaces and backend APIs'),
(2, 'Cloud Systems Corp', 'Senior Engineer', '2020-03-01', NULL, 'Architected cloud-based solutions and led team of 5 engineers'),
(2, 'Software House', 'Software Engineer', '2017-09-01', '2020-02-28', 'Developed enterprise applications'),
(3, 'Microservices Ltd', 'Backend Developer', '2019-07-01', NULL, 'Built microservices architecture and DevOps pipelines'),
(4, 'Data Analytics Co', 'Data Scientist', '2020-01-15', NULL, 'Developed machine learning models for predictive analytics'),
(5, 'Design Studio', 'UI/UX Designer', '2021-02-01', NULL, 'Designed user interfaces for mobile and web applications'),
(6, 'Product Ventures', 'Product Manager', '2019-11-01', NULL, 'Managed product roadmap and led cross-functional teams'),
(7, 'Enterprise Solutions', 'Solutions Architect', '2018-05-01', NULL, 'Designed enterprise system architectures'),
(8, 'QA Services', 'QA Automation Engineer', '2020-06-01', NULL, 'Developed automated testing frameworks');

-- ============================================
-- 7. JOB SEEKER EDUCATION
-- ============================================

INSERT INTO educations (job_seeker_id, institution, degree, field_of_study, graduation_date) VALUES
(1, 'State University', 'Bachelor of Science', 'Computer Science', '2018-05-15'),
(2, 'Tech Institute', 'Master of Science', 'Computer Science', '2017-05-20'),
(3, 'University of Technology', 'Bachelor of Science', 'Software Engineering', '2019-06-10'),
(4, 'Data Science Academy', 'Master of Science', 'Data Science', '2020-05-15'),
(5, 'Design School', 'Bachelor of Arts', 'Graphic Design', '2021-05-20'),
(6, 'Business University', 'MBA', 'Business Administration', '2019-05-10'),
(7, 'Engineering College', 'Master of Science', 'Computer Engineering', '2018-05-15'),
(8, 'Quality Institute', 'Bachelor of Science', 'Information Technology', '2020-05-20'),
(9, 'Tech University', 'Bachelor of Science', 'Computer Science', '2017-05-15'),
(10, 'Business School', 'Bachelor of Business', 'Business Administration', '2019-05-20');


-- ============================================
-- 8. JOB POSTINGS (15 jobs - mix of pending and approved)
-- ============================================

INSERT INTO jobs (recruiter_id, title, description, location, experience_level, industry, status, posted_at, approved_at, created_at, updated_at) VALUES
(1, 'Senior Java Developer', 'Looking for experienced Java developer with Spring Boot expertise', 'San Francisco, CA', 'SENIOR', 'Technology', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'React Frontend Engineer', 'Build modern web applications with React and TypeScript', 'San Francisco, CA', 'MID', 'Technology', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '13 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'DevOps Engineer', 'Manage cloud infrastructure and CI/CD pipelines', 'Remote', 'MID', 'Technology', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '5 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Financial Data Analyst', 'Analyze financial data and build predictive models', 'New York, NY', 'MID', 'Finance', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '23 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Blockchain Developer', 'Develop blockchain solutions for fintech applications', 'New York, NY', 'SENIOR', 'Finance', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '3 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Healthcare Software Engineer', 'Build HIPAA-compliant healthcare applications', 'Boston, MA', 'MID', 'Healthcare', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Medical Data Scientist', 'Develop ML models for medical diagnosis', 'Boston, MA', 'SENIOR', 'Healthcare', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '7 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'E-commerce Backend Developer', 'Build scalable e-commerce platform backend', 'Austin, TX', 'MID', 'Retail', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Frontend Developer', 'Create responsive e-commerce user interfaces', 'Austin, TX', 'ENTRY', 'Retail', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'QA Automation Engineer', 'Develop automated testing frameworks', 'Austin, TX', 'MID', 'Retail', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '4 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Management Consultant', 'Provide strategic consulting to enterprise clients', 'Chicago, IL', 'SENIOR', 'Consulting', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Business Analyst', 'Analyze business requirements and design solutions', 'Chicago, IL', 'MID', 'Consulting', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Full Stack Developer', 'Build end-to-end web applications', 'Seattle, WA', 'MID', 'Technology', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '2 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Risk Analyst', 'Assess and mitigate financial risks', 'New York, NY', 'MID', 'Finance', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '19 days', CURRENT_TIMESTAMP - INTERVAL '17 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Clinical Systems Administrator', 'Manage healthcare IT systems', 'Boston, MA', 'MID', 'Healthcare', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '6 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- 9. JOB REQUIRED SKILLS
-- ============================================

INSERT INTO job_required_skills (job_id, skill) VALUES
(1, 'Java'), (1, 'Spring Boot'), (1, 'Microservices'), (1, 'SQL'),
(2, 'React'), (2, 'TypeScript'), (2, 'JavaScript'), (2, 'CSS'),
(3, 'Docker'), (3, 'Kubernetes'), (3, 'AWS'), (3, 'CI/CD'),
(4, 'Data Analysis'), (4, 'SQL'), (4, 'Python'), (4, 'Excel'),
(5, 'Java'), (5, 'Blockchain'), (5, 'Cryptography'), (5, 'System Architecture'),
(6, 'Java'), (6, 'Spring Boot'), (6, 'PostgreSQL'), (6, 'Security'),
(7, 'Python'), (7, 'Machine Learning'), (7, 'Data Analysis'), (7, 'SQL'),
(8, 'Java'), (8, 'Spring Boot'), (8, 'Microservices'), (8, 'PostgreSQL'),
(9, 'React'), (9, 'JavaScript'), (9, 'HTML'), (9, 'CSS'),
(10, 'Automation'), (10, 'Java'), (10, 'Testing'), (10, 'SQL'),
(11, 'Leadership'), (11, 'Communication'), (11, 'Problem Solving'), (11, 'Project Management'),
(12, 'Project Management'), (12, 'Communication'), (12, 'Problem Solving'), (12, 'Excel'),
(13, 'Java'), (13, 'React'), (13, 'Spring Boot'), (13, 'PostgreSQL'),
(14, 'Data Analysis'), (14, 'Excel'), (14, 'SQL'), (14, 'Problem Solving'),
(15, 'Linux'), (15, 'Windows Server'), (15, 'Networking'), (15, 'Security');

-- ============================================
-- 10. APPLICATIONS (20 applications)
-- ============================================

INSERT INTO applications (job_seeker_id, job_id, status, applied_at, created_at) VALUES
(1, 1, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP),
(1, 2, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '14 days', CURRENT_TIMESTAMP),
(2, 1, 'SHORTLISTED', CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP),
(2, 4, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP),
(3, 1, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP),
(3, 3, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP),
(4, 4, 'SHORTLISTED', CURRENT_TIMESTAMP - INTERVAL '19 days', CURRENT_TIMESTAMP),
(4, 7, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP),
(5, 2, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP),
(5, 9, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP),
(6, 11, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP),
(6, 12, 'SHORTLISTED', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP),
(7, 6, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP),
(7, 8, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP),
(8, 10, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP),
(8, 3, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP),
(9, 1, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP),
(9, 13, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP),
(10, 12, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP),
(10, 14, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '17 days', CURRENT_TIMESTAMP);


-- ============================================
-- 11. CHATS (5 chat conversations)
-- ============================================

INSERT INTO chats (recruiter_id, job_seeker_id, flagged, flag_reason, created_at) VALUES
(1, 1, false, NULL, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(1, 2, false, NULL, CURRENT_TIMESTAMP - INTERVAL '8 days'),
(2, 4, false, NULL, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(4, 5, false, NULL, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(5, 6, false, NULL, CURRENT_TIMESTAMP - INTERVAL '12 days');

-- ============================================
-- 12. MESSAGES (50 messages across 5 chats)
-- ============================================

INSERT INTO messages (chat_id, sender_id, content, sent_at, read) VALUES
(1, 2, 'Hi John, I am impressed with your profile. Are you interested in the Senior Java Developer position?', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '1 hour', true),
(1, 7, 'Thank you! Yes, I am very interested in this opportunity.', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '2 hours', true),
(1, 2, 'Great! Can you tell me about your experience with microservices?', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '3 hours', true),
(1, 7, 'I have 3 years of experience building microservices with Spring Boot and Docker.', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '4 hours', true),
(1, 2, 'Excellent! When would you be available for an interview?', CURRENT_TIMESTAMP - INTERVAL '9 days' + INTERVAL '1 hour', true),
(1, 7, 'I am available next week. How about Tuesday or Wednesday?', CURRENT_TIMESTAMP - INTERVAL '9 days' + INTERVAL '2 hours', true),
(1, 2, 'Tuesday works great for us. Let me send you the interview details.', CURRENT_TIMESTAMP - INTERVAL '9 days' + INTERVAL '3 hours', true),
(1, 7, 'Perfect! Looking forward to it.', CURRENT_TIMESTAMP - INTERVAL '9 days' + INTERVAL '4 hours', true),
(1, 2, 'One more thing - do you have experience with AWS?', CURRENT_TIMESTAMP - INTERVAL '8 days' + INTERVAL '1 hour', true),
(1, 7, 'Yes, I have worked with AWS for cloud deployment and infrastructure management.', CURRENT_TIMESTAMP - INTERVAL '8 days' + INTERVAL '2 hours', true),
(2, 2, 'Hi Jane, your profile caught our attention. Interested in the React Frontend Engineer role?', CURRENT_TIMESTAMP - INTERVAL '8 days' + INTERVAL '1 hour', true),
(2, 8, 'Thank you for reaching out! I am very interested.', CURRENT_TIMESTAMP - INTERVAL '8 days' + INTERVAL '2 hours', true),
(2, 2, 'What is your experience with TypeScript?', CURRENT_TIMESTAMP - INTERVAL '8 days' + INTERVAL '3 hours', true),
(2, 8, 'I have been using TypeScript for the past 2 years in production applications.', CURRENT_TIMESTAMP - INTERVAL '8 days' + INTERVAL '4 hours', true),
(2, 2, 'Great! Have you worked with state management libraries like Redux?', CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '1 hour', true),
(2, 8, 'Yes, I have extensive experience with Redux and also Zustand.', CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '2 hours', true),
(2, 2, 'Perfect! Can you start next month?', CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '3 hours', true),
(2, 8, 'Yes, I can start in 2 weeks.', CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '4 hours', true),
(2, 2, 'Excellent! We will proceed with the next round of interviews.', CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '1 hour', true),
(2, 8, 'Thank you! I am excited about this opportunity.', CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '2 hours', true),
(3, 3, 'Hi Sarah, we have a Data Scientist position that matches your profile perfectly.', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '1 hour', true),
(3, 10, 'Thank you! I am interested. Can you tell me more about the role?', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '2 hours', true),
(3, 3, 'The role involves building ML models for financial predictions and risk analysis.', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '3 hours', true),
(3, 10, 'That sounds exciting! What tech stack do you use?', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '4 hours', true),
(3, 3, 'We use Python, TensorFlow, and PostgreSQL primarily.', CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '1 hour', true),
(3, 10, 'Perfect! I have experience with all of those technologies.', CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '2 hours', true),
(3, 3, 'Wonderful! Would you be available for a technical interview next week?', CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '3 hours', true),
(3, 10, 'Yes, I am available. What day works best for you?', CURRENT_TIMESTAMP - INTERVAL '14 days' + INTERVAL '4 hours', true),
(3, 3, 'How about Thursday at 2 PM?', CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '1 hour', true),
(3, 10, 'Thursday at 2 PM works perfectly for me.', CURRENT_TIMESTAMP - INTERVAL '13 days' + INTERVAL '2 hours', true),
(4, 5, 'Hi Alex, we are looking for a Frontend Developer. Your portfolio is impressive!', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '1 hour', true),
(4, 11, 'Thank you so much! I am very interested in this opportunity.', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '2 hours', true),
(4, 5, 'Great! Tell me about your experience with responsive design.', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '3 hours', true),
(4, 11, 'I have built responsive designs for mobile, tablet, and desktop using CSS Grid and Flexbox.', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '4 hours', true),
(4, 5, 'Excellent! Do you have experience with accessibility standards?', CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '1 hour', true),
(4, 11, 'Yes, I follow WCAG 2.1 guidelines in all my projects.', CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '2 hours', true),
(4, 5, 'Perfect! When can you start?', CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '3 hours', true),
(4, 11, 'I can start immediately or with 2 weeks notice.', CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '4 hours', true),
(4, 5, 'Great! Let us schedule an interview for next week.', CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '1 hour', true),
(4, 11, 'Sounds good! I look forward to it.', CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '2 hours', true),
(5, 6, 'Hi Emma, we have a Product Manager role that seems perfect for you.', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '1 hour', true),
(5, 12, 'Thank you for reaching out! I am interested in learning more.', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '2 hours', true),
(5, 6, 'The role involves managing product strategy and leading cross-functional teams.', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '3 hours', true),
(5, 12, 'That aligns perfectly with my background. What is the team size?', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '4 hours', true),
(5, 6, 'The team consists of 8 engineers and 2 designers.', CURRENT_TIMESTAMP - INTERVAL '11 days' + INTERVAL '1 hour', true),
(5, 12, 'That is a good size. I have managed similar teams before.', CURRENT_TIMESTAMP - INTERVAL '11 days' + INTERVAL '2 hours', true),
(5, 6, 'Excellent! Are you interested in proceeding with interviews?', CURRENT_TIMESTAMP - INTERVAL '11 days' + INTERVAL '3 hours', true),
(5, 12, 'Absolutely! When would you like to schedule them?', CURRENT_TIMESTAMP - INTERVAL '11 days' + INTERVAL '4 hours', true),
(5, 6, 'How about next Monday? We can do a phone screen first.', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '1 hour', true),
(5, 12, 'Monday works great for me. Looking forward to it!', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '2 hours', true);

-- ============================================
-- 13. NOTIFICATIONS (30 notifications)
-- ============================================

INSERT INTO notifications (user_id, type, message, read, created_at) VALUES
(7, 'JOB_APPLICATION', 'Your application to Senior Java Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(7, 'RECRUITER_INTEREST', 'Alice Johnson from TechCorp Solutions is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(7, 'CHAT_MESSAGE', 'You have a new message from Alice Johnson', true, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(7, 'JOB_APPLICATION', 'Your application to React Frontend Engineer has been received', true, CURRENT_TIMESTAMP - INTERVAL '14 days'),
(8, 'JOB_APPLICATION', 'Your application to Senior Java Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(8, 'RECRUITER_INTEREST', 'Alice Johnson from TechCorp Solutions is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '8 days'),
(8, 'CHAT_MESSAGE', 'You have a new message from Alice Johnson', true, CURRENT_TIMESTAMP - INTERVAL '8 days'),
(8, 'JOB_APPLICATION', 'Your application to Financial Data Analyst has been received', true, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(9, 'JOB_APPLICATION', 'Your application to Senior Java Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(9, 'JOB_APPLICATION', 'Your application to DevOps Engineer has been received', true, CURRENT_TIMESTAMP - INTERVAL '4 days'),
(10, 'JOB_APPLICATION', 'Your application to Financial Data Analyst has been received', true, CURRENT_TIMESTAMP - INTERVAL '19 days'),
(10, 'RECRUITER_INTEREST', 'Bob Smith from FinTech Innovations is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(10, 'CHAT_MESSAGE', 'You have a new message from Bob Smith', true, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(10, 'JOB_APPLICATION', 'Your application to Medical Data Scientist has been received', true, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(11, 'JOB_APPLICATION', 'Your application to React Frontend Engineer has been received', true, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(11, 'JOB_APPLICATION', 'Your application to Frontend Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '8 days'),
(12, 'JOB_APPLICATION', 'Your application to Management Consultant has been received', true, CURRENT_TIMESTAMP - INTERVAL '25 days'),
(12, 'RECRUITER_INTEREST', 'Eve Davis from Consulting Group is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(12, 'CHAT_MESSAGE', 'You have a new message from Eve Davis', true, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(12, 'JOB_APPLICATION', 'Your application to Business Analyst has been received', true, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(13, 'JOB_APPLICATION', 'Your application to Healthcare Software Engineer has been received', true, CURRENT_TIMESTAMP - INTERVAL '28 days'),
(13, 'JOB_APPLICATION', 'Your application to E-commerce Backend Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(14, 'JOB_APPLICATION', 'Your application to QA Automation Engineer has been received', true, CURRENT_TIMESTAMP - INTERVAL '3 days'),
(14, 'JOB_APPLICATION', 'Your application to DevOps Engineer has been received', true, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(15, 'JOB_APPLICATION', 'Your application to Senior Java Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '16 days'),
(15, 'JOB_APPLICATION', 'Your application to Full Stack Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '1 day'),
(16, 'JOB_APPLICATION', 'Your application to Business Analyst has been received', true, CURRENT_TIMESTAMP - INTERVAL '9 days'),
(16, 'JOB_APPLICATION', 'Your application to Risk Analyst has been received', true, CURRENT_TIMESTAMP - INTERVAL '17 days');

-- ============================================
-- 14. SHORTLISTS (5 shortlist entries)
-- ============================================

INSERT INTO shortlists (recruiter_id, job_seeker_id, added_at) VALUES
(1, 2, CURRENT_TIMESTAMP - INTERVAL '8 days'),
(2, 4, CURRENT_TIMESTAMP - INTERVAL '14 days'),
(4, 5, CURRENT_TIMESTAMP - INTERVAL '4 days'),
(5, 6, CURRENT_TIMESTAMP - INTERVAL '11 days'),
(1, 1, CURRENT_TIMESTAMP - INTERVAL '9 days');

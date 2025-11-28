-- ============================================
-- COMPREHENSIVE DEMO DATA FOR ALL ROLES
-- ============================================
-- This script populates all tables with realistic demo data
-- for Admin, Recruiter, and Job Seeker roles
-- Password for all users: password123 (bcrypt hash)

-- ============================================
-- SECTION 1: ADDITIONAL TAXONOMY DATA
-- ============================================

-- Add more categories if needed
INSERT INTO categories (name) VALUES
('Business Development'),
('Legal'),
('Compliance'),
('Risk Management'),
('Quality Assurance')
ON CONFLICT DO NOTHING;

-- Add more industries if needed
INSERT INTO industries (name) VALUES
('Government'),
('Non-Profit'),
('Logistics'),
('Agriculture'),
('Mining')
ON CONFLICT DO NOTHING;

-- Add more skills if needed
INSERT INTO skills (name) VALUES
('Rust'),
('Go'),
('Scala'),
('Kotlin'),
('Swift'),
('Objective-C'),
('PHP'),
('Ruby'),
('Perl'),
('Haskell'),
('Clojure'),
('Elixir'),
('Erlang'),
('Lua'),
('R'),
('MATLAB'),
('SAS'),
('Spark'),
('Hadoop'),
('Cassandra'),
('Redis'),
('Elasticsearch'),
('Solr'),
('RabbitMQ'),
('Kafka'),
('Nginx'),
('Apache'),
('IIS'),
('Tomcat'),
('JBoss')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 2: ADDITIONAL ADMIN USERS
-- ============================================

INSERT INTO users (username, password_hash, role, email, active, created_at) VALUES
('admin2@hirable.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'ADMIN', 'admin2@hirable.com', true, CURRENT_TIMESTAMP - INTERVAL '30 days'),
('admin3@hirable.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'ADMIN', 'admin3@hirable.com', true, CURRENT_TIMESTAMP - INTERVAL '60 days')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 3: ADDITIONAL RECRUITER USERS & PROFILES
-- ============================================

INSERT INTO users (username, password_hash, role, email, active, created_at) VALUES
('recruiter6@startup.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter6@startup.com', true, CURRENT_TIMESTAMP - INTERVAL '45 days'),
('recruiter7@enterprise.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter7@enterprise.com', true, CURRENT_TIMESTAMP - INTERVAL '50 days'),
('recruiter8@agency.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter8@agency.com', true, CURRENT_TIMESTAMP - INTERVAL '35 days'),
('recruiter9@logistics.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter9@logistics.com', true, CURRENT_TIMESTAMP - INTERVAL '40 days'),
('recruiter10@nonprofit.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'RECRUITER', 'recruiter10@nonprofit.com', true, CURRENT_TIMESTAMP - INTERVAL '55 days')
ON CONFLICT DO NOTHING;

INSERT INTO recruiters (user_id, first_name, last_name, phone, company_name, company_website, company_description, industry, created_at, updated_at) VALUES
(7, 'Frank', 'Miller', '+1-555-0106', 'StartUp Ventures', 'www.startupventures.com', 'Innovative startup focused on AI solutions', 'Technology', CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP),
(8, 'Grace', 'Taylor', '+1-555-0107', 'Enterprise Solutions Inc', 'www.enterprisesolutions.com', 'Large enterprise software provider', 'Technology', CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP),
(9, 'Henry', 'Anderson', '+1-555-0108', 'Recruitment Agency Pro', 'www.recruitmentagency.com', 'Full-service recruitment agency', 'Consulting', CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP),
(10, 'Iris', 'Thomas', '+1-555-0109', 'Logistics Global', 'www.logisticsglobal.com', 'International logistics and supply chain', 'Transportation', CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP),
(11, 'Jack', 'Jackson', '+1-555-0110', 'Community Impact', 'www.communityimpact.org', 'Non-profit organization focused on education', 'Non-Profit', CURRENT_TIMESTAMP - INTERVAL '55 days', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 4: ADDITIONAL JOB SEEKER USERS & PROFILES
-- ============================================

INSERT INTO users (username, password_hash, role, email, active, created_at) VALUES
('seeker11@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'seeker11@email.com', true, CURRENT_TIMESTAMP - INTERVAL '25 days'),
('seeker12@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'seeker12@email.com', true, CURRENT_TIMESTAMP - INTERVAL '30 days'),
('seeker13@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'seeker13@email.com', true, CURRENT_TIMESTAMP - INTERVAL '20 days'),
('seeker14@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'seeker14@email.com', true, CURRENT_TIMESTAMP - INTERVAL '35 days'),
('seeker15@email.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DjH.PKZbv5H8KnzzVgXXbVxzy990qm', 'JOB_SEEKER', 'seeker15@email.com', true, CURRENT_TIMESTAMP - INTERVAL '40 days')
ON CONFLICT DO NOTHING;

INSERT INTO job_seekers (user_id, first_name, last_name, phone, location, summary, resume_file_path, resume_uploaded_at, relevance_score, created_at, updated_at) VALUES
(17, 'Kevin', 'Martinez', '+1-555-0211', 'Phoenix, AZ', 'Full-stack developer with 6 years of experience', 'resume_011.pdf', CURRENT_TIMESTAMP - INTERVAL '25 days', 0.87, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(18, 'Laura', 'Garcia', '+1-555-0212', 'Philadelphia, PA', 'Senior backend engineer specializing in distributed systems', 'resume_012.pdf', CURRENT_TIMESTAMP - INTERVAL '30 days', 0.91, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, 'Mark', 'Rodriguez', '+1-555-0213', 'San Antonio, TX', 'DevOps engineer with cloud infrastructure expertise', 'resume_013.pdf', CURRENT_TIMESTAMP - INTERVAL '20 days', 0.84, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(20, 'Nancy', 'Lee', '+1-555-0214', 'San Diego, CA', 'Frontend specialist with React and Vue expertise', 'resume_014.pdf', CURRENT_TIMESTAMP - INTERVAL '35 days', 0.79, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, 'Oscar', 'White', '+1-555-0215', 'Dallas, TX', 'Solutions architect with 8 years of experience', 'resume_015.pdf', CURRENT_TIMESTAMP - INTERVAL '40 days', 0.93, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 5: ADDITIONAL JOB SEEKER SKILLS
-- ============================================

INSERT INTO job_seeker_skills (job_seeker_id, skill) VALUES
(11, 'Java'), (11, 'Python'), (11, 'React'), (11, 'Node.js'), (11, 'PostgreSQL'),
(12, 'Java'), (12, 'Microservices'), (12, 'Kafka'), (12, 'Docker'), (12, 'System Architecture'),
(13, 'Docker'), (13, 'Kubernetes'), (13, 'AWS'), (13, 'Terraform'), (13, 'CI/CD'),
(14, 'React'), (14, 'Vue'), (14, 'TypeScript'), (14, 'CSS'), (14, 'HTML'),
(15, 'System Architecture'), (15, 'Java'), (15, 'AWS'), (15, 'Leadership'), (15, 'Communication')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 6: ADDITIONAL JOB SEEKER EXPERIENCE
-- ============================================

INSERT INTO experiences (job_seeker_id, company, title, start_date, end_date, description) VALUES
(11, 'Tech Solutions', 'Full Stack Developer', '2018-03-01', NULL, 'Developed full-stack applications using modern tech stack'),
(11, 'Digital Agency', 'Junior Developer', '2017-01-01', '2018-02-28', 'Built web applications and APIs'),
(12, 'Cloud Systems', 'Senior Backend Engineer', '2019-06-01', NULL, 'Architected distributed systems and microservices'),
(12, 'Software Corp', 'Backend Developer', '2016-09-01', '2019-05-31', 'Developed backend services and APIs'),
(13, 'Infrastructure Co', 'DevOps Engineer', '2020-02-01', NULL, 'Managed cloud infrastructure and deployment pipelines'),
(14, 'Design Studio', 'Frontend Developer', '2019-08-01', NULL, 'Built responsive web interfaces'),
(15, 'Enterprise Corp', 'Solutions Architect', '2017-01-01', NULL, 'Designed enterprise solutions and system architectures')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 7: ADDITIONAL JOB SEEKER EDUCATION
-- ============================================

INSERT INTO educations (job_seeker_id, institution, degree, field_of_study, graduation_date) VALUES
(11, 'Tech University', 'Bachelor of Science', 'Computer Science', '2017-05-15'),
(12, 'Engineering Institute', 'Master of Science', 'Software Engineering', '2016-05-20'),
(13, 'Cloud Academy', 'Bachelor of Science', 'Information Technology', '2019-05-15'),
(14, 'Design School', 'Bachelor of Arts', 'Web Design', '2019-05-20'),
(15, 'Business University', 'Master of Science', 'Computer Science', '2017-05-15')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 8: ADDITIONAL JOB POSTINGS
-- ============================================

INSERT INTO jobs (recruiter_id, title, description, location, experience_level, industry, status, posted_at, approved_at, created_at, updated_at) VALUES
(6, 'AI/ML Engineer', 'Build machine learning models for AI applications', 'San Francisco, CA', 'SENIOR', 'Technology', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '33 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Python Developer', 'Develop Python applications for data processing', 'Remote', 'MID', 'Technology', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Cloud Architect', 'Design cloud infrastructure solutions', 'San Francisco, CA', 'SENIOR', 'Technology', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '8 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Enterprise Java Developer', 'Build enterprise Java applications', 'New York, NY', 'SENIOR', 'Technology', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '38 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Database Administrator', 'Manage and optimize databases', 'New York, NY', 'MID', 'Technology', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '32 days', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Security Engineer', 'Implement security solutions', 'Remote', 'SENIOR', 'Technology', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '10 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Logistics Coordinator', 'Coordinate logistics operations', 'Houston, TX', 'MID', 'Transportation', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '23 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Supply Chain Analyst', 'Analyze supply chain processes', 'Houston, TX', 'MID', 'Transportation', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Operations Manager', 'Manage logistics operations', 'Houston, TX', 'SENIOR', 'Transportation', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '9 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Education Program Manager', 'Manage educational programs', 'Boston, MA', 'MID', 'Non-Profit', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Community Outreach Coordinator', 'Coordinate community outreach', 'Boston, MA', 'ENTRY', 'Non-Profit', 'APPROVED', CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Grant Writer', 'Write grant proposals', 'Remote', 'MID', 'Non-Profit', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '11 days', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 9: ADDITIONAL JOB REQUIRED SKILLS
-- ============================================

INSERT INTO job_required_skills (job_id, skill) VALUES
(16, 'Python'), (16, 'Machine Learning'), (16, 'Data Analysis'), (16, 'TensorFlow'),
(17, 'Python'), (17, 'Data Processing'), (17, 'SQL'), (17, 'Spark'),
(18, 'AWS'), (18, 'System Architecture'), (18, 'Docker'), (18, 'Kubernetes'),
(19, 'Java'), (19, 'Spring Boot'), (19, 'Microservices'), (19, 'SQL'),
(20, 'PostgreSQL'), (20, 'SQL'), (20, 'Database Design'), (20, 'Performance Tuning'),
(21, 'Security'), (21, 'Cybersecurity'), (21, 'Linux'), (21, 'Networking'),
(22, 'Project Management'), (22, 'Logistics'), (22, 'Communication'), (22, 'Excel'),
(23, 'Data Analysis'), (23, 'Excel'), (23, 'Problem Solving'), (23, 'Communication'),
(24, 'Leadership'), (24, 'Project Management'), (24, 'Communication'), (24, 'Problem Solving'),
(25, 'Project Management'), (25, 'Communication'), (25, 'Leadership'), (25, 'Community Engagement'),
(26, 'Communication'), (26, 'Community Engagement'), (26, 'Problem Solving'), (26, 'Leadership'),
(27, 'Writing'), (27, 'Research'), (27, 'Communication'), (27, 'Project Management')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 10: ADDITIONAL APPLICATIONS
-- ============================================

INSERT INTO applications (job_seeker_id, job_id, status, applied_at, created_at) VALUES
(11, 16, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP),
(11, 17, 'SHORTLISTED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP),
(12, 19, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP),
(12, 20, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP),
(13, 18, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP),
(13, 3, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP),
(14, 2, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP),
(14, 9, 'SHORTLISTED', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP),
(15, 18, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '38 days', CURRENT_TIMESTAMP),
(15, 19, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '32 days', CURRENT_TIMESTAMP),
(1, 16, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '29 days', CURRENT_TIMESTAMP),
(2, 17, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '24 days', CURRENT_TIMESTAMP),
(3, 19, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '33 days', CURRENT_TIMESTAMP),
(4, 20, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '27 days', CURRENT_TIMESTAMP),
(5, 16, 'SHORTLISTED', CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP),
(6, 22, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '23 days', CURRENT_TIMESTAMP),
(7, 25, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '29 days', CURRENT_TIMESTAMP),
(8, 26, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP),
(9, 24, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP),
(10, 27, 'APPLIED', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 11: ADDITIONAL CHATS
-- ============================================

INSERT INTO chats (recruiter_id, job_seeker_id, flagged, flag_reason, created_at) VALUES
(6, 11, false, NULL, CURRENT_TIMESTAMP - INTERVAL '25 days'),
(6, 12, false, NULL, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(7, 13, false, NULL, CURRENT_TIMESTAMP - INTERVAL '30 days'),
(7, 14, false, NULL, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(8, 15, false, NULL, CURRENT_TIMESTAMP - INTERVAL '22 days'),
(8, 1, false, NULL, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(9, 2, false, NULL, CURRENT_TIMESTAMP - INTERVAL '28 days'),
(9, 3, false, NULL, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(1, 4, false, NULL, CURRENT_TIMESTAMP - INTERVAL '16 days'),
(2, 5, false, NULL, CURRENT_TIMESTAMP - INTERVAL '24 days')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 12: ADDITIONAL MESSAGES
-- ============================================

INSERT INTO messages (chat_id, sender_id, content, sent_at, read) VALUES
(6, 7, 'Hi Kevin, we have an exciting AI/ML Engineer position for you', CURRENT_TIMESTAMP - INTERVAL '25 days' + INTERVAL '1 hour', true),
(6, 17, 'Thank you! I am very interested in this role', CURRENT_TIMESTAMP - INTERVAL '25 days' + INTERVAL '2 hours', true),
(6, 7, 'Great! Can you tell me about your ML experience?', CURRENT_TIMESTAMP - INTERVAL '25 days' + INTERVAL '3 hours', true),
(6, 17, 'I have 4 years of experience with TensorFlow and PyTorch', CURRENT_TIMESTAMP - INTERVAL '25 days' + INTERVAL '4 hours', true),
(6, 7, 'Excellent! When can you start?', CURRENT_TIMESTAMP - INTERVAL '24 days' + INTERVAL '1 hour', true),
(6, 17, 'I can start in 2 weeks', CURRENT_TIMESTAMP - INTERVAL '24 days' + INTERVAL '2 hours', true),
(7, 7, 'Hi Laura, interested in our Enterprise Java Developer role?', CURRENT_TIMESTAMP - INTERVAL '20 days' + INTERVAL '1 hour', true),
(7, 18, 'Yes, I am very interested!', CURRENT_TIMESTAMP - INTERVAL '20 days' + INTERVAL '2 hours', true),
(7, 7, 'Tell me about your enterprise experience', CURRENT_TIMESTAMP - INTERVAL '20 days' + INTERVAL '3 hours', true),
(7, 18, 'I have 7 years building enterprise Java applications', CURRENT_TIMESTAMP - INTERVAL '20 days' + INTERVAL '4 hours', true),
(8, 8, 'Hi Mark, we need a DevOps Engineer urgently', CURRENT_TIMESTAMP - INTERVAL '30 days' + INTERVAL '1 hour', true),
(8, 19, 'I am available and interested', CURRENT_TIMESTAMP - INTERVAL '30 days' + INTERVAL '2 hours', true),
(8, 8, 'What is your Kubernetes experience?', CURRENT_TIMESTAMP - INTERVAL '30 days' + INTERVAL '3 hours', true),
(8, 19, 'I have 3 years of Kubernetes in production', CURRENT_TIMESTAMP - INTERVAL '30 days' + INTERVAL '4 hours', true),
(9, 8, 'Hi Nancy, we have a Frontend role for you', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '1 hour', true),
(9, 20, 'Thank you for reaching out!', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '2 hours', true),
(9, 8, 'Do you have React experience?', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '3 hours', true),
(9, 20, 'Yes, 4 years of React development', CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '4 hours', true),
(10, 9, 'Hi Oscar, we need a Solutions Architect', CURRENT_TIMESTAMP - INTERVAL '22 days' + INTERVAL '1 hour', true),
(10, 21, 'I am very interested in this opportunity', CURRENT_TIMESTAMP - INTERVAL '22 days' + INTERVAL '2 hours', true),
(10, 9, 'Tell me about your architecture experience', CURRENT_TIMESTAMP - INTERVAL '22 days' + INTERVAL '3 hours', true),
(10, 21, 'I have 8 years designing enterprise architectures', CURRENT_TIMESTAMP - INTERVAL '22 days' + INTERVAL '4 hours', true),
(11, 1, 'Hi John, interested in our AI/ML position?', CURRENT_TIMESTAMP - INTERVAL '18 days' + INTERVAL '1 hour', true),
(11, 7, 'Yes, I am interested', CURRENT_TIMESTAMP - INTERVAL '18 days' + INTERVAL '2 hours', true),
(11, 1, 'What is your ML background?', CURRENT_TIMESTAMP - INTERVAL '18 days' + INTERVAL '3 hours', true),
(11, 7, 'I have 3 years of machine learning experience', CURRENT_TIMESTAMP - INTERVAL '18 days' + INTERVAL '4 hours', true),
(12, 2, 'Hi Jane, we have a Python Developer role', CURRENT_TIMESTAMP - INTERVAL '28 days' + INTERVAL '1 hour', true),
(12, 8, 'I am interested in this role', CURRENT_TIMESTAMP - INTERVAL '28 days' + INTERVAL '2 hours', true),
(12, 2, 'What is your Python experience?', CURRENT_TIMESTAMP - INTERVAL '28 days' + INTERVAL '3 hours', true),
(12, 8, 'I have 5 years of Python development', CURRENT_TIMESTAMP - INTERVAL '28 days' + INTERVAL '4 hours', true),
(13, 3, 'Hi Mike, we need a DevOps Engineer', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '1 hour', true),
(13, 9, 'I am very interested', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '2 hours', true),
(13, 3, 'Tell me about your DevOps skills', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '3 hours', true),
(13, 9, 'I have 4 years of DevOps experience', CURRENT_TIMESTAMP - INTERVAL '12 days' + INTERVAL '4 hours', true),
(14, 4, 'Hi Sarah, we have a Data Scientist role', CURRENT_TIMESTAMP - INTERVAL '16 days' + INTERVAL '1 hour', true),
(14, 10, 'I am interested', CURRENT_TIMESTAMP - INTERVAL '16 days' + INTERVAL '2 hours', true),
(14, 4, 'What is your ML experience?', CURRENT_TIMESTAMP - INTERVAL '16 days' + INTERVAL '3 hours', true),
(14, 10, 'I have 5 years of data science experience', CURRENT_TIMESTAMP - INTERVAL '16 days' + INTERVAL '4 hours', true),
(15, 5, 'Hi Alex, we have a Frontend role', CURRENT_TIMESTAMP - INTERVAL '24 days' + INTERVAL '1 hour', true),
(15, 11, 'I am interested in this opportunity', CURRENT_TIMESTAMP - INTERVAL '24 days' + INTERVAL '2 hours', true),
(15, 5, 'Tell me about your React experience', CURRENT_TIMESTAMP - INTERVAL '24 days' + INTERVAL '3 hours', true),
(15, 11, 'I have 4 years of React development', CURRENT_TIMESTAMP - INTERVAL '24 days' + INTERVAL '4 hours', true)
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 13: ADDITIONAL NOTIFICATIONS
-- ============================================

INSERT INTO notifications (user_id, type, message, read, created_at) VALUES
(17, 'JOB_APPLICATION', 'Your application to AI/ML Engineer has been received', true, CURRENT_TIMESTAMP - INTERVAL '30 days'),
(17, 'RECRUITER_INTEREST', 'Frank Miller from StartUp Ventures is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '25 days'),
(17, 'CHAT_MESSAGE', 'You have a new message from Frank Miller', true, CURRENT_TIMESTAMP - INTERVAL '25 days'),
(18, 'JOB_APPLICATION', 'Your application to Enterprise Java Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '35 days'),
(18, 'RECRUITER_INTEREST', 'Grace Taylor from Enterprise Solutions Inc is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(18, 'CHAT_MESSAGE', 'You have a new message from Grace Taylor', true, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(19, 'JOB_APPLICATION', 'Your application to Cloud Architect has been received', true, CURRENT_TIMESTAMP - INTERVAL '7 days'),
(19, 'RECRUITER_INTEREST', 'Frank Miller from StartUp Ventures is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '30 days'),
(19, 'CHAT_MESSAGE', 'You have a new message from Frank Miller', true, CURRENT_TIMESTAMP - INTERVAL '30 days'),
(20, 'JOB_APPLICATION', 'Your application to Frontend Developer has been received', true, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(20, 'RECRUITER_INTEREST', 'Henry Anderson from Recruitment Agency Pro is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(20, 'CHAT_MESSAGE', 'You have a new message from Henry Anderson', true, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(21, 'JOB_APPLICATION', 'Your application to Cloud Architect has been received', true, CURRENT_TIMESTAMP - INTERVAL '38 days'),
(21, 'RECRUITER_INTEREST', 'Grace Taylor from Enterprise Solutions Inc is interested in your profile', true, CURRENT_TIMESTAMP - INTERVAL '22 days'),
(21, 'CHAT_MESSAGE', 'You have a new message from Grace Taylor', true, CURRENT_TIMESTAMP - INTERVAL '22 days'),
(2, 'JOB_APPROVED', 'Your job posting for AI/ML Engineer has been approved', true, CURRENT_TIMESTAMP - INTERVAL '33 days'),
(2, 'JOB_APPROVED', 'Your job posting for Python Developer has been approved', true, CURRENT_TIMESTAMP - INTERVAL '26 days'),
(3, 'JOB_APPROVED', 'Your job posting for Enterprise Java Developer has been approved', true, CURRENT_TIMESTAMP - INTERVAL '38 days'),
(3, 'JOB_APPROVED', 'Your job posting for Database Administrator has been approved', true, CURRENT_TIMESTAMP - INTERVAL '30 days'),
(4, 'JOB_APPROVED', 'Your job posting for Logistics Coordinator has been approved', true, CURRENT_TIMESTAMP - INTERVAL '23 days'),
(4, 'JOB_APPROVED', 'Your job posting for Supply Chain Analyst has been approved', true, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(5, 'JOB_APPROVED', 'Your job posting for Education Program Manager has been approved', true, CURRENT_TIMESTAMP - INTERVAL '28 days'),
(5, 'JOB_APPROVED', 'Your job posting for Community Outreach Coordinator has been approved', true, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(7, 'JOB_APPLICATION', 'You have 5 new applications for your job postings', true, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(8, 'JOB_APPLICATION', 'You have 3 new applications for your job postings', true, CURRENT_TIMESTAMP - INTERVAL '3 days'),
(9, 'JOB_APPLICATION', 'You have 2 new applications for your job postings', true, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(10, 'JOB_APPLICATION', 'You have 4 new applications for your job postings', true, CURRENT_TIMESTAMP - INTERVAL '1 day')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 14: ADDITIONAL SHORTLISTS
-- ============================================

INSERT INTO shortlists (recruiter_id, job_seeker_id, added_at) VALUES
(6, 11, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(6, 12, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(7, 13, CURRENT_TIMESTAMP - INTERVAL '25 days'),
(7, 14, CURRENT_TIMESTAMP - INTERVAL '10 days'),
(8, 15, CURRENT_TIMESTAMP - INTERVAL '17 days'),
(8, 1, CURRENT_TIMESTAMP - INTERVAL '14 days'),
(9, 2, CURRENT_TIMESTAMP - INTERVAL '23 days'),
(9, 3, CURRENT_TIMESTAMP - INTERVAL '8 days'),
(1, 5, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(2, 6, CURRENT_TIMESTAMP - INTERVAL '19 days')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 15: MODERATION LOGS (for Admin dashboard)
-- ============================================

INSERT INTO moderation_logs (admin_id, chat_id, user_id, action, reason, created_at) VALUES
(1, 1, NULL, 'FLAG_CHAT', 'Inappropriate language detected', CURRENT_TIMESTAMP - INTERVAL '10 days'),
(1, NULL, 7, 'DISABLE_USER_CHAT', 'Repeated violations', CURRENT_TIMESTAMP - INTERVAL '8 days'),
(1, 2, NULL, 'UNFLAG_CHAT', 'False positive, chat is appropriate', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(1, NULL, 8, 'ENABLE_USER_CHAT', 'User appealed and was approved', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(1, 3, NULL, 'FLAG_CHAT', 'Spam content detected', CURRENT_TIMESTAMP - INTERVAL '7 days'),
(1, NULL, 9, 'DISABLE_USER_CHAT', 'Harassment complaint', CURRENT_TIMESTAMP - INTERVAL '6 days'),
(1, 4, NULL, 'FLAG_CHAT', 'Suspicious activity', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(1, NULL, 10, 'ENABLE_USER_CHAT', 'Suspension period ended', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(1, 5, NULL, 'UNFLAG_CHAT', 'Chat reviewed and cleared', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(1, NULL, 11, 'DISABLE_USER_CHAT', 'Policy violation', CURRENT_TIMESTAMP - INTERVAL '12 hours')
ON CONFLICT DO NOTHING;

-- ============================================
-- SECTION 16: VERIFY DATA POPULATION
-- ============================================

-- Summary statistics (for verification)
-- SELECT 'Users' as entity, COUNT(*) as count FROM users
-- UNION ALL
-- SELECT 'Job Seekers', COUNT(*) FROM job_seekers
-- UNION ALL
-- SELECT 'Recruiters', COUNT(*) FROM recruiters
-- UNION ALL
-- SELECT 'Jobs', COUNT(*) FROM jobs
-- UNION ALL
-- SELECT 'Applications', COUNT(*) FROM applications
-- UNION ALL
-- SELECT 'Chats', COUNT(*) FROM chats
-- UNION ALL
-- SELECT 'Messages', COUNT(*) FROM messages
-- UNION ALL
-- SELECT 'Notifications', COUNT(*) FROM notifications
-- UNION ALL
-- SELECT 'Shortlists', COUNT(*) FROM shortlists
-- UNION ALL
-- SELECT 'Moderation Logs', COUNT(*) FROM moderation_logs;

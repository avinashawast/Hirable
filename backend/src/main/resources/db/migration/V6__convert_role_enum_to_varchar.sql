-- Convert role column from enum to varchar to avoid type casting issues
ALTER TABLE users ALTER COLUMN role TYPE VARCHAR(50);

-- Drop the enum type if it's no longer needed
DROP TYPE IF EXISTS user_role CASCADE;

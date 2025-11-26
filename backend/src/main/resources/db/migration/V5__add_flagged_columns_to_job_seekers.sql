-- Add flagged and flag_reason columns to job_seekers table
ALTER TABLE job_seekers
ADD COLUMN flagged BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN flag_reason TEXT;

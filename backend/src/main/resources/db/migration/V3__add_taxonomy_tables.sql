-- Create indexes for better query performance on taxonomy tables
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_industries_name ON industries(name);
CREATE INDEX idx_skills_name ON skills(name);

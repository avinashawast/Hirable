-- Add chat_disabled column to users table
ALTER TABLE users ADD COLUMN chat_disabled BOOLEAN NOT NULL DEFAULT false;

-- Create moderation_action enum type
CREATE TYPE moderation_action AS ENUM ('FLAG_CHAT', 'UNFLAG_CHAT', 'DISABLE_USER_CHAT', 'ENABLE_USER_CHAT');

-- Create moderation_logs table
CREATE TABLE moderation_logs (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT NOT NULL,
    chat_id BIGINT,
    user_id BIGINT,
    action moderation_action NOT NULL,
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for moderation logs
CREATE INDEX idx_moderation_log_admin_id ON moderation_logs(admin_id);
CREATE INDEX idx_moderation_log_chat_id ON moderation_logs(chat_id);
CREATE INDEX idx_moderation_log_user_id ON moderation_logs(user_id);
CREATE INDEX idx_moderation_log_created_at ON moderation_logs(created_at);

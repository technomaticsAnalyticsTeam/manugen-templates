-- ManuGen Database Schema
-- PostgreSQL 15+

-- Note: Database 'manugen' is created by POSTGRES_DB environment variable
-- The database already exists when this script runs

-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS templates;

-- Set search path for current session
SET search_path TO templates;

-- Users table
CREATE TABLE IF NOT EXISTS "user" (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Templates table
CREATE TABLE IF NOT EXISTS template (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    fields JSONB
);

-- User permissions table
CREATE TABLE IF NOT EXISTS user_permission (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    template_id BIGINT NOT NULL REFERENCES template(id) ON DELETE CASCADE,
    action INTEGER NOT NULL,
    UNIQUE (user_id, template_id, action)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_template_category ON template(category);
CREATE INDEX IF NOT EXISTS idx_user_permission_user_id ON user_permission(user_id);
CREATE INDEX IF NOT EXISTS idx_user_permission_template_id ON user_permission(template_id);
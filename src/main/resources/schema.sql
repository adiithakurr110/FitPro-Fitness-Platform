-- ===================================================================
-- FitPro Relational Database Schema
-- Idempotent, non-destructive table definitions for H2 and MySQL.
-- ===================================================================

CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS workouts (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(120) NOT NULL,
    description  VARCHAR(500),
    type         VARCHAR(20) NOT NULL,
    level        VARCHAR(20) NOT NULL,
    duration_min INT NOT NULL,
    met_value    DOUBLE NOT NULL,
    trainer_id   INT NULL,
    FOREIGN KEY (trainer_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS enrollments (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    workout_id  INT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_workout UNIQUE (user_id, workout_id),
    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS progress_logs (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    workout_id  INT NOT NULL,
    minutes     INT NOT NULL,
    weight_kg   DOUBLE NOT NULL,
    calories    DOUBLE NOT NULL,
    logged_on   DATE NOT NULL,
    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

-- Helpful secondary indexes for query performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_workouts_type ON workouts(type);
CREATE INDEX IF NOT EXISTS idx_progress_user ON progress_logs(user_id);

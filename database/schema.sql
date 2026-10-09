-- =====================================================
-- FitPro database schema + sample data
-- =====================================================
CREATE DATABASE IF NOT EXISTS fitpro CHARACTER SET utf8mb4;
USE fitpro;

DROP TABLE IF EXISTS progress_logs;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS workouts;
DROP TABLE IF EXISTS users;

-- Members train, trainers publish workout programs
CREATE TABLE users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE workouts (
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

CREATE TABLE enrollments (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    workout_id  INT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_workout UNIQUE (user_id, workout_id),
    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

CREATE TABLE progress_logs (
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

-- Sample programs so the catalogue is not empty on first run
INSERT INTO workouts (title, description, type, level, duration_min, met_value) VALUES
('Morning Jog Starter',   'Easy 30 minute jog to build stamina.',               'CARDIO',   'BEGINNER',     30, 7.0),
('HIIT Fat Burner',       'High intensity intervals: burpees, jumps, sprints.', 'CARDIO',   'ADVANCED',     25, 10.0),
('Full Body Strength',    'Squats, push-ups, rows and planks with dumbbells.',  'STRENGTH', 'INTERMEDIATE', 45, 5.0),
('Upper Body Power',      'Chest, shoulders and arms hypertrophy session.',     'STRENGTH', 'ADVANCED',     50, 6.0),
('Sunrise Yoga Flow',     'Gentle vinyasa flow for flexibility and focus.',     'YOGA',     'BEGINNER',     40, 3.0),
('Power Yoga',            'Strength-building yoga with long holds.',            'YOGA',     'INTERMEDIATE', 45, 4.0);

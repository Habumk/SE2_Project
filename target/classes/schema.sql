-- ====================================
-- K-Learn Database Schema (SRS Aligned)
-- ====================================

CREATE DATABASE IF NOT EXISTS klearn CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE klearn;

CREATE TABLE IF NOT EXISTS user (
    user_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL UNIQUE,
    password_hash    VARCHAR(255) NOT NULL,
    role             VARCHAR(20)  DEFAULT 'ROLE_LEARNER',
    streak_count     INT          DEFAULT 0,
    last_active_date DATE,
    total_xp         INT          DEFAULT 0,
    current_level    INT          DEFAULT 1,
    theme            VARCHAR(10)  DEFAULT 'light',
    created_at       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    last_login_at    DATETIME,
    is_locked        TINYINT(1)   DEFAULT 0,
    lock_until       DATETIME
);

CREATE TABLE IF NOT EXISTS course (
    course_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    level       VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS lesson (
    lesson_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id   BIGINT NOT NULL,
    title       VARCHAR(255) NOT NULL,
    order_index INT NOT NULL,
    xp_reward   INT DEFAULT 10,
    FOREIGN KEY (course_id) REFERENCES course(course_id)
);

CREATE TABLE IF NOT EXISTS theory (
    theory_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lesson_id BIGINT NOT NULL,
    content   TEXT NOT NULL,
    FOREIGN KEY (lesson_id) REFERENCES lesson(lesson_id)
);

CREATE TABLE IF NOT EXISTS vocabulary (
    vocab_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    lesson_id        BIGINT NOT NULL,
    hangul           VARCHAR(100) NOT NULL,
    romanization     VARCHAR(100),
    meaning          VARCHAR(255) NOT NULL,
    example_sentence TEXT,
    audio_url        VARCHAR(500),
    FOREIGN KEY (lesson_id) REFERENCES lesson(lesson_id)
);

CREATE TABLE IF NOT EXISTS exercise (
    exercise_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lesson_id   BIGINT NOT NULL,
    type        ENUM('listening','speaking','reading','writing') NOT NULL,
    audio_url   VARCHAR(500),
    FOREIGN KEY (lesson_id) REFERENCES lesson(lesson_id)
);

CREATE TABLE IF NOT EXISTS question (
    question_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    exercise_id   BIGINT NOT NULL,
    content       TEXT NOT NULL,
    expected_text VARCHAR(500),
    audio_url     VARCHAR(500),
    FOREIGN KEY (exercise_id) REFERENCES exercise(exercise_id)
);

CREATE TABLE IF NOT EXISTS answer (
    answer_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    content     TEXT NOT NULL,
    is_correct  TINYINT(1) DEFAULT 0,
    explanation TEXT,
    FOREIGN KEY (question_id) REFERENCES question(question_id)
);

CREATE TABLE IF NOT EXISTS user_answer (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    question_id         BIGINT NOT NULL,
    selected_answer_id  BIGINT,
    written_response    TEXT,
    stt_result          TEXT,
    pronunciation_score DECIMAL(5,2),
    is_correct          TINYINT(1),
    submitted_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (question_id) REFERENCES question(question_id),
    FOREIGN KEY (selected_answer_id) REFERENCES answer(answer_id)
);

CREATE TABLE IF NOT EXISTS user_progress (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    status    VARCHAR(20) DEFAULT 'not_started',
    score     INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (lesson_id) REFERENCES lesson(lesson_id),
    UNIQUE KEY uq_user_lesson (user_id, lesson_id)
);

CREATE TABLE IF NOT EXISTS lesson_result (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    lesson_id       BIGINT NOT NULL,
    reading_score   INT DEFAULT 0,
    listening_score INT DEFAULT 0,
    speaking_score  INT DEFAULT 0,
    writing_score   INT DEFAULT 0,
    total_score     INT DEFAULT 0,
    xp_earned       INT DEFAULT 0,
    completed_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (lesson_id) REFERENCES lesson(lesson_id)
);

CREATE TABLE IF NOT EXISTS badge (
    badge_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    icon_url        VARCHAR(500),
    condition_type  VARCHAR(50),
    condition_value INT
);

CREATE TABLE IF NOT EXISTS user_badge (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT NOT NULL,
    badge_id  BIGINT NOT NULL,
    earned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (badge_id) REFERENCES badge(badge_id),
    UNIQUE KEY uq_user_badge (user_id, badge_id)
);

CREATE TABLE IF NOT EXISTS speaking_room (
    room_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    created_by       BIGINT NOT NULL,
    max_participants INT DEFAULT 10,
    is_active        TINYINT(1) DEFAULT 1,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user(user_id)
);

CREATE TABLE IF NOT EXISTS room_participant (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id   BIGINT NOT NULL,
    user_id   BIGINT NOT NULL,
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES speaking_room(room_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

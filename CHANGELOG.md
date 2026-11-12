# Changelog

All notable changes to the **FitPro** project are documented in this file following the [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) format.

---

## [1.0.0] - 2026-11-12

### Added
- **Core Web Architecture**: 7 Servlets (`LoginServlet`, `RegisterServlet`, `DashboardServlet`, `WorkoutServlet`, `ProgressServlet`, `LogoutServlet`, `AdminServlet`) following the MVC design pattern.
- **Security & Session Layer**: `AuthFilter` guarding `/app/*` endpoints, HTTP-only cookie flag, session invalidation on login/logout to prevent fixation.
- **PBKDF2 Password Hashing**: Cryptographic password hashing using PBKDF2-HMAC-SHA256 with 64-byte random salt and 65,536 iterations.
- **Dual-Engine Database Connectivity**: Seamless dual-engine JDBC connection supporting local MySQL 8 with an automatic, zero-config fallback to embedded H2 database.
- **Object-Oriented Design & Polymorphism**:
  - `User` abstraction with `Member` and `Trainer` polymorphic behaviors.
  - `Workout` abstraction with `CardioWorkout`, `StrengthWorkout`, and `YogaWorkout` implementing custom EPOC and MET formulas via `CalorieCalculable`.
- **Desktop Administration GUI**: `FitProAdminGUI.java` built with Java Swing and `SwingWorker` for non-blocking asynchronous data fetching on background worker threads.
- **Multithreading & Synchronization**: Asynchronous `NotificationService` utilizing `ExecutorService` and `synchronized` thread-safe counter.
- **Comprehensive Test Suite**: 32 automated JUnit 5 tests covering Models, DAOs, Services, Security, and Multithreading.
- **Progress Report CSV Export**: Member-facing one-click CSV download of complete historical workout sessions (`/app/progress?action=export`).
- **Interactive BMI & Health Calculator**: Real-time client-side fitness widget calculating Body Mass Index and customized daily caloric burn recommendations.
- **Live Search & Filtering**: Real-time JavaScript search filter for the workout program catalog.
- **Desktop GUI Quick Launchers**: Added `run-gui.bat` and `exec-maven-plugin` configuration for one-click execution.

### Changed
- Refactored `WorkoutDAO.create` to safely handle nullable `trainer_id` foreign key mappings.
- Hardened `PasswordUtil.verify` against null pointers.

### Team Sprints Completed
- **Sprint 1 (Architecture & Database)**: Relational schema, dual-engine connection, base DAOs.
- **Sprint 2 (Authentication & Web Core)**: PBKDF2 hashing, session management, Servlets, and JSP views.
- **Sprint 3 (Features & Analytics)**: Program enrollment, workout session logging, dynamic MET calorie calculation.
- **Sprint 4 (Desktop GUI & Multithreading)**: Swing admin portal, SwingWorker asynchronous loading, Notification pool.
- **Sprint 5 (Quality Assurance & Innovation)**: 32 JUnit 5 test cases, CSV export, interactive BMI tool, and documentation.

# FitPro — Minimalist Fitness Technology Platform

[![Java](https://img.shields.io/badge/Java-11%20%7C%2017%20%7C%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Servlets](https://img.shields.io/badge/Servlets-4.0-0071e3?style=for-the-badge&logo=apache-tomcat&logoColor=white)](https://tomcat.apache.org/)
[![JSP](https://img.shields.io/badge/JSP%20%26%20JSTL-2.3%20%2F%201.2-1d1d1f?style=for-the-badge)](https://tomcat.apache.org/)
[![UI Design](https://img.shields.io/badge/UI-Minimal%20Apple%20Inspired-0071e3?style=for-the-badge&logo=apple&logoColor=white)](https://apple.com)
[![JDBC](https://img.shields.io/badge/JDBC-Dual%20Engine-166534?style=for-the-badge&logo=databricks&logoColor=white)](https://docs.oracle.com/javase/tutorial/jdbc/)
[![Database](https://img.shields.io/badge/Database-H2%20%7C%20MySQL%208-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.h2database.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-32%20Tests%20Passing-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)

FitPro is a full-stack **Java Enterprise Web & Desktop Application** engineered for digital athletic training, intelligent workout tracking, and platform moderation. Designed with a **minimal Apple-inspired visual aesthetic**, the user interface emphasizes generous whitespace, crisp typography, subtle hairline borders, and distraction-free usability.

---

## 🍎 Minimal Apple-Inspired Design System

The FitPro design language is inspired by the design principles of **Apple.com** and **Apple Fitness+**:

* **Minimalist Canvas & Whitespace**: Generous spacing, soft neutral backgrounds (`#f5f5f7`), and pure white cards (`#ffffff`) create an airy, premium feel without cluttered boxes.
* **Refined Typography**: Native Apple font hierarchy (`-apple-system`, `SF Pro Display`, `SF Pro Text`) with high-contrast obsidian headlines (`#1d1d1f`) and muted secondary descriptions (`#6e6e73`).
* **Restrained Accent Palette**: Precision Apple Blue (`#0071e3`) for primary actions and active states, paired with deep charcoal pill buttons (`#1d1d1f`).
* **Consistent Corner Geometry**: Apple-standard radii (`20px` for cards, `980px` for pill buttons and category tags).
* **Hairline Borders & Soft Elevation**: Subtle borders (`1px solid rgba(0, 0, 0, 0.08)`) and diffuse ambient drop shadows (`0 4px 20px rgba(0, 0, 0, 0.04)`).
* **Translucent Navigation**: Apple-style sticky blurred header (`backdrop-filter: blur(20px)` and 82% opacity).
* **Accessibility & Motion**: Compliant color contrast, accessible keyboard focus rings (`box-shadow: 0 0 0 4px rgba(0, 113, 227, 0.22)`), and `@media (prefers-reduced-motion)` support.

---

## 👥 Three Independent Roles Architecture

FitPro implements strict **Role-Based Access Control (RBAC)** across three distinct personas:

```
               ┌────────────────────────────────────────────────────────┐
               │                  FitPro Account Base                   │
               │            com.fitpro.model.User (Abstract)            │
               └──────────────────────────┬─────────────────────────────┘
                                          │
            ┌─────────────────────────────┼─────────────────────────────┐
            │                             │                             │
            ▼                             ▼                             ▼
┌─────────────────────────┐   ┌─────────────────────────┐   ┌─────────────────────────┐
│     Member (MEMBER)     │   │    Trainer (TRAINER)    │   │      Admin (ADMIN)      │
├─────────────────────────┤   ├─────────────────────────┤   ├─────────────────────────┤
│ • Personal Dashboard    │   │ • Coach Studio          │   │ • Superuser Console     │
│ • Workout Enrollment    │   │ • Program Publishing    │   │ • User & Role Mgmt      │
│ • Session Logging       │   │ • Member Adherence      │   │ • Content Moderation    │
│ • Calorie Analytics     │   │ • Progress Feeds        │   │ • JVM Telemetry Status  │
│ • BMI & Health Target   │   │ • Coaching Advice       │   │ • Master Enrollment Log │
│ • CSV Report Export     │   │ • Subscriptions Review  │   │ • Cascade User Delete   │
└─────────────────────────┘   └─────────────────────────┘   └─────────────────────────┘
```

### Pre-Seeded Demonstration Accounts

The platform automatically initializes three demonstration accounts on first run:

| Role | Email Address | Password | Default Capabilities |
|---|---|---|---|
| **Member** | `member@fitpro.com` | `member123` | Enrolls in programs, logs sessions, tracks calories, computes BMI |
| **Trainer** | `trainer@fitpro.com` | `trainer123` | Publishes routines, inspects assigned member progress, gives advice |
| **Administrator** | `admin@fitpro.com` | `admin123` | Reassigns user roles, moderates workout catalogue, monitors system telemetry |

---

## 📱 Page-by-Page Overview

### 1. Landing Page (`index.jsp` & `index.html`)
* **Navigation Bar**: Translucent header with FitPro emblem, Features, Workouts, How It Works, About, and Sign In / Register buttons.
* **Striking Hero Section**: Headline *"Your fitness. Your pace. Your progress."* with clean call-to-actions.
* **Apple Studio Visual Showcase**: Live UI simulation showcasing Active Energy, Exercise Time, and Recovery metrics.
* **Four Feature Pillars**: Personalized Workouts, Intelligent Progress Tracking, Certified Trainer Guidance, and Holistic Fitness Planning.
* **Workout Showcase**: Detailed cards for Cardio, Strength, and Yoga disciplines.
* **How It Works**: 3-step walkthrough from program selection to verified athletic gains.
* **Interactive Tool**: Real-time Body Mass Index (BMI) & daily caloric target calculator directly on the landing page.
* **Minimal Footer**: Multi-column navigation with technology disclosures and copyright.

### 2. Authentication Pages (`login.jsp` & `register.jsp`)
* Minimalist centered cards on soft canvas.
* Password visibility toggle with accessible eye icons.
* Role selector on registration (Member, Trainer, Admin).
* Quick demo credentials box for 1-click test credentials reference during viva evaluation.
* Fully connected to backend `LoginServlet` and `RegisterServlet` with salted PBKDF2 password hashing.

### 3. Member Dashboard (`dashboard.jsp`)
* Personalized greeting with real session greeting polymorphism.
* 4-card metric overview: Sessions Completed, Minutes Trained, Total Calories Burned, and Enrolled Programs.
* Current workout plan section with one-click session logging.
* Apple Health style calorie breakdown progress bars by workout discipline.
* Interactive BMI & Calorie Target Calculator.
* Trainer Recommendations & Nutrition Guide (hydration, protein targets, and sleep recovery).
* Recent activity feed with link to full history.

### 4. Trainer Studio (`dashboard.jsp` for TRAINERS)
* Coach greeting and certified coach status badge.
* Quick metrics: Assigned Member Subscriptions, Programs Managed, and Sessions Recorded.
* Assigned Members Directory with email and real-time status indicators.
* Live member progress feed showing workouts logged by members across the platform.
* Coaching Guidance & Recommendations panel.
* Direct shortcut to publish new workout routines.

### 5. Administrator Portal (`admin.jsp` & `AdminServlet`)
* Strict security check: non-admins are blocked from accessing admin actions.
* Real-time telemetry summary: Total Users, Active Programs, Subscriptions, Completed Sessions.
* **User & Role Management Table**: Live role re-assignment (Member ↔ Trainer ↔ Admin) and cascade user deletion.
* **Workout Content Moderation**: Full catalogue table with MET intensities, duration, and one-click removal.
* **JVM & Server Diagnostics**: Live heap memory usage (Used MB / Total MB), CPU cores, and relational database status.
* **Master Enrollment Directory**: Full subscriber audit trail.

### 6. Workout Catalogue (`workouts.jsp`)
* Category filter chips (All Programs, Cardio, Strength, Yoga).
* Instant client-side search filtering by title, coach, or difficulty.
* Program cards with difficulty badges, duration, coach name, and one-click Enroll / Enrolled indicators.
* Collapsible Program Publisher for Trainers and Administrators.

### 7. Session Logger & History (`progress.jsp`)
* Clean form to record workout duration and body weight.
* Automatic polymorphic calorie calculation based on workout MET and EPOC multiplier.
* Historical training session table.
* One-click CSV Export button downloading `fitpro_progress_report.csv`.

---

## 💻 Tech Stack & Architecture

| Layer | Technology | Details |
|---|---|---|
| **Backend Core** | Java 11+ (JDK 17/21/26 ready) | Layered MVC, OOP inheritance/polymorphism, Collections, Multithreading |
| **Web Controllers** | Java EE Servlets 4.0 | 7 Servlets (`Login`, `Register`, `Logout`, `Dashboard`, `Workout`, `Progress`, `Admin`) |
| **Security Filter** | `AuthFilter` (`@WebFilter("/app/*")`) | Intercepts protected routes; redirects unauthenticated visitors |
| **View Tier** | JSP 2.3 + JSTL 1.2 | Dynamic server-side rendering, XSS defense via `<c:out>`, modular JSPF fragments |
| **Frontend Design** | CSS3, HTML5, Vanilla JavaScript | Apple-inspired minimal design system, CSS Custom Properties, Flexbox, CSS Grid |
| **Database & JDBC** | MySQL 8 / Embedded H2 | Dual-engine JDBC architecture with automated schema initialization |
| **Testing** | JUnit 5 (Jupiter 5.10.1) | 32 automated unit and integration tests |
| **Build & Container** | Maven 3.9+ & Cargo Tomcat 9 | Embedded Tomcat container runner (`mvn package cargo:run`) |

---

## 🚀 Execution & Quick Start Guide

### Prerequisites
* Java JDK 11 or higher (`java -version`)
* Maven 3.6+ (`mvn -version`)

### 1. Run Automated Tests
```bash
cd fitpro
..\tools\apache-maven-3.9.6\bin\mvn.cmd test
```
*Expected: 32 tests run, 0 failures, BUILD SUCCESS.*

### 2. Start the Web Application
```bash
# From workspace root:
run-web-app.bat

# Or using Maven directly:
cd fitpro
..\tools\apache-maven-3.9.6\bin\mvn.cmd package cargo:run
```
Open your browser and navigate to:
**`http://localhost:8080/fitpro/`**

### 3. Launch Desktop Admin GUI
```bash
# From workspace root:
run-admin-gui.bat

# Or using Maven:
cd fitpro
..\tools\apache-maven-3.9.6\bin\mvn.cmd exec:java
```

---

## 🧪 Automated Test Suite (32 Passing Tests)

FitPro includes comprehensive JUnit 5 tests covering every critical system component:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.fitpro.dao.DAOTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fitpro.model.UserModelTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fitpro.model.WorkoutModelTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fitpro.service.AuthServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fitpro.service.ProgressServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fitpro.service.WorkoutServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fitpro.util.DBConnectionTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.fitpro.util.PasswordUtilTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] Results: Tests run: 32, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🎓 Java Viva Talking Points

When presenting or demonstrating FitPro:

1. **Object-Oriented Design**:
   * **Inheritance & Abstraction**: Base abstract class `User` is extended by `Member`, `Trainer`, and `Admin`. Base class `Workout` is extended by `CardioWorkout`, `StrengthWorkout`, and `YogaWorkout`.
   * **Polymorphism**: `CalorieCalculable` interface overrides energy expenditure based on physiological disciplines (standard MET for Cardio, 10% EPOC bonus for Strength, 0.90 factor for Yoga).
   * **Encapsulation**: Private fields across all models with typed accessors.

2. **Multithreading**:
   * `NotificationService` uses `ExecutorService` thread pooling to dispatch notifications asynchronously without blocking HTTP servlet responses.
   * `FitProAdminGUI` uses `SwingWorker` to query the database on a background thread while keeping the Swing GUI Event Dispatch Thread (EDT) responsive.

3. **Database & Security**:
   * Dual-engine connectivity auto-falls back to embedded H2 if MySQL is unavailable.
   * 100% of SQL statements use parameterized `PreparedStatement` to eliminate SQL injection.
   * Passwords are encrypted using salted PBKDF2-HMAC-SHA256 with 65,536 iterations.

# FitPro — Online Fitness Training & Management Platform

FitPro is a full-stack **Java Web and Desktop Application** designed for workout routine management, session progress tracking, and administrative oversight. The project demonstrates core Java engineering concepts, including Object-Oriented Programming (OOP), Java Servlets, JSP/JSTL, JDBC data access, multithreading, and Java Swing, wrapped in an Apple-inspired minimal user interface.

---

## 1. Technologies Used

* **Core Platform**: Java 11+ (Compatible with JDK 11, 17, and 21)
* **Web Tier**: Java EE Servlets 4.0 (`@WebServlet`, `@WebFilter`, `@WebListener`)
* **Presentation Tier**: JavaServer Pages (JSP 2.3), JSTL 1.2, HTML5, CSS3, minimal Vanilla JavaScript
* **Desktop Client**: Java Swing (`JFrame`, `JTable`, `SwingWorker` for asynchronous threading)
* **Persistence Tier**: JDBC with PreparedStatement, dual-mode H2 Embedded Database & MySQL 8.x
* **Build Tool**: Apache Maven 3.6+
* **Testing Framework**: JUnit 5 (Jupiter 5.10.1) — 39 automated tests
* **Runtime Container**: Apache Tomcat 9 (automated via Cargo Maven Plugin)

---

## 2. Actual Implemented Features

### A. Role-Based Access Control (3 Distinct Roles)
1. **Member (`MEMBER`)**:
   * Browse published workout routines across Cardio, Strength, and Yoga.
   * Search workouts by title/description keywords and filter by discipline or difficulty.
   * Enroll in workout programs.
   * Log completed training sessions (duration, body weight, calculated caloric expenditure).
   * View personal metrics and caloric breakdown charts by workout discipline.
   * Interactive Body Mass Index (BMI) & daily caloric target calculator.
   * Export historical training sessions to a standard CSV report (`fitpro_progress_report.csv`).
2. **Trainer (`TRAINER`)**:
   * Publish new workout programs with duration and MET intensity calibration.
   * Delete programs authored by themselves (cannot delete other coaches' programs).
   * Monitor assigned member subscriptions and review workout sessions logged across the platform.
   * Access coaching guidelines (progressive overload, pacing, active recovery).
3. **Administrator (`ADMIN`)**:
   * Manage user accounts and reassign roles (Member ↔ Trainer ↔ Admin).
   * Delete accounts (with safeguards preventing self-deletion and self-demotion).
   * Moderate workout catalogue content across the entire platform.
   * Review JVM diagnostics (heap memory usage, total memory, active CPU cores).
   * Review master subscriber and progress logs.

### B. Desktop Administration GUI (Java Swing)
* Standalone desktop application connecting directly to the database via JDBC DAOs.
* Uses `SwingWorker` to execute database queries on a background thread without freezing the Swing Event Dispatch Thread (EDT).
* Live enrolled member directory display in a searchable, sortable `JTable`.
* Real-time platform summary metrics and 1-click web application browser launcher.

---

## 3. Project Structure

```
fitpro/
├── pom.xml                               # Maven project definition and dependencies
├── README.md                             # Project overview and instructions
├── index.html                            # Minimal Apple-inspired static showcase
├── vercel.json                           # Static showcase hosting configuration
├── run-app.bat                           # 1-click Windows batch runner for Tomcat 9 web app
├── run-gui.bat                           # 1-click Windows batch runner for Desktop Swing GUI
├── docs/
│   ├── DEVELOPMENT.md                    # Developer and architecture guide
│   ├── FitPro_Presentation_v3.pdf        # Presentation slides (PDF)
│   └── FitPro_Presentation_v3.pptx       # Presentation slides (PowerPoint)
└── src/
    ├── main/
    │   ├── java/com/fitpro/
    │   │   ├── dao/                      # JDBC DAOs (BaseDAO, UserDAO, WorkoutDAO, etc.)
    │   │   ├── exception/                # Checked application exceptions (AppException)
    │   │   ├── filter/                   # Session authentication filter (AuthFilter)
    │   │   ├── gui/                      # Desktop Swing Admin GUI (FitProAdminGUI)
    │   │   ├── listener/                 # Context listener for resource shutdown
    │   │   ├── model/                    # Domain entities (User, Member, Trainer, Admin, Workout)
    │   │   ├── service/                  # Business logic (AuthService, WorkoutService, etc.)
    │   │   ├── servlet/                  # Web controllers (Login, Register, Dashboard, etc.)
    │   │   └── util/                     # Helpers (DBConnection, PasswordUtil)
    │   ├── resources/
    │   │   ├── db.properties             # Database configuration
    │   │   └── schema.sql                # Non-destructive relational schema definitions
    │   └── webapp/
    │       ├── index.jsp                 # Apple-inspired web landing page
    │       ├── css/style.css             # Apple-inspired design system stylesheet
    │       ├── js/app.js                 # Minimal client-side interactivity
    │       └── WEB-INF/
    │           ├── web.xml               # Deployment descriptor
    │           └── views/                # Modular JSP views and fragments
    └── test/java/com/fitpro/             # Automated test suite (39 JUnit 5 tests)
```

---

## 4. Prerequisites

* **Java Development Kit (JDK)**: Version 11 or higher (`java -version`)
* **Apache Maven**: Version 3.6 or higher (`mvn -version`)

---

## 5. Database Configuration

FitPro uses a dual-engine JDBC architecture managed via `src/main/resources/db.properties`:

1. **Embedded H2 Database (Default)**:
   * Requires no external installation.
   * Automatically initializes non-destructive tables in `./data/fitpro_db.mv.db` upon first connection.
   * Auto-seeds sample workouts and 3 demo accounts (Admin, Trainer, Member).
2. **External MySQL 8.x (Optional)**:
   * Set `db.type=mysql` in `db.properties` or set environment variable `DB_TYPE=mysql`.
   * Configure `db.mysql.url`, `db.mysql.user`, and `db.mysql.password`, or supply `DB_URL`, `DB_USER`, `DB_PASSWORD`.
   * If MySQL is unreachable, the system explicitly logs a warning and falls back safely to H2.

---

## 6. Build and Run Instructions

### A. Run Automated Tests
```bash
mvn clean test
```
*Expected: 39 tests executed, 0 failures, 0 errors, BUILD SUCCESS.*

### B. Run the Web Application on Tomcat 9
```bash
# Using the pre-configured Cargo plugin (downloads Tomcat 9 automatically):
mvn package cargo:run
```
*Access the web application at:* **`http://localhost:8080/fitpro/`**

### C. Run the Desktop Administration Swing GUI
```bash
# Runs com.fitpro.gui.FitProAdminGUI via exec-maven-plugin:
mvn compile exec:java
```

### D. Build the Distributable WAR
```bash
mvn clean package
```
*Generates `target/fitpro.war` for deployment to any standalone Tomcat server.*

---

## 7. Pre-Seeded Demonstration Accounts

For viva demonstration and evaluation:

| Role | Email Address | Password | Primary Actions |
|---|---|---|---|
| **Member** | `member@fitpro.com` | `member123` | Browse workouts, enroll, log sessions, view calorie breakdown, export CSV |
| **Trainer** | `trainer@fitpro.com` | `trainer123` | Publish programs, delete own programs, inspect assigned member subscriptions |
| **Administrator** | `admin@fitpro.com` | `admin123` | Manage user roles, delete users, moderate workouts, inspect JVM telemetry |

---

## 8. Java Concepts Demonstrated

1. **Object-Oriented Programming (OOP)**:
   * **Inheritance & Abstraction**: Base abstract class `User` extended by `Member`, `Trainer`, and `Admin`. Base abstract class `Workout` extended by `CardioWorkout`, `StrengthWorkout`, and `YogaWorkout`.
   * **Interfaces & Polymorphism**: `CalorieCalculable` interface implemented by `Workout`, enabling polymorphic calorie estimation based on exercise type (Cardio base MET, Strength 10% EPOC bonus, Yoga 0.90 factor).
   * **Encapsulation**: Private instance variables across all model classes with typed getters and setters.
2. **Collections & Generics**:
   * `BaseDAO<T, ID>` generic interface for type-safe data access.
   * `List<Workout>`, `Set<Integer>` for O(1) enrollment membership checks, `TreeMap<String, Double>` for alphabetical calorie aggregation.
   * Java 8 Stream API (`filter`, `map`, `collect`, `groupingBy`, `summingDouble`).
3. **Multithreading & Concurrency**:
   * `NotificationService`: Thread-safe singleton using `Executors.newFixedThreadPool(2)` to dispatch notifications asynchronously without blocking HTTP requests. Synchronized counter prevents race conditions.
   * `FitProAdminGUI`: Employs `SwingWorker<List<EnrollmentDetail>, Void>` to execute database queries on a background thread while keeping the Swing Event Dispatch Thread (EDT) responsive.
4. **Web & Controller Architecture**:
   * Java EE Servlets 4.0 (`@WebServlet`) handling GET/POST requests.
   * `AuthFilter` (`@WebFilter("/app/*")`) intercepting protected routes.
   * `AppContextListener` (`@WebListener`) gracefully terminating background thread pools upon container shutdown.
   * Post/Redirect/Get (PRG) pattern applied to all state-changing POST endpoints (`/app/workouts`, `/app/progress`, `/app/admin`) to eliminate duplicate submissions on browser refresh.
5. **Database Security & Reliability**:
   * Parameterized `PreparedStatement` used across 100% of SQL statements to prevent SQL injection.
   * Salted PBKDF2-HMAC-SHA256 password hashing with 65,536 iterations.
   * Server-side role authorization preventing horizontal and vertical privilege escalation.
   * Try-with-resources blocks ensuring proper closure of `Connection`, `PreparedStatement`, and `ResultSet` objects.

---

## 9. Innovation Features Implemented

1. **Session Progress CSV Data Export** (`ProgressServlet?action=export`):
   Streams session logs into a downloadable standard CSV spreadsheet file (`fitpro_progress_report.csv`) utilizing Java I/O (`PrintWriter`) and Servlet streaming response headers (`Content-Disposition: attachment`).
2. **Multi-Criteria Workout Search & Filtering** (`WorkoutServlet` & `WorkoutDAO.search`):
   Dynamic filtering supporting title/description keywords, discipline type (Cardio, Strength, Yoga), and difficulty tier (Beginner, Intermediate, Advanced) using safe SQL parameterized queries.
3. **Interactive Client-side BMI & Caloric Target Tool**:
   Client-side calculator computing Body Mass Index and daily active energy expenditure targets.
4. **Desktop Admin GUI Multithreading**:
   `SwingWorker` implementation ensuring non-blocking UI behavior during heavy database lookups.

---

## 10. College Marking Rubrics Alignment

### A. Java GUI Based Projects Marking Rubric (17 Marks Total)
1. **Servlet Implementation (10 Marks)**:
   * 7 dedicated Servlets with explicit `@WebServlet` mappings.
   * Clear separation between GET (retrieval) and POST (state mutation).
   * Post/Redirect/Get (PRG) pattern prevents duplicate submissions.
   * Server-side parameter validation and session authentication checks on protected paths.
   * Note: The desktop Swing GUI directly interacts with the JDBC DAO layer, while the web application implements the complete Servlet requirements.
2. **Code Quality & Execution (5 Marks)**:
   * Clean package organization (`dao`, `model`, `service`, `servlet`, `util`, `gui`, `filter`).
   * Try-with-resources across all database calls.
   * Layered architecture with no SQL in JSPs or controllers.
   * Working automated test suite (39 passing JUnit 5 tests).
3. **Innovation / Extra Effort (2 Marks)**:
   * Desktop Swing GUI multithreading via `SwingWorker`.
   * CSV report export and dynamic workout search.

### B. Java Web Based Projects Marking Rubric (17 Marks Total)
1. **Code Quality & Testing (10 Marks)**:
   * Strict adherence to OOP principles (Abstraction, Polymorphism, Inheritance, Encapsulation).
   * Java Collections, Generics, and Streams.
   * 39 automated unit and integration tests covering models, services, DAOs, and multithreading.
2. **Teamwork & Collaboration (5 Marks)**:
   * Clear, structured Git commit history.
   * Comprehensive `docs/DEVELOPMENT.md` guide and clean `.gitignore`.
   * Documented as an individual student project engineered to professional collaboration standards.
3. **Innovation / Extra Effort (2 Marks)**:
   * Dual-engine H2/MySQL database architecture with automatic schema creation.
   * Real-time progress CSV streaming and client-side BMI estimation tool.

---

## 11. Known Limitations

* Email notifications in `NotificationService` simulate network transmission via thread sleeps rather than connecting to an active SMTP mail server.
* Workout-specific calorie multipliers (1.10x EPOC for Strength, 0.90x for Yoga) are simplified educational estimates rather than clinical medical telemetry.
* The static showcase on Vercel serves the presentation and architectural preview (`index.html`); the functional Java WAR backend requires a Java/Tomcat runtime environment.

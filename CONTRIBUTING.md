# Team Collaboration & Contributing Guide

Welcome to the **FitPro** engineering repository. This document outlines our collaborative software development lifecycle, 3-member team responsibility matrix, Git workflow, coding standards, and quality assurance gates to satisfy the **Teamwork & Collaboration** requirements.

---

## 👥 3-Member Team Roles & Responsibilities Matrix

Our team consists of 3 members collaborating across the full stack with clearly defined technical ownership:

| Team Member | Roll Number | Role | Responsibilities & Core Deliverables |
|---|---|---|---|
| **Aditya Singh** *(Team Lead)* | `25scse1180171` | **Full-Stack & MVC Architecture Lead** | • Designed layered MVC architecture and request routing.<br>• Implemented 7 HTTP Servlets (`LoginServlet`, `RegisterServlet`, `DashboardServlet`, `WorkoutServlet`, `ProgressServlet`, `LogoutServlet`, `AdminServlet`).<br>• Engineered Security & Authentication Filter ([`AuthFilter.java`](src/main/java/com/fitpro/filter/AuthFilter.java)), session fixation defense, and PBKDF2 hashing.<br>• Built CSV Progress Report Exporter with servlet streaming.<br>• Repository management, CI/CD coordination, and Maven packaging. |
| **Manik** | `25scse1180216` | **Database & Persistence Specialist** | • Designed normalized relational SQL schema ([`schema.sql`](database/schema.sql)) with constraints and cascades.<br>• Engineered zero-config **Dual-Engine JDBC** ([`DBConnection.java`](src/main/java/com/fitpro/util/DBConnection.java)) with automatic MySQL 8 + embedded H2 fallback.<br>• Implemented entire DAO tier ([`UserDAO.java`](src/main/java/com/fitpro/dao/UserDAO.java), [`WorkoutDAO.java`](src/main/java/com/fitpro/dao/WorkoutDAO.java), [`EnrollmentDAO.java`](src/main/java/com/fitpro/dao/EnrollmentDAO.java), [`ProgressDAO.java`](src/main/java/com/fitpro/dao/ProgressDAO.java)).<br>• Structured custom checked exception handling (`AppException.java`). |
| **Harshit Vats** | `25scse1180097` | **Frontend UX, Desktop GUI & QA Engineer** | • Created responsive dark-accent design system ([`style.css`](src/main/webapp/css/style.css)) and JSP/JSTL dynamic views.<br>• Implemented client-side interactivity ([`app.js`](src/main/webapp/js/app.js)): instant catalog search, interactive BMI calculator, dynamic progress bar animation.<br>• Developed standalone **Java Swing Desktop GUI** ([`FitProAdminGUI.java`](src/main/java/com/fitpro/gui/FitProAdminGUI.java)) with `SwingWorker` multithreading.<br>• Authored comprehensive **32 JUnit 5 Automated Tests** across 8 test suites with 100% pass rate. |

---

## 🌿 Git Branching Strategy

Our team utilizes a **GitFlow-inspired feature branching workflow**:

1. **`main`**: Production-ready, stable codebase. Direct unreviewed commits to `main` are restricted.
2. **`develop`**: Integration branch for current iteration.
3. **`feature/<feature-name>`**: Dedicated branches for individual member tasks:
   - `feature/aditya-servlets-auth`: Core Servlets, AuthFilter, and CSV export.
   - `feature/manik-database-daos`: Relational schema, dual-engine JDBC, and DAOs.
   - `feature/harshit-frontend-gui-tests`: JSP views, CSS styling, Swing GUI, and JUnit 5 test suite.
4. **`bugfix/<issue-name>`**: Targeted fixes for verified issues.

### Branch Lifecycle Workflow:
```text
(develop) ───> [ git checkout -b feature/member-task ]
                      │
                      ▼
               Work, commit, test locally
                      │
                      ▼
               [ git push origin feature/member-task ]
                      │
                      ▼
               Open Pull Request -> Peer Code Review -> Merge to develop
```

---

## 📋 Pull Request & Code Review Process

Before any code is merged into `develop` or `main`:

1. **Automated Test Gate**: All 32 JUnit 5 test cases must pass cleanly (`mvn test`).
2. **Peer Review**: At least one other team member must review and approve the pull request.
3. **Documentation Check**: Any new public methods or controllers must have descriptive JavaDoc comments.
4. **Security Check**: All SQL executed via `PreparedStatement`; all JSP variables escaped with `<c:out>`.

---

## 📐 Coding Standards & Conventions

- **Java Version**: Compatible with Java 11 through Java 21+.
- **Naming Conventions**:
  - Classes & Interfaces: `UpperCamelCase` (e.g., `WorkoutService`, `CalorieCalculable`).
  - Methods & Variables: `lowerCamelCase` (e.g., `caloriesBurned`, `latestWeight`).
  - Constants: `UPPER_SNAKE_CASE` (e.g., `ITERATIONS`, `KEY_LENGTH`).
- **Resource Management**: Always use `try-with-resources` for `Connection`, `PreparedStatement`, `ResultSet`, and I/O streams.
- **Security**: Never store plain-text passwords; all passwords route through `PasswordUtil.hash()`.
- **View Escaping**: All dynamic variables in JSP must be escaped using `<c:out value="${...}"/>` to prevent Cross-Site Scripting (XSS).

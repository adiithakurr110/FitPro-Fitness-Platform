# Team Collaboration & Contributing Guide

Welcome to the **FitPro** engineering repository. This document outlines our collaborative software development lifecycle, Git workflow, coding standards, and quality assurance gates to support effective teamwork across our development group.

---

## 👥 Team Roles & Responsibilities Matrix

To ensure clear ownership and collaborative execution across the stack, the project responsibilities are distributed as follows:

| Role | Responsibilities | Core Deliverables |
|---|---|---|
| **Backend & Architecture Lead** | Layered MVC architecture, HTTP Servlets, Filters, Listeners | `*Servlet.java`, `AuthFilter.java`, `AppContextListener.java` |
| **Database & JDBC Engineer** | Relational schema design, PreparedStatement JDBC DAOs, dual-engine fallback | `schema.sql`, `DBConnection.java`, `*DAO.java` |
| **Frontend & UX Specialist** | JSP view design, JSTL rendering, responsive CSS design system, client-side JS | `*.jsp`, `*.jspf`, `style.css`, `app.js` |
| **Desktop GUI Engineer** | Java Swing administration application, asynchronous UI background threads | `FitProAdminGUI.java`, `SwingWorker` multithreading |
| **QA & Security Engineer** | JUnit 5 test suite, PBKDF2 password hashing, XSS/SQLi defense, input validation | 32 Automated Tests, `PasswordUtil.java`, `AppException.java` |

---

## 🌿 Git Branching Strategy

Our team utilizes a **GitFlow-inspired feature branching workflow**:

1. **`main`**: Production-ready, stable codebase. Direct commits to `main` are restricted.
2. **`develop`**: Integration branch for current iteration.
3. **`feature/<feature-name>`**: Dedicated feature branches branched from `develop` (e.g., `feature/csv-export`, `feature/swing-gui`).
4. **`bugfix/<issue-name>`**: Targeted fixes for verified bugs.

### Branch Lifecycle Workflow:
```text
(develop) ───> [ git checkout -b feature/awesome-feature ]
                      │
                      ▼
               Work, commit, test
                      │
                      ▼
               [ git push origin feature/awesome-feature ]
                      │
                      ▼
               Open Pull Request -> Code Review -> Merge to develop
```

---

## 📋 Pull Request & Code Review Process

Before any code is merged into `develop` or `main`:

1. **Automated Test Gate**: All 32 JUnit 5 test cases must pass cleanly (`mvn test`).
2. **Peer Review**: At least one peer developer must review and approve the pull request.
3. **Documentation Check**: Any new public methods or controllers must have descriptive JavaDoc comments.
4. **No Hardcoded Secrets**: Ensure database credentials use environment variables or `db.properties` templates.

---

## 📐 Coding Standards & Conventions

- **Java Version**: Compatible with Java 11 through Java 21+.
- **Naming Conventions**:
  - Classes & Interfaces: `UpperCamelCase` (e.g., `WorkoutService`, `CalorieCalculable`).
  - Methods & Variables: `lowerCamelCase` (e.g., `caloriesBurned`, `latestWeight`).
  - Constants: `UPPER_SNAKE_CASE` (e.g., `ITERATIONS`, `KEY_LENGTH`).
- **Resource Management**: Always use `try-with-resources` for `Connection`, `PreparedStatement`, `ResultSet`, and I/O streams.
- **Security**: Never store plain-text passwords; all passwords must route through `PasswordUtil.hash()`.
- **View Escaping**: All dynamic variables in JSP must be escaped using `<c:out value="${...}"/>` to mitigate Cross-Site Scripting (XSS).

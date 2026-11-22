# FitPro — Development & Architecture Guide

This document provides technical guidelines, architecture references, and setup procedures for the **FitPro** platform. It is designed to assist evaluators, examiners, and future maintainers in understanding, building, running, and testing the system.

---

## 1. Project Overview & Architecture

FitPro is built following the classic **Model-View-Controller (MVC)** architectural pattern using core Java technologies without external heavy enterprise frameworks (such as Spring or Hibernate), maximizing pure Java understanding for academic evaluation:

```
[ Browser / Client ]              [ Desktop Admin Client ]
        │                                    │
        ▼ (HTTP / HTML / Forms)               ▼ (Swing Event Dispatch Thread)
[ Java Servlets (Controller) ]       [ FitProAdminGUI (View + Controller) ]
   • LoginServlet                       • SwingWorker (Multithreading)
   • RegisterServlet                    • JTable, DefaultTableModel
   • WorkoutServlet                     • ActionListeners
   • ProgressServlet                         │
   • AdminServlet                            │
   • DashboardServlet                        │
        │                                    │
        ▼                                    ▼
[ Service Layer (Business Logic) ] ◄─────────┘
   • AuthService (PBKDF2 Password Hashing, RBAC verification)
   • WorkoutService (MET Calculations, Publishing & Deletion Rules)
   • ProgressService (Calorie Aggregations, Java 8 Streams, CSV Generation)
   • NotificationService (Multithreading via ExecutorService pool)
        │
        ▼
[ Data Access Objects (DAO / JDBC) ]
   • UserDAO (implements BaseDAO<User, Integer>)
   • WorkoutDAO (implements BaseDAO<Workout, Integer>)
   • EnrollmentDAO
   • ProgressDAO
        │
        ▼
[ Relational Database ]
   • Embedded H2 Database (Default zero-configuration at ./data/fitpro_db)
   • MySQL Database 8.x (Optional production mode)
```

---

## 2. Directory Structure

```
fitpro/
├── pom.xml                               # Maven project configuration (WAR packaging)
├── README.md                             # Comprehensive project documentation
├── index.html                            # Minimal Apple-inspired static showcase
├── vercel.json                           # Static hosting configuration for Vercel
├── run-app.bat                           # 1-click batch launcher for Tomcat 9 web app
├── run-gui.bat                           # 1-click batch launcher for Desktop Swing GUI
├── docs/
│   ├── DEVELOPMENT.md                    # Developer and architecture guide
│   ├── FitPro_Presentation_v3.pdf        # Official project slide deck (PDF)
│   └── FitPro_Presentation_v3.pptx       # Official project slide deck (PowerPoint)
└── src/
    ├── main/
    │   ├── java/com/fitpro/
    │   │   ├── dao/                      # Data Access Objects (JDBC abstraction)
    │   │   │   ├── BaseDAO.java          # Generic DAO interface (Generics)
    │   │   │   ├── UserDAO.java
    │   │   │   ├── WorkoutDAO.java
    │   │   │   ├── EnrollmentDAO.java
    │   │   │   └── ProgressDAO.java
    │   │   ├── exception/
    │   │   │   └── AppException.java     # Custom checked exception hierarchy
    │   │   ├── filter/
    │   │   │   └── AuthFilter.java       # Intercepts /app/* routes for session validity
    │   │   ├── gui/
    │   │   │   └── FitProAdminGUI.java   # Desktop Swing GUI (Multithreading with SwingWorker)
    │   │   ├── listener/
    │   │   │   └── AppContextListener.java# ServletContext lifecycle listener (clean shutdown)
    │   │   ├── model/                    # Object-Oriented Domain Entities
    │   │   │   ├── User.java             # Abstract base class
    │   │   │   ├── Member.java           # Concrete subclass
    │   │   │   ├── Trainer.java          # Concrete subclass
    │   │   │   ├── Admin.java            # Concrete subclass
    │   │   │   ├── CalorieCalculable.java# Core interface for calorie calculations
    │   │   │   ├── Workout.java          # Abstract class implementing CalorieCalculable
    │   │   │   ├── CardioWorkout.java    # Concrete subclass (Standard MET)
    │   │   │   ├── StrengthWorkout.java  # Concrete subclass (10% EPOC bonus)
    │   │   │   ├── YogaWorkout.java      # Concrete subclass (0.90 intensity factor)
    │   │   │   ├── ProgressLog.java
    │   │   │   └── EnrollmentDetail.java
    │   │   ├── service/                  # Business Logic Layer
    │   │   │   ├── AuthService.java
    │   │   │   ├── WorkoutService.java
    │   │   │   ├── ProgressService.java
    │   │   │   └── NotificationService.java # Multithreaded async notifier
    │   │   ├── servlet/                  # Web Controllers (Servlets 4.0)
    │   │   │   ├── LoginServlet.java
    │   │   │   ├── RegisterServlet.java
    │   │   │   ├── LogoutServlet.java
    │   │   │   ├── DashboardServlet.java
    │   │   │   ├── WorkoutServlet.java
    │   │   │   ├── ProgressServlet.java
    │   │   │   └── AdminServlet.java
    │   │   └── util/
    │   │       ├── DBConnection.java     # JDBC connection manager & schema initializer
    │   │       └── PasswordUtil.java     # Salted PBKDF2-HMAC-SHA256 hasher
    │   ├── resources/
    │   │   ├── db.properties             # External database properties
    │   │   └── schema.sql                # Non-destructive relational DDL
    │   └── webapp/
    │       ├── index.jsp                 # Apple-inspired landing page
    │       ├── css/style.css             # Apple-inspired design system CSS
    │       ├── js/app.js                 # Minimal client-side interactivity
    │       └── WEB-INF/
    │           ├── web.xml               # Deployment descriptor (HTTP-only cookies, 404/500 mapping)
    │           └── views/
    │               ├── header.jspf       # Reusable header fragment with alerts
    │               ├── footer.jspf       # Reusable footer fragment
    │               ├── login.jsp
    │               ├── register.jsp
    │               ├── dashboard.jsp     # Adaptive dashboard (Member, Trainer, Admin)
    │               ├── workouts.jsp      # Searchable workout catalogue
    │               ├── progress.jsp      # Session logger and CSV exporter
    │               ├── admin.jsp         # Administrator control center
    │               └── error.jsp         # User-friendly error view
    └── test/java/com/fitpro/             # Automated test suite (JUnit 5)
        ├── dao/DAOTest.java
        ├── model/UserModelTest.java
        ├── model/WorkoutModelTest.java
        ├── service/AuthServiceTest.java
        ├── service/ProgressServiceTest.java
        ├── service/WorkoutServiceTest.java
        └── util/
            ├── DBConnectionTest.java
            └── PasswordUtilTest.java
```

---

## 3. Environment & Prerequisites

* **Java Development Kit (JDK)**: Version 11 or higher (Tested on OpenJDK 11, 17, and 21).
* **Apache Maven**: Version 3.6 or higher.
* **Operating System**: Platform-independent (Windows, macOS, Linux).

---

## 4. Database Setup & Switching

FitPro uses a dual-engine architecture managed in [src/main/resources/db.properties](file:///d:/fitpro/fitpro/src/main/resources/db.properties):

### Mode A: Embedded H2 Database (Default)
* Requires zero installation or external service startup.
* Created automatically on first run in `./data/fitpro_db.mv.db`.
* Fully persistent across application restarts.
* Seeded with initial demonstration accounts and workouts if empty.

### Mode B: External MySQL Server
To use a local or remote MySQL 8.x database:
1. Create the MySQL database:
   ```sql
   CREATE DATABASE fitpro CHARACTER SET utf8mb4;
   ```
2. Edit `src/main/resources/db.properties`:
   ```properties
   db.type=mysql
   db.mysql.url=jdbc:mysql://localhost:3306/fitpro?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.mysql.user=your_username
   db.mysql.password=your_password
   ```
   *Alternatively, pass environment variables `DB_TYPE=mysql`, `DB_URL`, `DB_USER`, and `DB_PASSWORD`.*

---

## 5. Build, Run, and Test Procedures

### A. Run Automated Unit & Integration Tests
```bash
mvn clean test
```
*Executes all 39 JUnit 5 test cases covering models, calculations, DAOs, services, and multithreading.*

### B. Package the Web Application (WAR)
```bash
mvn clean package
```
*Generates `target/fitpro.war` ready for deployment on any standard Tomcat 9/10 container.*

### C. Run the Web Application Locally (Tomcat 9 via Cargo)
```bash
mvn package cargo:run
```
*Automatically downloads and launches Apache Tomcat 9 on port 8080.*
*Web application URL:* **`http://localhost:8080/fitpro/`**

### D. Run the Desktop Administration Swing GUI
```bash
mvn compile exec:java
```
*Launches the Swing desktop interface connecting directly to the database via JDBC DAOs.*

---

## 6. Pre-Configured Demonstration Accounts

| Role | Email | Password | Allowed Capabilities |
|---|---|---|---|
| **Member** | `member@fitpro.com` | `member123` | Enrolls in programs, logs sessions, tracks calories, computes BMI, exports CSV |
| **Trainer** | `trainer@fitpro.com` | `trainer123` | Publishes routines, deletes own routines, inspects enrolled member subscriptions |
| **Administrator** | `admin@fitpro.com` | `admin123` | Reassigns user roles, moderates workout catalogue, monitors JVM heap & CPU telemetry |

---

## 7. Individual Project & Collaboration Readiness

This project was developed as an individual B.Tech CSE capstone project. To ensure high code quality, readability, and evaluation standards:
* **Standard Git Workflow**: Commits are atomic and structured with clear semantic messages.
* **Separation of Concerns**: Controllers (Servlets), Views (JSPs), Business Services, and Persistence (DAOs) are strictly separated.
* **Security Hardening**: Passwords hashed with PBKDF2; server-side role validation on every action; horizontal privilege escalation prevented; state changes protected using Post/Redirect/Get.

# FitPro - Online Fitness Training Platform

A full-stack **Java Enterprise Web & Desktop Application** where trainers publish workout programs and members enroll, log training sessions, and track calories burned.

Built with **Java 11, Servlets 4.0, JSP, JSTL, JDBC, MySQL 8 & Embedded H2** using a clean layered Model-View-Controller (MVC) architecture.

---

## 🌟 Key Features

| For Members | For Trainers & Admins |
|---|---|
| Register / login securely (PBKDF2 Password Hashing) | Register / login securely as Trainer / Admin |
| Browse & filter programs (Cardio, Strength, Yoga) | Publish new workout routines with custom intensity (MET) |
| **One-Click Enrollment** in fitness programs | Access dedicated **Admin Portal** (`/app/admin`) |
| **Interactive Dashboard** with active enrolled workouts | View the **Enrolled Members Directory** with subscriber details |
| Log sessions (Minutes trained + body weight in kg) | View full system user directory & platform metrics |
| Automatic calorie estimate per workout type (MET formula) | **Desktop Administration GUI** (`FitProAdminGUI.java`) |
| Real-time analytics & calories-by-category progress chart | Asynchronous background notifications (`NotificationService`) |

---

## 💻 Tech Stack & Languages Used

| Layer | Language / Technology | Details |
|---|---|---|
| **Backend Core** | Java 11+ (JDK 26 compatible) | OOP Architecture, Servlets 4.0, Multithreading (`ExecutorService`), Collections, Streams |
| **Web Presentation** | JSP 2.3 + JSTL 1.2 | Dynamic server-side rendering, XSS prevention via `<c:out>`, custom fragments |
| **Desktop GUI** | Java Swing / AWT | Desktop Admin GUI (`FitProAdminGUI`) with `JFrame`, `JTable`, and event handling |
| **Frontend UI** | HTML5, CSS3, Vanilla JS | Modern responsive design system, Flexbox, CSS Grid, FontAwesome 6 icons |
| **Database** | SQL (MySQL 8 & H2) | Relational schema with Foreign Keys, Cascades, and `PreparedStatement` JDBC execution |
| **Build & Container**| Maven 3.9+ & Apache Tomcat 9 | Automated packaging (`fitpro.war`) and embedded Cargo container runner |

---

## 🚀 How to Setup and Run

### 1. Requirements
- **JDK 11 or newer** (`java -version`)
- **Apache Maven 3.6+** (`mvn -version`)

---

### 2. Run the Web Application (One Command)
Tomcat 9 downloads and runs automatically:
```bash
mvn package cargo:run
```
Open your browser and navigate to:
```
http://localhost:8080/fitpro/
```

* **Registration**: `http://localhost:8080/fitpro/register`
* **Member Dashboard**: `http://localhost:8080/fitpro/app/dashboard`
* **Admin & Trainer Portal**: `http://localhost:8080/fitpro/app/admin`

---

### 3. Run the Desktop Admin GUI
You can also launch the Swing-based Desktop Administration window:
```bash
java -cp "target/classes;target/fitpro/WEB-INF/lib/*" com.fitpro.gui.FitProAdminGUI
```

---

### 4. Database Configuration (Dual-Engine)
The app runs **out-of-the-box with zero configuration** using the embedded **H2 Database** (`./data/fitpro_db.mv.db`), automatically populating all tables and sample programs on startup.


---

## 📂 Project Structure

```
fitpro/
├── pom.xml                                 Maven project configuration & dependencies
├── README.md                               Project documentation and rubric mapping
├── git.cmd                                 Git wrapper script for version control
├── docs/
│   ├── FitPro_Comprehensive_Presentation.pptx  Complete 10-slide PowerPoint presentation
│   └── FitPro_Presentation.pptx                Original presentation slide deck
├── database/
│   └── schema.sql                          Relational SQL schema & sample workout data
└── src/
    ├── main/
    │   ├── java/com/fitpro/
    │   │   ├── model/                      Domain Entities (User > Member/Trainer, Workout > Cardio/Strength/Yoga)
    │   │   │   ├── CalorieCalculable.java  Interface for polymorphic calorie calculation
    │   │   │   └── EnrollmentDetail.java   DTO for Admin Portal reporting
    │   │   ├── dao/                        Data Access Objects (BaseDAO<T,ID>, UserDAO, WorkoutDAO, EnrollmentDAO, ProgressDAO)
    │   │   ├── service/                    Business Logic (AuthService, WorkoutService, ProgressService, NotificationService)
    │   │   ├── servlet/                    Controllers (Login, Register, Dashboard, Workout, Progress, Admin, Logout)
    │   │   ├── filter/                     AuthFilter - guards /app/* routes
    │   │   ├── listener/                   AppContextListener - database init & thread pool shutdown
    │   │   ├── exception/                  AppException - custom checked exception
    │   │   ├── gui/                        FitProAdminGUI - Java Swing desktop administration app
    │   │   └── util/                       DBConnection (JDBC auto-fallback), PasswordUtil (PBKDF2)
    │   ├── resources/
    │   │   ├── db.properties               JDBC connection properties
    │   │   └── schema.sql                  Packaged classpath SQL schema
    │   └── webapp/
    │       ├── css/style.css               Custom modern responsive styling
    │       ├── js/app.js                   Client-side validation
    │       └── WEB-INF/
    │           ├── web.xml                 Deployment descriptor
    │           └── views/                  JSP views (dashboard, workouts, admin, progress, login, register)
    └── test/java/com/fitpro/util/
        └── DBConnectionTest.java           JUnit 5 automated tests for JDBC & schema
```

---



---

## 🔒 Security Features
- **Password Security**: Passwords are salted and hashed using **PBKDF2-HMAC-SHA256** (never stored in plain text).
- **SQL Injection Defense**: 100% of queries use parameterized `PreparedStatement`.
- **XSS Prevention**: All dynamic JSP outputs are escaped with JSTL `<c:out>`.
- **Session Protection**: Sessions regenerate on login, and cookies are flagged `HttpOnly`.

---




# FitPro - Online Fitness Training Platform

A full-stack **Java web application** where trainers publish workout programs and members enrol, log training sessions and track calories burned.

Built with **Servlets + JSP + JDBC + MySQL** using a clean layered (MVC) architecture.

---

## Features

| For Members | For Trainers |
|---|---|
| Register / login securely | Register / login securely |
| Browse and filter workouts (Cardio, Strength, Yoga) | Publish new workout programs |
| Enrol in programs | See the shared program catalogue |
| Log sessions (minutes + body weight) | Role-specific dashboard |
| Automatic calorie estimate per workout type | |
| Dashboard with totals and a calories-by-workout chart | |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | JSP, JSTL, HTML5, CSS3, vanilla JavaScript |
| Backend | Java 11, Servlets 4.0, Filters, Listeners |
| Database | MySQL 8 via JDBC (`PreparedStatement`) |
| Build | Maven (WAR packaging) |
| Server | Apache Tomcat 9 |

---

## Requirements

Install these first:

1. **JDK 11 or newer** (`java -version`)
2. **Apache Maven 3.6+** (`mvn -version`)
3. **MySQL 8** running locally
4. *(Optional)* Apache Tomcat 9 - only if you do not want to use the bundled `cargo` runner

> Use **Tomcat 9** (package `javax.servlet`). Tomcat 10+ uses `jakarta.*` and will not run this project unchanged.

---

## Setup and Run

### 1. Clone
```bash
git clone https://github.com/<your-username>/fitpro.git
cd fitpro
```

### 2. Create the database
```bash
mysql -u root -p < database/schema.sql
```
This creates the `fitpro` database, all tables and six sample workout programs.

### 3. Configure the database connection
Open `src/main/resources/db.properties` and set your MySQL username and password:
```properties
db.url=jdbc:mysql://localhost:3306/fitpro?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=your_password
```
Alternatively set the environment variables `DB_URL`, `DB_USER`, `DB_PASSWORD` (they override the file).

### 4. Build and run

**Option A - one command (downloads Tomcat 9 automatically):**
```bash
mvn package cargo:run
```

**Option B - your own Tomcat 9:**
```bash
mvn clean package
```
Copy `target/fitpro.war` into Tomcat's `webapps/` folder and start Tomcat.
(In IntelliJ / Eclipse you can also add Tomcat 9 as a run configuration and deploy the WAR.)

### 5. Open the app
```
http://localhost:8080/fitpro/
```

### 6. Try it out
1. Register a **Trainer** account and publish a program on the *Workouts* page.
2. Log out, register a **Member** account.
3. Enrol in a program, then open *My Progress* and log a session.
4. Check the *Dashboard* for your stats and chart.

---

## Project Structure

```
fitpro/
├── pom.xml                         Maven build (WAR)
├── README.md
├── docs/FitPro_Presentation.pptx   Project presentation
├── database/
│   └── schema.sql                  Tables + sample data
└── src/main/
    ├── java/com/fitpro/
    │   ├── model/                  Entities: User > Member/Trainer, Workout > Cardio/Strength/Yoga, ProgressLog
    │   ├── dao/                    JDBC data access (BaseDAO<T,ID>, UserDAO, WorkoutDAO, EnrollmentDAO, ProgressDAO)
    │   ├── service/                Business logic (Auth, Workout, Progress, Notification)
    │   ├── servlet/                Controllers (Login, Register, Dashboard, Workout, Progress, Logout)
    │   ├── filter/                 AuthFilter - protects /app/*
    │   ├── listener/               AppContextListener - clean thread shutdown
    │   ├── exception/              AppException
    │   └── util/                   DBConnection, PasswordUtil
    ├── resources/db.properties     JDBC settings
    └── webapp/
        ├── css/style.css
        ├── js/app.js
        └── WEB-INF/
            ├── web.xml
            └── views/              JSP pages (login, register, dashboard, workouts, progress, error)
```

### Architecture (MVC, layered)

```
Browser -> Servlet (controller) -> Service (business rules) -> DAO (JDBC) -> MySQL
              |
              +--> JSP view (JSTL) renders the response
```

---

## Marking Rubric Mapping

| Rubric item | Where it is implemented |
|---|---|
| **OOP** - inheritance, polymorphism, abstraction, interfaces | `User` -> `Member`/`Trainer`; `Workout` -> `CardioWorkout`/`StrengthWorkout`/`YogaWorkout`; interface `CalorieCalculable` |
| **Exception handling** | Custom checked `AppException`; every DAO wraps `SQLException`; servlets show friendly errors |
| **Collections and generics** | Generic `BaseDAO<T, ID>`; `List`, `Set`, `Map`/`TreeMap`, `Optional`, streams in `ProgressService` |
| **Multithreading and synchronization** | `NotificationService`: thread pool (`ExecutorService`) + `synchronized` shared counter |
| **Database classes** | `UserDAO`, `WorkoutDAO`, `EnrollmentDAO`, `ProgressDAO` |
| **JDBC connectivity** | `DBConnection` (driver loading, properties, env overrides) + `PreparedStatement` + try-with-resources |
| **Servlets and web integration** | 6 servlets, `AuthFilter`, `ServletContextListener`, sessions, JSP + JSTL views |
| **Problem understanding and design** | Layered architecture, role-based features, documented in the presentation |

---

## Security Notes

- Passwords are hashed with **PBKDF2-HMAC-SHA256** and a random salt (never stored in plain text).
- All SQL uses **PreparedStatement** (prevents SQL injection).
- JSP output is escaped with `<c:out>` (prevents XSS).
- Sessions are regenerated at login and cookies are `HttpOnly`.
- Role checks are enforced on the server, not just hidden in the UI.

## Known Limitations / Future Work

- No CSRF tokens yet (add a per-session token to forms).
- Trainer role is chosen at sign-up; a real system would use admin approval.
- Possible extensions: video lessons, trainer-member chat, weekly goals, charts with Chart.js, REST API.

## Author

Your Name - Roll No. - Course / Institute

# K-Learn — Korean Language Learning Platform

## 📋 Project Overview

K-Learn is a modern web-based platform for learning Korean language with comprehensive 4-skill training (Listening, Speaking, Reading, Writing). Built with Spring Boot 3.2.5 and Thymeleaf.

**Key Features:**
- 📚 Structured course system (4 levels: Beginner I-II, Intermediate I-II)
- 🎮 Gamification (XP, Badges, Streaks)
- 🗣️ Speaking practice with pronunciation scoring
- 📱 Responsive design with dark mode
- 🏫 Community study rooms (WebSocket)
- 🏆 Leaderboard system

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Spring Boot 3.2.5, Spring Data JPA, Spring Security |
| **Frontend** | Thymeleaf, HTML5, CSS3, Vanilla JavaScript |
| **Database** | MySQL 8.0+ (UTF-8mb4) |
| **Build** | Maven 3 |
| **Testing** | JUnit 5, Mockito |
| **Java Version** | Java 17+ |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd project-k-learn-se2a
   ```

2. **Configure MySQL Database**
   ```bash
   mysql -u root -p
   CREATE DATABASE klearn CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Update Database Credentials**
   Edit `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       username: root
       password: your_password
   ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

   The app will start at: `http://localhost:8088`

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/klearn/
│   │   ├── config/           (Configuration classes)
│   │   ├── controller/       (REST & MVC controllers)
│   │   ├── dto/              (Data Transfer Objects)
│   │   ├── exception/        (Custom exceptions)
│   │   ├── model/            (JPA entities)
│   │   ├── repository/       (Data access layer)
│   │   ├── security/         (Authentication & Authorization)
│   │   └── service/          (Business logic)
│   └── resources/
│       ├── application.yml   (Main configuration)
│       ├── application-{dev,test,prod}.yml
│       ├── schema.sql        (Database initialization)
│       ├── logback-spring.xml (Logging config)
│       ├── templates/        (Thymeleaf templates)
│       └── static/           (CSS, JS, images)
└── test/
    └── java/com/klearn/     (Unit tests)
```

---

## 📖 Configuration

### Application Profiles

- **dev**: Development (ddl-auto: update, logging: DEBUG)
- **test**: Testing (ddl-auto: create-drop, test database)
- **prod**: Production (ddl-auto: validate, HTTPS enabled)

Run with specific profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Environment Variables

For production:
```bash
export SPRING_DATASOURCE_USERNAME=klearn_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export SSL_KEYSTORE_PASSWORD=keystore_password
```

---

## 🗄️ Database Schema

Key tables:
- **user**: User accounts with XP, level, streaks
- **course**: Learning courses
- **lesson**: Individual lessons
- **exercise**: Practice exercises (listening, reading, etc.)
- **user_progress**: Track user progress per lesson
- **user_badge**: Earned badges
- **lesson_result**: Results of completed lessons

---

## 🔒 Security

- **Session-based Authentication** via Spring Security
- **CSRF Protection** enabled
- **Secure Password Hashing** with BCrypt
- **Input Validation** with Bean Validation (@Valid)
- **SQL Injection Prevention** via parameterized queries

---

## ✅ Testing

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=CourseServiceTest
```

Current test coverage:
- ✅ XpServiceTest
- ✅ StreakServiceTest
- ✅ BadgeServiceTest
- ✅ CourseServiceTest

---

## 🐛 Error Handling

All errors are handled globally via `GlobalExceptionHandler`:
- Validation errors (400)
- Resource not found (404)
- Internal server errors (500)

Error templates: `templates/error.html`, `templates/404.html`, `templates/500.html`

---

## 📝 Recent Updates (v0.0.1)

### ✅ Completed
1. ✅ Updated MySQL Connector (8.0.33 → 8.2.0) - Fixed CVE-2023-22102
2. ✅ Migrated `application.properties` → `application.yml` with profiles
3. ✅ Added environment-specific configurations (dev/test/prod)
4. ✅ Implemented Global Exception Handler with custom exceptions
5. ✅ Created error templates (404, 500, error)
6. ✅ Added request validation DTOs (RegisterRequest, LoginRequest)
7. ✅ Created comprehensive unit tests (Streak, Badge, Course services)
8. ✅ Added structured logging with Logback
9. ✅ Created KLearnProperties config class

### 🔄 In Progress
- Performance optimization (caching, indexing)
- REST API enhancement
- Frontend improvements

---

## 🚧 Known Issues & TODOs

- [ ] Speech recognition integration for speaking exercises
- [ ] Automated writing exercise evaluation
- [ ] Profile picture upload functionality
- [ ] Password reset via email
- [ ] User progress export/reports
- [ ] Mobile app version
- [ ] Docker containerization

---

## 📚 Documentation

### Key Services
- `CourseService`: Course and lesson management
- `XpService`: XP and level calculation
- `StreakService`: Daily streak tracking
- `BadgeService`: Badge earning logic
- `AuthService`: Authentication handling

### API Endpoints

**Public:**
- `GET /` - Home page
- `POST /auth/login` - Login
- `GET /auth/logout` - Logout

**Protected:**
- `GET /dashboard` - User dashboard
- `GET /courses` - List courses
- `GET /courses/{id}` - Course details
- `GET /courses/{id}/lessons` - Course lessons
- `GET /flashcards` - Flashcards
- `GET /quiz` - Quiz
- `GET /leaderboard` - Leaderboard
- `GET /profile` - User profile

---

## 🤝 Contributing

1. Create a feature branch
2. Make changes with tests
3. Ensure all tests pass
4. Submit pull request

---

## 📄 License

This project is part of SE2 course at [University Name]

---

## 👥 Team

- **Database Design**: [Team]
- **Backend Development**: [Team]
- **Frontend Development**: [Team]

---

## 📧 Contact & Support

For issues and questions:
- GitHub Issues: [Link to issues]
- Email: support@klearn.local

---

Last Updated: April 14, 2026

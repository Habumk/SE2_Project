# 🚀 K-LEARN QUICK START GUIDE
## Chạy dự án trong 5 phút

---

## ⚙️ **BƯỚC 1: Chuẩn Bị (2 phút)**

### **Yêu cầu**
- ✅ Java 17+ installed
- ✅ Maven 3.8+ installed  
- ✅ MySQL 8.0+ running
- ✅ Port 8088 available

### **Kiểm tra**
```bash
# Check Java
java -version
# Expected: openjdk 17+ or Oracle JDK 17+

# Check Maven
mvn -version
# Expected: Apache Maven 3.8+

# Check MySQL
mysql --version
# Expected: mysql Ver 8.0+
```

---

## 🗄️ **BƯỚC 2: Setup Database (1 phút)**

### **Tạo Database**
```bash
# Connect to MySQL
mysql -u root -p

# Inside MySQL:
CREATE DATABASE klearn CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

### **Verify Credentials**
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    username: root          # ← Your MySQL username
    password: 220405        # ← Your MySQL password
```

---

## 📦 **BƯỚC 3: Build & Run (2 phút)**

### **Option 1: Quick Run (Recommended)**
```bash
cd c:\Users\GIGABYTE\Downloads\New Project\project-k-learn-se2\SE2_K-Learning\project-k-learn-se2a

# Clean and build
mvn clean package -DskipTests

# Run
mvn spring-boot:run
```

**Expected Output:**
```
[INFO] Tomcat started on port(s): 8088 (http)
[INFO] Started KLearnApplication in X.XX seconds
```

### **Option 2: JAR Execution**
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/klearn-0.0.1-SNAPSHOT.jar
```

### **Option 3: IDE (IntelliJ IDEA)**
1. Open project in IntelliJ
2. Wait for Maven indexing to complete
3. Click "Run" → "Run 'KLearnApplication'"

---

## 🌐 **BƯỚC 4: Access Application (Now!)**

### **URL**
```
http://localhost:8088
```

### **Test Login Credentials**
```
Email: bvanh004@gmail.com
Password: (use the one from schema.sql, or register new)
```

### **First Steps**
1. ✅ Go to login page
2. ✅ Enter credentials
3. ✅ Should see Dashboard
4. ✅ Click "Khóa học" (Courses)
5. ✅ Select a course
6. ✅ Click a lesson
7. ✅ Try listening/reading/writing

---

## 🧪 **BƯỚC 5: Run Tests (Optional)**

```bash
# Run all tests
mvn test

# Expected: All 4 tests PASS
# [INFO] Tests run: N, Failures: 0, Errors: 0
```

---

## 🔍 **TROUBLESHOOTING**

### **Problem: Port 8088 already in use**
```bash
# Kill process on Windows
netstat -ano | findstr :8088
taskkill /PID <PID> /F

# Or change port in application.yml
server:
  port: 8089  # Change here
```

### **Problem: MySQL connection refused**
```bash
# Check MySQL is running
# Windows:
services.msc
# Look for MySQL service, restart if needed

# Verify credentials in application.yml
spring:
  datasource:
    username: root
    password: 220405  # Change if needed
```

### **Problem: Thymeleaf templates not found**
```bash
# Verify template files exist
ls src/main/resources/templates/
# Should show: layout.html, auth/, pages/, courses/, lessons/
```

### **Problem: Build fails**
```bash
# Clean cache and rebuild
mvn clean install -DskipTests

# Check Java version
java -version
# Must be 17 or higher
```

### **Problem: Compilation errors**
```
[ERROR] failed to execute goal 
        org.apache.maven.plugins:maven-compiler-plugin
```
**Solution:** Ensure Java 17 is set as project SDK in IDE

---

## 📊 **QUICK VALIDATION**

After starting, open browser and check:

| URL | Expected |
|-----|----------|
| http://localhost:8088 | Home page loads ✅ |
| http://localhost:8088/auth/login | Login form ✅ |
| http://localhost:8088/dashboard | Dashboard (after login) ✅ |
| http://localhost:8088/courses | Courses list ✅ |
| http://localhost:8088/api/exercises | API returns JSON ✅ |
| http://localhost:8088/invalid | 404 page shows ✅ |

---

## 📋 **IMPORTANT FILES**

| File | Purpose |
|------|---------|
| `application.yml` | Main config |
| `application-dev.yml` | Dev config |
| `application-prod.yml` | Prod config |
| `schema.sql` | Database init |
| `pom.xml` | Dependencies |
| `logs/klearn.log` | Application logs |

---

## 🎯 **NEXT STEPS**

1. ✅ Start the application
2. ✅ Test login flow
3. ✅ Browse courses
4. ✅ Try exercises
5. ✅ Check error handling
6. ✅ Review logs if issues

See **TESTING_CHECKLIST.md** for comprehensive test cases

---

## 💡 **TIPS**

- **Develop mode:** `mvn spring-boot:run` has auto-reload
- **Debug:** Add `--debug` flag to see detailed logs
- **Profile:** Switch profiles with `--spring.profiles.active=prod`
- **Logs:** Check `logs/klearn.log` for detailed diagnostics

---

## ⏱️ **TIMING**

| Step | Time |
|------|------|
| Setup | 1 min |
| Database | 1 min |
| Build | 1 min |
| Run | <1 min |
| Test | 2 min |
| **TOTAL** | **5 min** |

---

## ✅ **YOU'RE READY!**

If you get "Tomcat started on port(s): 8088" → **SUCCESS! 🎉**

Open http://localhost:8088 and enjoy K-Learn!

---

**For more details, see:**
- 📖 README.md - Full documentation
- ✅ TESTING_CHECKLIST.md - All test cases
- 📊 STATUS_REPORT.md - Project status
- 📋 AUDIT_REPORT.md - Component review

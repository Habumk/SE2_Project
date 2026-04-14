# 🧪 K-LEARN TESTING CHECKLIST - PRE-DEADLINE

**Date:** April 14, 2026 | **Status:** Critical Path Testing

---

## ✅ CORE FUNCTIONALITY TESTS (MUST PASS)

### **Module 1: Authentication & Session**
- [ ] **Login Page Loads** - Visit http://localhost:8088/auth/login
  - Expected: Login form loads without errors
  - Verify: No 500 errors, CSS loads, form visible

- [ ] **Valid Login** - Email: `bvanh004@gmail.com`, Password: (correct password)
  - Expected: Redirects to /dashboard
  - Verify: Session created, user displayed

- [ ] **Invalid Login** - Wrong password
  - Expected: Shows error message "Invalid email or password"
  - Verify: No null pointer exception

- [ ] **Registration** - Try registering with new email
  - Expected: Shows validation or success message
  - Verify: Form validates password strength

- [ ] **Logout** - Click logout button
  - Expected: Redirects to login page
  - Verify: Session destroyed (JSESSIONID removed)

---

### **Module 2: Dashboard & Navigation**
- [ ] **Dashboard Access** - After login, view dashboard
  - Expected: Dashboard page loads
  - Verify: User info displayed, XP/Streak shows, no nulls

- [ ] **Sidebar Navigation** - Click different menu items
  - Expected: All pages load (courses, profile, etc.)
  - Verify: No 404 or 500 errors

- [ ] **Dark Mode Toggle** - Click theme button
  - Expected: Theme switches
  - Verify: CSS applies correctly

---

### **Module 3: Courses & Lessons**
- [ ] **View Courses** - Go to /courses
  - Expected: 4 courses displayed
  - Verify: Course titles, descriptions load

- [ ] **View Course Details** - Click on a course
  - Expected: Lessons list displays
  - Verify: 5-6 lessons per course shown

- [ ] **Access Lesson Player** - Click lesson
  - Expected: Lesson player page loads
  - Verify: Theory, vocabulary, and controls visible

- [ ] **Theory Content** - Scroll in lesson player
  - Expected: Theory content displays
  - Verify: Markdown renders properly

- [ ] **Vocabulary Display** - Check vocabulary section
  - Expected: Words list with Korean/Vietnamese
  - Verify: All 48+ vocabulary words load

---

### **Module 4: Exercises - All 4 Skills**

#### **4.1 Listening Exercises**
- [ ] **Access Listening** - Click listening button in lesson
  - Expected: Listening page loads
  - Verify: Audio player or audio indicators visible

- [ ] **Play Audio** - Try to play audio file
  - Expected: Audio loads (if browser supports)
  - Verify: No CORS errors

#### **4.2 Reading Exercises**
- [ ] **Access Reading** - Click reading button
  - Expected: Reading passages display
  - Verify: Korean text and Vietnamese translation visible

- [ ] **Reading Questions** - Scroll to questions
  - Expected: Multiple choice questions appear
  - Verify: JSON parsing works, no undefined questions

#### **4.3 Speaking Exercises**
- [ ] **Access Speaking** - Click speaking button
  - Expected: Speaking prompts load
  - Verify: Korean text visible, pronunciation hints shown

- [ ] **Try Speaking Feature** - Attempt to record/submit
  - Expected: Form accepts submission or shows feature message
  - Verify: No JavaScript errors in console

#### **4.4 Writing Exercises**
- [ ] **Access Writing** - Click writing button
  - Expected: Writing exercises display
  - Verify: Character writing or translation exercises visible

- [ ] **Try Writing** - Attempt submission
  - Expected: Form accepts input
  - Verify: No validation errors if input is valid

---

### **Module 5: Exercise Submission & Scoring**

- [ ] **Submit Exercise** - Complete and submit any exercise
  - Expected: API call to `/api/exercises/{id}/submit`
  - Verify: No 500 errors, response is JSON

- [ ] **View Results** - Check scoring display
  - Expected: Score displayed (0-100%)
  - Verify: Correct calculation

- [ ] **Multiple Choice Answer** - Submit multiple choice
  - Expected: Correct/incorrect shown
  - Verify: Logic works, XP awarded if correct

---

### **Module 6: Gamification & Progress**

- [ ] **XP Update** - Complete a lesson
  - Expected: XP increases (100 per lesson)
  - Verify: Total XP updates in dashboard

- [ ] **Level Up** - Earn enough XP to level up
  - Expected: Level shows change (e.g., 1→2)
  - Verify: Threshold calculation correct (100 XP = Level 2)

- [ ] **Streak Display** - Login on consecutive days
  - Expected: Streak count increases
  - Verify: Shows in header as 🔥 icon

- [ ] **Badge Earning** - Complete conditions for badge
  - Expected: Badge appears in profile
  - Verify: Badge service runs, no duplicates

- [ ] **Leaderboard** - Visit /leaderboard
  - Expected: User ranking displays
  - Verify: Sorted by XP correctly

---

### **Module 7: Content Pages**

- [ ] **Hangul Page** - Visit /pages/hangul
  - Expected: Displays all 40 Hangul characters
  - Verify: Categorized (consonants, vowels, etc.)

- [ ] **Vocabulary Page** - Visit /pages/vocabulary
  - Expected: 48+ words in categories
  - Verify: Korean, Romanization, Vietnamese shown

- [ ] **Grammar Page** - Visit /pages/grammar
  - Expected: 5 grammar lessons display
  - Verify: Structure, examples load properly

---

### **Module 8: User Profile & Settings**

- [ ] **Profile Page** - Visit /profile
  - Expected: User info displayed
  - Verify: Name, email, level shown

- [ ] **Progress Tracking** - Check lessons completed
  - Expected: Shows completed lessons
  - Verify: Accurate count and status

- [ ] **Theme Preference** - Toggle dark mode
  - Expected: Preference saved (localStorage or session)
  - Verify: Persists on page reload

---

## 🔴 ERROR HANDLING TESTS (MUST NOT CRASH)

- [ ] **Invalid URL** - Visit /invalid-page
  - Expected: 404 error page shows
  - Verify: No white screen, redirect to home option visible

- [ ] **Database Error** - If database down, try login
  - Expected: Error message shown (not null pointer)
  - Verify: Graceful error, not 500 stack trace

- [ ] **Invalid Exercise ID** - Try to access /lessons/999/player
  - Expected: Redirect to /courses or error page
  - Verify: No NullPointerException

- [ ] **Unauthorized Access** - Try to access /dashboard without login
  - Expected: Redirect to /auth/login
  - Verify: Security working

- [ ] **Session Timeout** - Let session expire
  - Expected: Redirected to login on next action
  - Verify: No 500 errors

---

## 🔍 CODE QUALITY CHECKS

- [ ] **Java Compilation** - Run `mvn clean compile`
  - Expected: BUILD SUCCESS
  - Verify: No compilation errors

- [ ] **Unit Tests** - Run `mvn test`
  - Expected: All tests pass (XpServiceTest, etc.)
  - Verify: No test failures

- [ ] **Application Startup** - Run `mvn spring-boot:run`
  - Expected: Starts successfully on port 8088
  - Verify: Log shows "Tomcat started on port(s): 8088"

- [ ] **Database Connection** - Check logs for DB errors
  - Expected: "HHH000412: Hibernate ORM core version X.X.X"
  - Verify: Connected to MySQL without errors

---

## 📋 PERFORMANCE CHECKS

- [ ] **Page Load Time** - Check dashboard load
  - Expected: < 2 seconds
  - Verify: Using browser dev tools Network tab

- [ ] **API Response Time** - Submit exercise
  - Expected: < 1 second response
  - Verify: No slow queries

- [ ] **Memory Usage** - Check after 5 minutes of use
  - Expected: Stable, no leaks
  - Verify: No OutOfMemoryError in logs

---

## 🔐 SECURITY CHECKS

- [ ] **CSRF Token** - Check form sources
  - Expected: CSRF token present in forms
  - Verify: Form has hidden `_csrf` field

- [ ] **Password Hashing** - Check database password
  - Expected: Password is hashed (bcrypt format)
  - Verify: Starts with `$2a$` or `$2b$`

- [ ] **SQL Injection** - Try SQL injection in search
  - Expected: No SQL errors, safe
  - Verify: Input properly escaped

- [ ] **XSS Prevention** - Check HTML escaping
  - Expected: User input escaped in templates
  - Verify: No script execution

---

## ✅ FINAL SIGN-OFF

| Component | Status | Notes |
|-----------|--------|-------|
| Authentication | ⏳ TODO | |
| Dashboard | ⏳ TODO | |
| Courses/Lessons | ⏳ TODO | |
| Listening | ⏳ TODO | |
| Reading | ⏳ TODO | |
| Speaking | ⏳ TODO | |
| Writing | ⏳ TODO | |
| XP/Streak/Badges | ⏳ TODO | |
| Error Handling | ⏳ TODO | |
| Performance | ⏳ TODO | |

---

## 🚀 HOW TO RUN TESTS

### **Quick Start**
```bash
# 1. Build
mvn clean package

# 2. Run application
mvn spring-boot:run

# 3. Open browser
http://localhost:8088

# 4. Test critical flows (see above)
```

### **Test Database Reset**
```bash
# If needed, reset database:
# 1. Delete /target folder
# 2. Restart application
# 3. schema.sql will re-initialize
```

### **Check Logs**
```bash
# Tail logs in real-time
tail -f logs/klearn.log
```

---

## 📞 TROUBLESHOOTING

| Problem | Solution |
|---------|----------|
| **Port 8088 already in use** | Kill process or change port in application.yml |
| **Database connection error** | Verify MySQL running, credentials in application.yml |
| **Thymeleaf not rendering** | Check template files exist in `src/main/resources/templates/` |
| **NPE in LessonService** | Should be fixed now - test login → course → lesson flow |
| **404 on static resources** | Clear browser cache, verify /static files exist |

---

## ⏱️ ESTIMATED TIME

- Full test run: **15-20 minutes**
- Quick critical path: **5-10 minutes**
- Fix issues if found: **30-60 minutes**

**Good luck! 🍀**

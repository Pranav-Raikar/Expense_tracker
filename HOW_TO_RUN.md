# How to Run the Expense Tracker

## Step 1 — Run MySQL Setup Commands

Open MySQL CLI:
```
mysql -u Pranav -p
```
Then paste the contents of **MYSQL_SETUP.sql** (adds first_name, last_name, email columns to users table).

## Step 2 — Build & Run Spring Boot

Open a terminal in the `expense-tracker/` folder and run:

```bash
# Windows
mvnw.cmd spring-boot:run

# Mac / Linux
./mvnw spring-boot:run
```

Wait for:
```
Started ExpenseTrackerApplication in X.XXX seconds
```

## Step 3 — Open the App

Open your browser and go to:
```
http://localhost:8080/login.html
```

## Bugs Fixed in This Version

| File | Bug | Fix |
|------|-----|-----|
| `pom.xml` | Wrong Spring Boot version `4.0.5` (doesn't exist) | Changed to `3.3.5` |
| `pom.xml` | Wrong artifact `spring-boot-starter-webmvc` | Changed to `spring-boot-starter-web` |
| `pom.xml` | Wrong artifact `spring-boot-starter-data-jpa-test` | Changed to `spring-boot-starter-test` |
| `Balance.java` | `getUserId()` was returning `id` instead of `userId` | Fixed to return `userId` |
| `User.java` | No `@Table(name="users")` — mapped to wrong table | Added annotation |
| `application.properties` | Missing `serverTimezone`, `dialect` | Added full config |
| `login.html` | No signup form | Added Login/Sign Up tabs |
| `index.html` | Not calling real backend API | Fully connected to `/api` endpoints |
| `index.html` | Balance/Expenses were in sidebar | Moved to Dashboard cards |
| `index.html` | No "Other" custom category | Added with custom tag input |
| `index.html` | No report filters | Added date range + category filter |

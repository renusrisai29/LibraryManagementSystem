# Library Management System

A console-based Library Management System built in Java, backed by MySQL.
Demonstrates OOP principles (inheritance, polymorphism, encapsulation) and
three design patterns: Singleton, Factory, and Strategy.

## Tech stack
- Java 17
- MySQL 8
- Maven (dependency management)
- JDBC (database connectivity)

## Setup

### 1. Database
- Install MySQL Server + MySQL Workbench.
- Open `sql/schema.sql` in Workbench and run the whole script. This creates
  the `library_db` database, three tables, and inserts sample data.

### 2. Project
- Open the `LibraryManagementSystem` folder in **IntelliJ IDEA** (or VS Code
  with the Java Extension Pack + Maven for Java extension).
- IntelliJ/VS Code will auto-detect `pom.xml` and download the MySQL
  connector dependency.
- Open `src/main/java/library/DBConnection.java` and update `DB_USER` /
  `DB_PASS` to match your local MySQL credentials.

### 3. Run
- Run `Main.java` (right-click → Run, or `mvn compile exec:java` from
  terminal).
- You'll see a console menu:
  ```
  1. Search Book
  2. Issue Book
  3. Return Book
  4. View All Books
  0. Exit
  ```

## Project structure
```
LibraryManagementSystem/
├── pom.xml
├── sql/
│   └── schema.sql
├── src/main/java/library/
│   ├── DBConnection.java   # Singleton JDBC connection
│   ├── User.java           # abstract base class
│   ├── Student.java        # extends User
│   ├── Librarian.java      # extends User
│   ├── UserFactory.java    # Factory pattern
│   ├── Book.java
│   ├── Transaction.java
│   ├── FineStrategy.java   # Strategy pattern
│   ├── Library.java        # Singleton, core business logic
│   └── Main.java           # console entry point
└── README.md
```

## Design patterns used
- **Singleton** — `Library` and `DBConnection` each have exactly one instance.
- **Factory** — `UserFactory` creates `Student` or `Librarian` objects without
  the caller needing to know the concrete class.
- **Strategy** — `FineStrategy` interface lets you swap fine-calculation logic
  (currently flat-rate ₹5/day) without touching `Library.java`.

## Next steps / stretch goals
- Add a login screen that checks credentials against the `users` table.
- Add JUnit tests for `issueBook` / `returnBook`.
- Replace the console UI with a JavaFX front-end or a Spring Boot REST API.

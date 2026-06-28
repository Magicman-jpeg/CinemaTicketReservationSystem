# Cinema Ticket Reservation System

## COMP 013 - Object-Oriented Programming Final Project
### Group 2 - Cinema Hall Ticket Sales

---

## Project Overview

A complete console-based Java application for managing cinema ticket reservations with SQLite database integration. The system supports role-based access (Admin and Customer), full CRUD operations, seat reservation with ASCII grid display, and report generation.

---

## OOP Concepts Demonstrated

| Concept | Implementation |
|---------|---------------|
| **Encapsulation** | Private fields with public getters/setters in all model classes |
| **Inheritance** | `User` (abstract) → `Admin`, `Customer` |
| **Polymorphism** | `displayInfo()` method overridden in Admin and Customer |
| **Abstraction** | Abstract `User` class defines contract for subclasses |
| **Exception Handling** | 5 custom exception classes for business logic validation |
| **Constructors** | Default, parameterized, and overloaded constructors |
| **Methods** | CRUD operations, business logic, utility/helper methods |

---

## Project Structure

```
CinemaTicketSystem/
├── src/
│   └── com/cinema/
│       ├── Main.java                 (Entry point)
│       ├── model/                    (Entity classes)
│       │   ├── User.java            (Abstract base class)
│       │   ├── Admin.java           (Inherits User)
│       │   ├── Customer.java        (Inherits User)
│       │   ├── Movie.java
│       │   ├── Screening.java
│       │   ├── CinemaSeat.java
│       │   └── Transaction.java
│       ├── exception/                (Custom exceptions)
│       │   ├── InvalidLoginException.java
│       │   ├── InvalidSeatException.java
│       │   ├── DuplicateReservationException.java
│       │   ├── DatabaseConnectionException.java
│       │   └── PaymentFailedException.java
│       ├── dao/                      (Data Access Objects)
│       │   ├── DatabaseManager.java  (Singleton DB connector)
│       │   ├── MovieDAO.java
│       │   ├── AdminDAO.java
│       │   ├── CustomerDAO.java
│       │   ├── ScreeningDAO.java
│       │   ├── CinemaSeatDAO.java
│       │   └── TransactionDAO.java
│       ├── service/                  (Business Logic)
│       │   ├── LoginService.java
│       │   ├── ReservationService.java
│       │   └── ReportService.java
│       └── ui/                       (Console UI)
│           ├── ConsoleUtils.java
│           ├── AdminMenu.java
│           └── CustomerMenu.java
├── db/
│   ├── schema.sql                    (Database schema)
│   ├── sample_data.sql               (Sample data inserts)
│   └── cinema.db                     (SQLite database file)
├── build.gradle                      (Gradle build file)
└── README.md                         (This file)
```

---

## Prerequisites

- **Java JDK 17+** (tested with Java 25)
- **SQLite3** command-line tool (pre-installed on most Linux/Mac systems)

### Verify Prerequisites
```bash
java -version      # Should show Java 17 or later
sqlite3 --version  # Should show SQLite version
```

---

## Compilation and Running Instructions

### Option 1: Quick Start (Recommended)

```bash
# 1. Navigate to project directory
cd CinemaTicketSystem

# 2. Compile all Java files
javac -d out $(find src -name "*.java")

# 3. Initialize the database (first time only)
sqlite3 db/cinema.db < db/schema.sql
sqlite3 db/cinema.db < db/sample_data.sql

# 4. Run the application
java -cp out com.cinema.Main
```

### Option 2: Using the Build Script

```bash
# Make the build script executable and run
chmod +x run.sh
./run.sh
```

### Option 3: Using Gradle

```bash
gradle build
gradle run --console=plain
```

---

## Default Login Credentials

### Admin Accounts
| Username | Password | Name |
|----------|----------|------|
| admin | admin123 | System Administrator |
| manager | mgr2024 | Maria Santos |
| supervisor | sup2024 | Juan Dela Cruz |

### Customer Accounts
| Username | Password | Name | Membership |
|----------|----------|------|------------|
| jdoe | pass123 | John Doe | REGULAR |
| mcruz | pass456 | Maria Cruz | PREMIUM (10% off) |
| rgarcia | pass789 | Roberto Garcia | VIP (20% off) |
| alopez | alop2024 | Ana Lopez | REGULAR |
| ctan | ctan2024 | Carlos Tan | PREMIUM |

---

## Features

### Admin Features
- [x] Login with admin credentials
- [x] **Manage Movies** - Add, View All, Search, Update, Delete
- [x] **Manage Screenings** - Add (auto-generates 110 seats), View, Update, Delete
- [x] **Manage Customers** - View All, Search by Name, Delete
- [x] **View All Transactions** - Tabular display of all bookings
- [x] **Generate Reports** - Sales Summary, Daily Revenue, Popular Movies, Occupancy

### Customer Features
- [x] Login / Register new account
- [x] **Browse Movies** - View all, search by title
- [x] **View Screenings** - See all available showtimes
- [x] **Reserve a Seat** - ASCII seat map, choose seat, select payment, confirm
- [x] **My Reservations** - View booking history
- [x] **Cancel Reservation** - Cancel and release seat

### Seat Map (ASCII Grid)
```
              ========== SCREEN ==========

        1    2    3    4    5    6    7    8    9   10
      +----+----+----+----+----+----+----+----+----+----+
  A   | XX | XX | -- | -- | -- | -- | -- | -- | -- | -- |
      +----+----+----+----+----+----+----+----+----+----+
  B   | -- | -- | -- | -- | XX | -- | -- | -- | -- | -- |
      ...
  K   | -- | -- | -- | -- | -- | -- | -- | -- | -- | -- |
      +----+----+----+----+----+----+----+----+----+----+

  Legend: [ -- ] Available   [ XX ] Reserved   [ ## ] Occupied
```

---

## Database Schema

### Tables
- **movie** - Films showing in the cinema
- **admin** - Administrator accounts
- **customer** - Customer accounts with membership types
- **screenings** - Movie showtimes (links to movie)
- **cinema_seat** - Individual seats per screening (rows A-K, cols 1-10)
- **transaction** - Ticket purchase records

### Relationships
- `screenings.movie_id` → `movie.movie_id` (FK)
- `cinema_seat.screening_id` → `screenings.screening_id` (FK)
- `transaction.customer_id` → `customer.customer_id` (FK)
- `transaction.screening_id` → `screenings.screening_id` (FK)
- `transaction.seat_id` → `cinema_seat.seat_id` (FK)

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `sqlite3: command not found` | Install SQLite: `sudo apt install sqlite3` or `brew install sqlite` |
| `Database file missing` | Run: `sqlite3 db/cinema.db < db/schema.sql && sqlite3 db/cinema.db < db/sample_data.sql` |
| `Class not found` | Ensure you compiled with `javac -d out $(find src -name "*.java")` |
| Login fails | Use exact credentials from the table above (case-sensitive) |

---

## Team Members - Group 2

COMP 013 - Object-Oriented Programming with Database Integration

---

*Generated as Final Project for OOP (COMP 013)*

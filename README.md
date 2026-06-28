# Cinema Ticket Reservation System

## COMP 013 - Object-Oriented Programming Final Project | Group 2

Console-based Java application with SQLite database integration.

---

## 10 Java Modules

| # | Module | Purpose |
|---|--------|---------|
| 1 | `LoginModule.java` | User authentication with database validation |
| 2 | `MainMenuModule.java` | Main menu + seat reservation with ASCII map |
| 3 | `AddRecordModule.java` | Add movies, customers, screenings |
| 4 | `ViewRecordsModule.java` | Display all records in table format |
| 5 | `SearchRecordModule.java` | Search by ID or keyword |
| 6 | `UpdateRecordModule.java` | Edit existing records |
| 7 | `DeleteRecordModule.java` | Delete with confirmation |
| 8 | `ReportModule.java` | Sales summary, revenue, popular movies |
| 9 | `InputValidator.java` | Input validation (email, phone, seat, etc.) |
| 10 | `ExceptionHandler.java` | Centralized try-catch error handling |

**Supporting files:**
- `DatabaseHelper.java` - SQLite database connectivity
- `Main.java` - Entry point

---

## How to Run

### Windows (CMD):
```cmd
run.bat
```

### Linux/Mac:
```bash
chmod +x run.sh
./run.sh
```

### Manual:
```cmd
javac -d out src\*.java
sqlite3 db\cinema.db < db\schema.sql
sqlite3 db\cinema.db < db\sample_data.sql
java -cp out Main
```

---

## Requirements

- Java JDK 17+
- SQLite3 (command-line tool)

---

## Login Credentials

### Admin Accounts:
| Username | Password | Role |
|----------|----------|------|
| fairytopia | popcornmanager | Manager |
| highschooldxd | 12345pogisijay | Assistant Manager |
| magicman21 | fordxsya | Box Office Staff |

### Customer Accounts (App Users):
| Username | Password | Name |
|----------|----------|------|
| FordMustang | seat71HEEHEE | Ford Levisberg |
| Clydejayy2005 | romancefan01_ | Jayson Clyde Aravelo |
| mateoDeTorre62 | HEREosandwich | Mateo Torre |

---

## Database

- **SQLite** file-based database (`db/cinema.db`)
- Data from: `EDITED GROUP 2_cinema_hall_ticket_sales.xlsx`
- 15 movies, 50 customers, 10 admins, 33 screenings, 100 seats

---

## Project Structure

```
CinemaTicketReservationSystem/
├── src/
│   ├── Main.java              (Entry point)
│   ├── LoginModule.java       (Module 1)
│   ├── MainMenuModule.java    (Module 2)
│   ├── AddRecordModule.java   (Module 3)
│   ├── ViewRecordsModule.java (Module 4)
│   ├── SearchRecordModule.java(Module 5)
│   ├── UpdateRecordModule.java(Module 6)
│   ├── DeleteRecordModule.java(Module 7)
│   ├── ReportModule.java      (Module 8)
│   ├── InputValidator.java    (Module 9)
│   ├── ExceptionHandler.java  (Module 10)
│   └── DatabaseHelper.java    (DB connectivity)
├── db/
│   ├── schema.sql             (Database schema)
│   └── sample_data.sql        (Sample data from Excel)
├── run.bat                    (Windows launcher)
├── run.sh                     (Linux/Mac launcher)
└── README.md
```

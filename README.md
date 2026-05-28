# CinemaTicketReservationSystem
A Java + SQLite project with Python database builder and a simple HTML/JS frontend. Supports admin/customer login, seat reservation, and report generation.

1. Open terminal in `CinemaTicketReservationSystem/database`.
2. Run `python -m pip install openpyxl` (only once, if not installed yet).
3. Run `python build_database.py` to create `cinema.db` and frontend JSON data from the Excel file.
4. Open terminal in `CinemaTicketReservationSystem/backend`.
5. Run `mvn clean compile`.
6. Run `mvn exec:java` and log in using `admin / admin123` (or any valid user in the database).
7. For seat layout, open `CinemaTicketReservationSystem/frontend/seat-reservation.html` in a browser.
8. For report page, open `CinemaTicketReservationSystem/frontend/report.html` in a browser.
9. If browser blocks local JSON loading, run `python -m http.server 8000` inside `CinemaTicketReservationSystem/frontend` and open `http://localhost:8000/seat-reservation.html` and `http://localhost:8000/report.html`.

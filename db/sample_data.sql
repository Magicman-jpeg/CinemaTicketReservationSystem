-- Cinema Ticket Reservation System - Sample Data
-- Based on OOP_GROUP 2_cinema_hall_ticket_sales.xlsx dataset
-- COMP 013 OOP Final Project

PRAGMA foreign_keys = ON;

-- ========== ADMIN DATA ==========
INSERT OR IGNORE INTO admin (username, password, full_name, email, department, access_level) VALUES
('admin', 'admin123', 'System Administrator', 'admin@cinema.ph', 'IT', 'FULL'),
('manager', 'mgr2024', 'Maria Santos', 'maria.santos@cinema.ph', 'Operations', 'FULL'),
('supervisor', 'sup2024', 'Juan Dela Cruz', 'juan.delacruz@cinema.ph', 'Customer Service', 'LIMITED');

-- ========== MOVIE DATA ==========
INSERT OR IGNORE INTO movie (title, genre, duration_minutes, rating, director, release_date) VALUES
('Avengers: Endgame', 'Action', 181, 'PG-13', 'Anthony Russo', '2019-04-26'),
('Spider-Man: No Way Home', 'Action', 148, 'PG-13', 'Jon Watts', '2021-12-17'),
('The Batman', 'Action', 176, 'PG-13', 'Matt Reeves', '2022-03-04'),
('Everything Everywhere All at Once', 'Sci-Fi', 139, 'R', 'Daniel Kwan', '2022-03-25'),
('Top Gun: Maverick', 'Action', 130, 'PG-13', 'Joseph Kosinski', '2022-05-27'),
('Black Panther: Wakanda Forever', 'Action', 161, 'PG-13', 'Ryan Coogler', '2022-11-11'),
('Avatar: The Way of Water', 'Sci-Fi', 192, 'PG-13', 'James Cameron', '2022-12-16'),
('Oppenheimer', 'Drama', 180, 'R', 'Christopher Nolan', '2023-07-21'),
('Barbie', 'Comedy', 114, 'PG-13', 'Greta Gerwig', '2023-07-21'),
('Dune: Part Two', 'Sci-Fi', 166, 'PG-13', 'Denis Villeneuve', '2024-03-01');

-- ========== CUSTOMER DATA ==========
INSERT OR IGNORE INTO customer (username, password, full_name, email, phone_number, membership_type, registration_date) VALUES
('jdoe', 'pass123', 'John Doe', 'john.doe@email.com', '09171234567', 'REGULAR', '2024-01-15'),
('mcruz', 'pass456', 'Maria Cruz', 'maria.cruz@email.com', '09181234567', 'PREMIUM', '2024-01-20'),
('rgarcia', 'pass789', 'Roberto Garcia', 'roberto.garcia@email.com', '09191234567', 'VIP', '2024-02-01'),
('alopez', 'alop2024', 'Ana Lopez', 'ana.lopez@email.com', '09201234567', 'REGULAR', '2024-02-10'),
('ctan', 'ctan2024', 'Carlos Tan', 'carlos.tan@email.com', '09211234567', 'PREMIUM', '2024-02-15'),
('lreyes', 'lrey2024', 'Laura Reyes', 'laura.reyes@email.com', '09221234567', 'REGULAR', '2024-03-01'),
('mvillanueva', 'mvil2024', 'Marco Villanueva', 'marco.v@email.com', '09231234567', 'VIP', '2024-03-10'),
('pnavarro', 'pnav2024', 'Patricia Navarro', 'patricia.n@email.com', '09241234567', 'PREMIUM', '2024-03-15'),
('jsantos', 'jsan2024', 'Jose Santos', 'jose.santos@email.com', '09251234567', 'REGULAR', '2024-04-01'),
('kristel', 'kris2024', 'Kristel Mendoza', 'kristel.m@email.com', '09261234567', 'REGULAR', '2024-04-10');


-- ========== SCREENING DATA ==========
INSERT OR IGNORE INTO screenings (movie_id, screen_date, screen_time, hall_number, ticket_price) VALUES
(1, '2024-06-01', '10:00', 1, 350.00),
(1, '2024-06-01', '14:00', 2, 350.00),
(2, '2024-06-01', '11:00', 3, 320.00),
(3, '2024-06-01', '13:00', 1, 300.00),
(4, '2024-06-01', '16:00', 2, 280.00),
(5, '2024-06-02', '10:00', 1, 350.00),
(6, '2024-06-02', '13:00', 2, 320.00),
(7, '2024-06-02', '16:00', 3, 400.00),
(8, '2024-06-03', '10:00', 1, 380.00),
(9, '2024-06-03', '13:00', 2, 300.00),
(10, '2024-06-03', '16:00', 3, 360.00),
(1, '2024-06-04', '19:00', 1, 400.00),
(8, '2024-06-04', '19:00', 2, 420.00),
(10, '2024-06-04', '19:00', 3, 380.00);

-- ========== GENERATE SEATS FOR ALL SCREENINGS ==========
-- Generate seats for screening 1 (11 rows A-K, 10 columns each = 110 seats)
INSERT OR IGNORE INTO cinema_seat (screening_id, seat_row, seat_column, status)
SELECT s.screening_id, r.row_label, c.col_num, 'AVAILABLE'
FROM screenings s
CROSS JOIN (SELECT 'A' as row_label UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D'
            UNION SELECT 'E' UNION SELECT 'F' UNION SELECT 'G' UNION SELECT 'H'
            UNION SELECT 'I' UNION SELECT 'J' UNION SELECT 'K') r
CROSS JOIN (SELECT 1 as col_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
            UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) c;

-- ========== SAMPLE TRANSACTIONS (Pre-existing bookings) ==========
-- Reserve some seats first (mark them as RESERVED)
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 1 AND seat_row = 'A' AND seat_column = 1;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 1 AND seat_row = 'A' AND seat_column = 2;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 1 AND seat_row = 'B' AND seat_column = 5;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 2 AND seat_row = 'C' AND seat_column = 3;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 2 AND seat_row = 'D' AND seat_column = 7;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 3 AND seat_row = 'E' AND seat_column = 5;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 4 AND seat_row = 'A' AND seat_column = 1;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 5 AND seat_row = 'F' AND seat_column = 8;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 6 AND seat_row = 'G' AND seat_column = 3;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 7 AND seat_row = 'H' AND seat_column = 6;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 8 AND seat_row = 'A' AND seat_column = 4;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 9 AND seat_row = 'B' AND seat_column = 2;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 9 AND seat_row = 'C' AND seat_column = 6;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 10 AND seat_row = 'D' AND seat_column = 9;
UPDATE cinema_seat SET status = 'RESERVED'
WHERE screening_id = 11 AND seat_row = 'E' AND seat_column = 1;


-- Now insert corresponding transactions
INSERT OR IGNORE INTO "transaction" (customer_id, screening_id, seat_id, seat_label, transaction_date, transaction_time, amount_paid, payment_method, status) VALUES
(1, 1, (SELECT seat_id FROM cinema_seat WHERE screening_id=1 AND seat_row='A' AND seat_column=1), 'A1', '2024-05-28', '09:30:00', 350.00, 'CASH', 'CONFIRMED'),
(1, 1, (SELECT seat_id FROM cinema_seat WHERE screening_id=1 AND seat_row='A' AND seat_column=2), 'A2', '2024-05-28', '09:31:00', 350.00, 'CASH', 'CONFIRMED'),
(2, 1, (SELECT seat_id FROM cinema_seat WHERE screening_id=1 AND seat_row='B' AND seat_column=5), 'B5', '2024-05-28', '10:15:00', 315.00, 'CREDIT_CARD', 'CONFIRMED'),
(3, 2, (SELECT seat_id FROM cinema_seat WHERE screening_id=2 AND seat_row='C' AND seat_column=3), 'C3', '2024-05-28', '11:00:00', 280.00, 'GCASH', 'CONFIRMED'),
(4, 2, (SELECT seat_id FROM cinema_seat WHERE screening_id=2 AND seat_row='D' AND seat_column=7), 'D7', '2024-05-29', '08:45:00', 350.00, 'DEBIT_CARD', 'CONFIRMED'),
(5, 3, (SELECT seat_id FROM cinema_seat WHERE screening_id=3 AND seat_row='E' AND seat_column=5), 'E5', '2024-05-29', '09:20:00', 288.00, 'CREDIT_CARD', 'CONFIRMED'),
(6, 4, (SELECT seat_id FROM cinema_seat WHERE screening_id=4 AND seat_row='A' AND seat_column=1), 'A1', '2024-05-29', '12:00:00', 300.00, 'CASH', 'CONFIRMED'),
(7, 5, (SELECT seat_id FROM cinema_seat WHERE screening_id=5 AND seat_row='F' AND seat_column=8), 'F8', '2024-05-30', '15:30:00', 224.00, 'GCASH', 'CONFIRMED'),
(8, 6, (SELECT seat_id FROM cinema_seat WHERE screening_id=6 AND seat_row='G' AND seat_column=3), 'G3', '2024-05-30', '09:00:00', 315.00, 'CREDIT_CARD', 'CONFIRMED'),
(9, 7, (SELECT seat_id FROM cinema_seat WHERE screening_id=7 AND seat_row='H' AND seat_column=6), 'H6', '2024-05-30', '10:45:00', 320.00, 'CASH', 'CONFIRMED'),
(10, 8, (SELECT seat_id FROM cinema_seat WHERE screening_id=8 AND seat_row='A' AND seat_column=4), 'A4', '2024-05-31', '14:20:00', 400.00, 'DEBIT_CARD', 'CONFIRMED'),
(1, 9, (SELECT seat_id FROM cinema_seat WHERE screening_id=9 AND seat_row='B' AND seat_column=2), 'B2', '2024-05-31', '12:30:00', 380.00, 'GCASH', 'CONFIRMED'),
(2, 9, (SELECT seat_id FROM cinema_seat WHERE screening_id=9 AND seat_row='C' AND seat_column=6), 'C6', '2024-05-31', '12:35:00', 270.00, 'CREDIT_CARD', 'CONFIRMED'),
(3, 10, (SELECT seat_id FROM cinema_seat WHERE screening_id=10 AND seat_row='D' AND seat_column=9), 'D9', '2024-06-01', '08:00:00', 288.00, 'GCASH', 'CONFIRMED'),
(4, 11, (SELECT seat_id FROM cinema_seat WHERE screening_id=11 AND seat_row='E' AND seat_column=1), 'E1', '2024-06-01', '15:00:00', 360.00, 'CASH', 'CONFIRMED');

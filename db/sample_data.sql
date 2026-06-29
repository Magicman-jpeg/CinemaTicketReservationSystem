-- Cinema Ticket Reservation System - Sample Data
-- Source: EDITED GROUP 2_cinema_hall_ticket_sales.xlsx
-- COMP 009 OOP Final Project - Group 1

PRAGMA foreign_keys = ON;

-- ========== LOOKUP DATA ==========

-- Movie Genres
INSERT OR IGNORE INTO movie_genre (genre_id, movie_genre) VALUES
(1, 'Romance'),
(2, 'Action'),
(3, 'Horror'),
(4, 'Comedy'),
(5, 'Thriller'),
(6, 'Fantasy'),
(7, 'Adventure'),
(8, 'Drama'),
(9, 'Sci-Fi');

-- Movie Status
INSERT OR IGNORE INTO movie_status (status_id, movie_status) VALUES
(1, 'Upcoming'),
(2, 'Showing'),
(3, 'Post-Screening');

-- Age Ratings
INSERT OR IGNORE INTO age_rating (age_rate_id, movie_age_rating) VALUES
(1, 'PG'),
(2, 'R13'),
(3, 'R16');

-- Seat Types (with pricing)
INSERT OR IGNORE INTO seat_type (seat_type_id, seat_type, seat_description, ticket_price) VALUES
(1, 'VIP', 'Reclining Seats, Butler Service, Complementary Snacks', 590.00),
(2, 'Premium', 'Reclining Seats', 385.00),
(3, 'Regular', 'No Benefits', 275.00);

-- ========== MOVIE DATA (15 movies) ==========
INSERT OR IGNORE INTO movie (movie_id, movie_title, genre_id, movie_duration, duration_code, release_date, status_id, age_rate_id) VALUES
(1, 'Fractured', 5, '1 hr 40 mins', 140, '2019-10-11', 2, 3),
(2, 'Wicked Part 1', 6, '2 hrs 40 mins', 240, '2024-11-22', 2, 1),
(3, 'Michael', 8, '2 hrs 07 mins', 207, '2026-04-24', 2, 1),
(4, 'National Treasure', 7, '2 hrs 11 mins', 211, '2004-11-19', 3, 1),
(5, 'Journey 2: Mysterious Island', 7, '1 hr 34 mins', 134, '2012-02-10', 2, 1),
(6, 'Chiikawa: Mermaid Island', 7, '1 hr 30 mins', 130, '2026-07-24', 1, 1),
(7, 'Lucy', 5, '1 hr 30 mins', 130, '2014-07-25', 2, 3),
(8, 'Hunger Games 2: Catching Fire', 9, '2 hrs 26 mins', 226, '2013-11-22', 2, 1),
(9, 'Ready Player One', 9, '2 hrs 20 mins', 220, '2018-03-29', 3, 1),
(10, 'John Wick 1', 2, '1 hr 41 mins', 141, '2014-10-24', 2, 3),
(11, 'Hoppers', 4, '1 hr 48 mins', 148, '2026-03-06', 3, 1),
(12, 'Me Before You', 1, '1 hr 50 mins', 150, '2016-06-03', 2, 1),
(13, 'Insidious', 3, '1 hr 41 mins', 141, '2011-04-01', 2, 1),
(14, 'Deranged', 3, '1 hr 49 mins', 149, '2012-07-05', 3, 2),
(15, 'The Sheep Detectives', 8, '1 hr 49 mins', 149, '2026-05-08', 2, 1);

-- ========== ADMIN DATA (10 staff) ==========
INSERT OR IGNORE INTO admin (admin_id, role, admin_name, admin_username, admin_pass) VALUES
(1001, 'Default Admin', 'RandHive', 'admin', 'admin123'),
(2409, 'Manager', 'Patrice', 'fairytopia', 'popcornmanager'),
(2102, 'Assistant Manager', 'Clifferson', 'highschooldxd', '12345pogisijay'),
(1207, 'Box Office Staff', 'Henry', 'magicman21', 'fordxsya'),
(1029, 'Admin Staff', 'David', 'sfushigi26', '33nights44days'),
(1030, 'Box Office Staff', 'Maria', 'sailorvenus88', 'moonhearts!77'),
(1031, 'Box Office Staff', 'John', 'naruto09', 'ramenlover@22'),
(1032, 'Admin Staff', 'Liza', 'marvelgirl23', 'avengersendgame44'),
(1033, 'Box Office Staff', 'Ramon', 'batmanarkham42', 'gothamcity#99'),
(1034, 'Box Office Staff', 'Teresa', 'disneyfan17', 'pixiedust*33'),
(1035, 'Box Office Staff', 'Miguel', 'matrixneo101', 'redpillbluepill77');


-- ========== CINEMA SEAT DATA (100 seats: rows A-J, columns 1-10) ==========
INSERT OR IGNORE INTO cinema_seat (seat_no, row, col) VALUES
('A1','A',1),('A2','A',2),('A3','A',3),('A4','A',4),('A5','A',5),
('A6','A',6),('A7','A',7),('A8','A',8),('A9','A',9),('A10','A',10),
('B1','B',1),('B2','B',2),('B3','B',3),('B4','B',4),('B5','B',5),
('B6','B',6),('B7','B',7),('B8','B',8),('B9','B',9),('B10','B',10),
('C1','C',1),('C2','C',2),('C3','C',3),('C4','C',4),('C5','C',5),
('C6','C',6),('C7','C',7),('C8','C',8),('C9','C',9),('C10','C',10),
('D1','D',1),('D2','D',2),('D3','D',3),('D4','D',4),('D5','D',5),
('D6','D',6),('D7','D',7),('D8','D',8),('D9','D',9),('D10','D',10),
('E1','E',1),('E2','E',2),('E3','E',3),('E4','E',4),('E5','E',5),
('E6','E',6),('E7','E',7),('E8','E',8),('E9','E',9),('E10','E',10),
('F1','F',1),('F2','F',2),('F3','F',3),('F4','F',4),('F5','F',5),
('F6','F',6),('F7','F',7),('F8','F',8),('F9','F',9),('F10','F',10),
('G1','G',1),('G2','G',2),('G3','G',3),('G4','G',4),('G5','G',5),
('G6','G',6),('G7','G',7),('G8','G',8),('G9','G',9),('G10','G',10),
('H1','H',1),('H2','H',2),('H3','H',3),('H4','H',4),('H5','H',5),
('H6','H',6),('H7','H',7),('H8','H',8),('H9','H',9),('H10','H',10),
('I1','I',1),('I2','I',2),('I3','I',3),('I4','I',4),('I5','I',5),
('I6','I',6),('I7','I',7),('I8','I',8),('I9','I',9),('I10','I',10),
('J1','J',1),('J2','J',2),('J3','J',3),('J4','J',4),('J5','J',5),
('J6','J',6),('J7','J',7),('J8','J',8),('J9','J',9),('J10','J',10);

-- ========== SCREENINGS DATA (33 screenings across 3 days) ==========
INSERT OR IGNORE INTO screenings (screening_id, screening_day, screening_date, time_slot, seat_type_id, movie_id, cinema_no) VALUES
('SUN-1', 'SUN', '2026-06-07', '12:30', 1, 3, 5),
('SUN-2', 'SUN', '2026-06-07', '15:30', 1, 3, 5),
('SUN-3', 'SUN', '2026-06-07', '18:30', 1, 3, 5),
('SUN-4', 'SUN', '2026-06-07', '16:00', 2, 2, 3),
('SUN-5', 'SUN', '2026-06-07', '19:00', 2, 5, 4),
('SUN-6', 'SUN', '2026-06-07', '12:00', 3, 1, 1),
('SUN-7', 'SUN', '2026-06-07', '14:20', 3, 7, 2),
('SUN-8', 'SUN', '2026-06-07', '16:40', 3, 1, 1),
('SUN-9', 'SUN', '2026-06-07', '19:00', 3, 7, 2),
('MON-1', 'MON', '2026-06-08', '13:00', 1, 3, 5),
('MON-2', 'MON', '2026-06-08', '15:00', 1, 3, 5),
('MON-3', 'MON', '2026-06-08', '17:00', 1, 15, 5),
('MON-4', 'MON', '2026-06-08', '19:00', 1, 3, 5),
('MON-5', 'MON', '2026-06-08', '12:00', 2, 8, 3),
('MON-6', 'MON', '2026-06-08', '14:15', 2, 12, 4),
('MON-7', 'MON', '2026-06-08', '17:00', 2, 8, 3),
('MON-8', 'MON', '2026-06-08', '19:00', 2, 12, 4),
('MON-9', 'MON', '2026-06-08', '12:00', 3, 10, 1),
('MON-10', 'MON', '2026-06-08', '14:15', 3, 13, 2),
('MON-11', 'MON', '2026-06-08', '16:30', 3, 10, 1),
('MON-12', 'MON', '2026-06-08', '19:00', 3, 13, 2),
('TUE-1', 'TUE', '2026-06-09', '13:00', 1, 3, 5),
('TUE-2', 'TUE', '2026-06-09', '15:00', 1, 3, 5),
('TUE-3', 'TUE', '2026-06-09', '17:00', 1, 15, 5),
('TUE-4', 'TUE', '2026-06-09', '19:00', 1, 3, 5),
('TUE-5', 'TUE', '2026-06-09', '12:00', 2, 2, 3),
('TUE-6', 'TUE', '2026-06-09', '14:15', 2, 5, 4),
('TUE-7', 'TUE', '2026-06-09', '17:00', 2, 2, 3),
('TUE-8', 'TUE', '2026-06-09', '19:00', 2, 5, 4),
('TUE-9', 'TUE', '2026-06-09', '12:00', 3, 1, 1),
('TUE-10', 'TUE', '2026-06-09', '14:15', 3, 7, 2),
('TUE-11', 'TUE', '2026-06-09', '16:30', 3, 1, 1),
('TUE-12', 'TUE', '2026-06-09', '19:00', 3, 7, 2);


-- ========== CUSTOMER DATA (first 10 from 296 total in Excel) ==========
INSERT OR IGNORE INTO customer (customer_no, name, age, email_address, app_user, customer_username, customer_pass, mobile_no) VALUES
(1001, 'Ford Levisberg', 20, 'levisfordisberg@gmail.com', 'Yes', 'FordMustang', 'seat71HEEHEE', '09625872041'),
(1002, 'Jayson Clyde Aravelo', 20, 'jaysonclyde92@gmail.com', 'Yes', 'Clydejayy2005', 'romancefan01_', '09178032820'),
(1003, 'Fae Ryn De Mundo', 21, 'faery9000@gmail.com', 'No', NULL, NULL, '09282169927'),
(1004, 'Mateo Torre', 65, 'smatteoa@gmail.com', 'Yes', 'mateoDeTorre62', 'HEREosandwich', '09158082839'),
(1005, 'Rain Mark Mangamba', 19, 'basa09sa10ulan@gmail.com', 'No', NULL, NULL, '09666702931'),
(1006, 'Luke Delos Santos', 37, 'luxtrous@gmail.com', 'Yes', 'skywalkLuke', 'popcornjedi99', '09289422626'),
(1007, 'Mari Chan Argon', 50, 'bigjom1201@gmail.com', 'Yes', 'meriChanmas25', 'christmasDay!', '09674206966'),
(1008, 'Micha Ordinance', 44, 'mikmart8080@gmail.com', 'No', NULL, NULL, '09639935269'),
(1009, 'Azriel Minguita', 64, 'azhudzon13@gmail.com', 'No', NULL, NULL, '09606538694'),
(1010, 'Miguel Dimagiba', 70, 'miguelitosayskrim@gmail.com', 'No', NULL, NULL, '09562948371'),

-- ========== TRANSACTION DATA (10 confirmed transactions from Excel) ==========
INSERT OR IGNORE INTO "transaction" (transaction_id, transaction_date, transaction_time, customer_no, seat_no, screening_id, movie_id, seat_type_id, reservation_type, admin_id, booking_fee, ticket_price, discount_type, discount_amount, payment_method, total_payment, status) VALUES
('2-M05-1001', '2026-06-04', '09:12', 1001, 'C5', 'SUN-5', 5, 2, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'E-Wallet', 405.00, 'CONFIRMED'),
('3-M10-1002', '2026-06-04', '10:47', 1002, 'D3', 'MON-9', 10, 3, 'Online', NULL, 20.00, 275.00, 'PWD', 59.00, 'Online Bank', 236.00, 'CONFIRMED'),
('1-M15-1003', '2026-06-04', '13:05', 1003, 'A2', 'MON-3', 15, 1, 'Onsite', '2102', 0.00, 590.00, 'N/A', 0.00, 'N/A', 590.00, 'CONFIRMED'),
('2-M02-1004', '2026-06-04', '14:33', 1004, 'E7', 'SUN-4', 2, 2, 'Online', NULL, 20.00, 385.00, 'Senior Citizen', 81.00, 'E-Wallet', 324.00, 'CONFIRMED'),
('2-M02-1005', '2026-06-04', '17:18', 1005, 'B8', 'SUN-4', 2, 2, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'E-Wallet', 405.00, 'CONFIRMED'),
('1-M03-1006', '2026-06-04', '18:42', 1006, 'A1', 'TUE-1', 3, 1, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'Online Bank', 610.00, 'CONFIRMED'),
('1-M03-1007', '2026-06-04', '20:11', 1007, 'B4', 'TUE-2', 3, 1, 'Onsite', '1207', 0.00, 590.00, 'N/A', 0.00, 'N/A', 590.00, 'CONFIRMED'),
('2-M12-1008', '2026-06-04', '21:56', 1008, 'F2', 'MON-6', 12, 2, 'Onsite', '1207', 0.00, 385.00, 'N/A', 0.00, 'N/A', 385.00, 'CONFIRMED'),
('3-M01-1009', '2026-06-04', '22:27', 1009, 'G6', 'SUN-6', 1, 3, 'Onsite', '2409', 0.00, 275.00, 'Senior Citizen', 55.00, 'N/A', 220.00, 'CONFIRMED'),
('1-M03-1010', '2026-06-04', '23:41', 1010, 'H3', 'SUN-3', 3, 1, 'Online', NULL, 20.00, 590.00, 'Senior Citizen', 122.00, 'Online Bank', 488.00, 'CONFIRMED');

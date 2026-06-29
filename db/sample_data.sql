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


-- ========== CUSTOMER DATA (first 50 from 296 total in Excel) ==========
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
(1011, 'Clara Villanueva', 28, 'claravilla28@gmail.com', 'Yes', 'ClaraV28', 'sunflower28!', '09162347890'),
(1012, 'Ramon Cruz', 33, 'ramoncruz33@gmail.com', 'Yes', 'RamonCruz33', 'ironman2024', '09284561234'),
(1013, 'Sofia Hernandez', 19, 'sofhern19@gmail.com', 'No', NULL, NULL, '09671234567'),
(1014, 'Daniel Reyes', 42, 'danreyes42@gmail.com', 'Yes', 'DanRey42', 'batman2024!', '09181239876'),
(1015, 'Angela Santos', 25, 'angelas25@gmail.com', 'Yes', 'Angie25', 'sparkle25star', '09293456781'),
(1016, 'Marco Gutierrez', 31, 'marcogut31@gmail.com', 'No', NULL, NULL, '09684561239'),
(1017, 'Teresa Delgado', 47, 'teresad47@gmail.com', 'Yes', 'TDelgado47', 'gardenlove47', '09194567823'),
(1018, 'Julian Navarro', 22, 'julnav22@gmail.com', 'Yes', 'JNav22', 'basketball22!', '09205678912'),
(1019, 'Bianca Flores', 36, 'bianflo36@gmail.com', 'No', NULL, NULL, '09616789023'),
(1020, 'Carlos Mendoza', 55, 'carlmend55@gmail.com', 'Yes', 'CarlM55', 'fishing55lake', '09127890123'),
(1021, 'Patricia Lim', 29, 'patlim29@gmail.com', 'Yes', 'PatLim29', 'bookworm29!', '09238901234'),
(1022, 'Eduardo Tan', 61, 'edutan61@gmail.com', 'No', NULL, NULL, '09649012345'),
(1023, 'Michelle Go', 24, 'michgo24@gmail.com', 'Yes', 'MichGo24', 'travel2024!!', '09150123456'),
(1024, 'Roberto Sy', 38, 'robsy38@gmail.com', 'Yes', 'RobSy38', 'guitar38rock', '09261234567'),
(1025, 'Vanessa Ong', 27, 'vanong27@gmail.com', 'No', NULL, NULL, '09672345678'),
(1026, 'Antonio Chua', 45, 'antchua45@gmail.com', 'Yes', 'AntChua45', 'chess45king!', '09183456789'),
(1027, 'Samantha Yu', 32, 'samyu32@gmail.com', 'Yes', 'SamYu32', 'painting32art', '09294567890'),
(1028, 'Gabriel Lao', 18, 'gablao18@gmail.com', 'No', NULL, NULL, '09605678901'),
(1029, 'Catherine Dee', 53, 'catdee53@gmail.com', 'Yes', 'CatDee53', 'cooking53chef', '09116789012'),
(1030, 'Alejandro Vega', 40, 'alevega40@gmail.com', 'Yes', 'AleVega40', 'soccer40goal!', '09227890123'),
(1031, 'Francesca Ramos', 23, 'franram23@gmail.com', 'No', NULL, NULL, '09638901234'),
(1032, 'Nathaniel Cruz', 35, 'natcruz35@gmail.com', 'Yes', 'NatCruz35', 'movies35fan!!', '09149012345'),
(1033, 'Isabella Torres', 26, 'isator26@gmail.com', 'Yes', 'IsaTor26', 'dance26star!!', '09250123456'),
(1034, 'Ricardo Santos', 48, 'ricsan48@gmail.com', 'No', NULL, NULL, '09661234567'),
(1035, 'Lorena Pascual', 30, 'lorpas30@gmail.com', 'Yes', 'LorPas30', 'baking30cake!', '09172345678'),
(1036, 'Vincent Aquino', 57, 'vinaq57@gmail.com', 'No', NULL, NULL, '09283456789'),
(1037, 'Diana Mercado', 21, 'diamer21@gmail.com', 'Yes', 'DiaMer21', 'kpop21forever', '09394567890'),
(1038, 'Fernando Lim', 66, 'ferlim66@gmail.com', 'No', NULL, NULL, '09505678901'),
(1039, 'Andrea Ocampo', 34, 'andoca34@gmail.com', 'Yes', 'AndOca34', 'garden34rose!', '09616789012'),
(1040, 'Jose Maria Tan', 41, 'jmtan41@gmail.com', 'Yes', 'JMTan41', 'history41buff', '09727890123'),
(1041, 'Beatrice Reyes', 28, 'bearey28@gmail.com', 'No', NULL, NULL, '09838901234'),
(1042, 'Christian Diaz', 39, 'chrdia39@gmail.com', 'Yes', 'ChrDia39', 'gaming39pro!!', '09949012345'),
(1043, 'Maria Luisa Go', 52, 'mlugo52@gmail.com', 'Yes', 'MLuGo52', 'flowers52love', '09150234567'),
(1044, 'Enrique Valle', 17, 'enrval17@gmail.com', 'No', NULL, NULL, '09261345678'),
(1045, 'Jasmine Tan', 25, 'jastan25@gmail.com', 'Yes', 'JasTan25', 'anime25otaku!', '09372456789'),
(1046, 'Alfonso Cruz', 60, 'alfcru60@gmail.com', 'No', NULL, NULL, '09483567890'),
(1047, 'Camille Santos', 22, 'camsan22@gmail.com', 'Yes', 'CamSan22', 'coffee22latte', '09594678901'),
(1048, 'Ricardo Bautista', 46, 'ricbau46@gmail.com', 'Yes', 'RicBau46', 'fishing46bass', '09605789012'),
(1049, 'Stella Morales', 31, 'stemor31@gmail.com', 'No', NULL, NULL, '09716890123'),
(1050, 'Dominic Aguilar', 37, 'domag37@gmail.com', 'Yes', 'DomAg37', 'boxing37champ', '09827901234');


-- ========== TRANSACTION DATA (10 confirmed transactions from Excel) ==========
INSERT OR IGNORE INTO "transaction" (transaction_id, transaction_date, transaction_time, customer_no, seat_no, screening_id, movie_id, seat_type_id, reservation_type, admin_id, booking_fee, ticket_price, discount_type, discount_amount, payment_method, total_payment, status) VALUES
('1-M03-1001', '2026-06-04', '00:57', 1001, 'B7', 'SUN-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('2-M08-1002', '2026-06-04', '01:42', 1002, 'A3', 'MON-8', 2, 8, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'Online Bank', 405.00, 'CONFIRMED'),
('3-M03-1003', '2026-06-04', '01:53', 1003, 'F10', 'TUE-3', 1, 3, 'Online', 2102, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('4-M03-1004', '2026-06-04', '02:22', 1004, 'C2', 'SUN-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'Senior Citizen', 122.00, 'E-Wallet', 488.00, 'CONFIRMED'),
('5-M10-1005', '2026-06-04', '02:45', 1005, 'H5', 'MON-10', 3, 10, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'Online Bank', 295.00, 'REFUNDED'),
('6-M01-1006', '2026-06-04', '03:29', 1006, 'E8', 'TUE-1', 3, 1, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'E-Wallet', 295.00, 'CONFIRMED'),
('7-M05-1007', '2026-06-04', '03:55', 1007, 'A1', 'SUN-5', 2, 5, 'Online', 1207, 20.00, 385.00, 'N/A', 0.00, 'Online Bank', 405.00, 'CONFIRMED'),
('8-M03-1008', '2026-06-04', '04:09', 1008, 'K9', 'MON-3', 1, 3, 'Online', 1207, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('9-M07-1009', '2026-06-04', '04:33', 1009, 'D4', 'TUE-7', 3, 7, 'Online', 2409, 20.00, 275.00, 'Senior Citizen', 59.00, 'E-Wallet', 236.00, 'CONFIRMED'),
('10-M01-1010', '2026-06-04', '04:59', 1010, 'G6', 'SUN-1', 3, 1, 'Online', NULL, 20.00, 275.00, 'Senior Citizen', 59.00, 'Online Bank', 236.00, 'CONFIRMED'),
('11-M03-1011', '2026-06-04', '05:21', 1011, 'I10', 'MON-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('12-M05-1012', '2026-06-04', '05:47', 1012, 'B2', 'TUE-5', 2, 5, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'Online Bank', 405.00, 'CONFIRMED'),
('13-M03-1013', '2026-06-04', '06:07', 1013, 'A7', 'SUN-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('14-M10-1014', '2026-06-04', '06:19', 1014, 'F3', 'MON-10', 3, 10, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'E-Wallet', 295.00, 'CONFIRMED'),
('15-M03-1015', '2026-06-04', '06:47', 1015, 'C9', 'TUE-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'Online Bank', 610.00, 'CONFIRMED'),
('16-M01-1016', '2026-06-04', '07:21', 1016, 'H1', 'SUN-1', 3, 1, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'E-Wallet', 295.00, 'REFUNDED'),
('17-M08-1017', '2026-06-04', '07:39', 1017, 'E5', 'MON-8', 2, 8, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'Online Bank', 405.00, 'CONFIRMED'),
('18-M01-1018', '2026-06-04', '08:03', 1018, 'K2', 'TUE-1', 3, 1, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'E-Wallet', 295.00, 'CONFIRMED'),
('19-M02-1019', '2026-06-04', '08:55', 1019, 'D7', 'SUN-2', 2, 2, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'E-Wallet', 405.00, 'CONFIRMED'),
('20-M12-1020', '2026-06-04', '10:12', 1020, 'G10', 'MON-12', 2, 12, 'Onsite', 1207, 0.00, 385.00, 'N/A', 0.00, 'Cash', 385.00, 'CONFIRMED'),
('21-M15-1021', '2026-06-04', '10:47', 1021, 'I4', 'TUE-15', 1, 15, 'Onsite', 1230, 0.00, 590.00, 'N/A', 0.00, 'Cash', 590.00, 'CONFIRMED'),
('22-M07-1022', '2026-06-04', '11:33', 1022, 'B9', 'SUN-7', 3, 7, 'Onsite', 1207, 0.00, 275.00, 'N/A', 0.00, 'E-Wallet', 275.00, 'CONFIRMED'),
('23-M03-1023', '2026-06-04', '11:58', 1023, 'A10', 'MON-3', 1, 3, 'Onsite', 1230, 0.00, 590.00, 'N/A', 0.00, 'E-Wallet', 590.00, 'CONFIRMED'),
('24-M07-1024', '2026-06-04', '12:49', 1024, 'F6', 'TUE-7', 3, 7, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'Online Bank', 295.00, 'CONFIRMED'),
('25-M07-1025', '2026-06-04', '13:25', 1025, 'C5', 'SUN-7', 3, 7, 'Onsite', 1207, 0.00, 275.00, 'N/A', 0.00, 'Cash', 275.00, 'CONFIRMED'),
('26-M12-1026', '2026-06-04', '13:44', 1026, 'H8', 'MON-12', 2, 12, 'Onsite', 1230, 0.00, 385.00, 'N/A', 0.00, 'E-Wallet', 385.00, 'CONFIRMED'),
('27-M02-1027', '2026-06-04', '14:29', 1027, 'E2', 'TUE-2', 2, 2, 'Onsite', 1207, 0.00, 385.00, 'N/A', 0.00, 'E-Wallet', 385.00, 'CONFIRMED'),
('28-M03-1028', '2026-06-04', '14:44', 1028, 'K7', 'SUN-3', 1, 3, 'Onsite', 1230, 0.00, 590.00, 'N/A', 0.00, 'E-Wallet', 590.00, 'CONFIRMED'),
('29-M13-1029', '2026-06-04', '15:37', 1029, 'D1', 'MON-13', 3, 13, 'Onsite', 2409, 0.00, 275.00, 'N/A', 0.00, 'Cash', 275.00, 'CONFIRMED'),
('30-M03-1030', '2026-06-04', '15:58', 1030, 'G3', 'TUE-3', 1, 3, 'Onsite', 1231, 0.00, 590.00, 'N/A', 0.00, 'E-Wallet', 590.00, 'CONFIRMED'),
('31-M03-1031', '2026-06-04', '17:18', 1031, 'I7', 'SUN-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'REFUNDED'),
('32-M13-1032', '2026-06-04', '17:36', 1032, 'B4', 'MON-13', 3, 13, 'Onsite', 1231, 0.00, 275.00, 'N/A', 0.00, 'E-Wallet', 275.00, 'CONFIRMED'),
('33-M02-1033', '2026-06-04', '18:42', 1033, 'A6', 'TUE-2', 2, 2, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'Online Bank', 405.00, 'CONFIRMED'),
('34-M03-1034', '2026-06-04', '18:52', 1034, 'F9', 'SUN-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('35-M15-1035', '2026-06-04', '19:11', 1035, 'C1', 'MON-15', 1, 15, 'Onsite', 1234, 0.00, 590.00, 'N/A', 0.00, 'Cash', 590.00, 'CONFIRMED'),
('36-M05-1036', '2026-06-04', '19:45', 1036, 'H10', 'TUE-5', 2, 5, 'Onsite', 1231, 0.00, 385.00, 'N/A', 0.00, 'Cash', 385.00, 'CONFIRMED'),
('37-M05-1037', '2026-06-04', '20:28', 1037, 'E4', 'SUN-5', 2, 5, 'Onsite', 1234, 0.00, 385.00, 'N/A', 0.00, 'Cash', 385.00, 'CONFIRMED'),
('38-M08-1038', '2026-06-04', '20:56', 1038, 'K5', 'MON-8', 2, 8, 'Onsite', 1231, 0.00, 385.00, 'N/A', 0.00, 'Cash', 385.00, 'CONFIRMED'),
('39-M01-1039', '2026-06-04', '21:27', 1039, 'D8', 'TUE-1', 3, 1, 'Onsite', 1234, 0.00, 275.00, 'N/A', 0.00, 'Cash', 275.00, 'CONFIRMED'),
('40-M01-1040', '2026-06-04', '21:33', 1040, 'G8', 'SUN-1', 3, 1, 'Onsite', 1231, 0.00, 275.00, 'N/A', 0.00, 'Cash', 275.00, 'CONFIRMED'),
('41-M03-1041', '2026-06-05', '03:17', 1041, 'I2', 'SUN-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('42-M08-1042', '2026-06-05', '03:33', 1042, 'B10', 'MON-8', 2, 8, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'Online Bank', 405.00, 'CONFIRMED'),
('43-M03-1043', '2026-06-05', '03:49', 1043, 'A2', 'TUE-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('44-M03-1044', '2026-06-05', '04:05', 1044, 'F5', 'SUN-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'Online Bank', 610.00, 'CONFIRMED'),
('45-M10-1045', '2026-06-05', '04:21', 1045, 'C7', 'MON-10', 3, 10, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'E-Wallet', 295.00, 'CONFIRMED'),
('46-M01-1046', '2026-06-05', '04:37', 1046, 'H3', 'TUE-1', 3, 1, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'E-Wallet', 295.00, 'CONFIRMED'),
('47-M05-1047', '2026-06-05', '04:53', 1047, 'E9', 'SUN-5', 2, 5, 'Online', NULL, 20.00, 385.00, 'N/A', 0.00, 'Online Bank', 405.00, 'REFUNDED'),
('48-M03-1048', '2026-06-05', '05:09', 1048, 'K1', 'MON-3', 1, 3, 'Online', NULL, 20.00, 590.00, 'N/A', 0.00, 'E-Wallet', 610.00, 'CONFIRMED'),
('49-M07-1049', '2026-06-05', '05:25', 1049, 'D3', 'TUE-7', 3, 7, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'Online Bank', 295.00, 'CONFIRMED'),
('50-M01-1050', '2026-06-05', '05:41', 1050, 'G9', 'SUN-1', 3, 1, 'Online', NULL, 20.00, 275.00, 'N/A', 0.00, 'E-Wallet', 295.00, 'CONFIRMED'),

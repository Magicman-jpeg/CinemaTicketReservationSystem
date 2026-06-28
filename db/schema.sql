-- Cinema Ticket Reservation System - Database Schema
-- COMP 013 OOP Final Project - Group 2
-- Based on: EDITED GROUP 2_cinema_hall_ticket_sales.xlsx
-- SQLite Database

PRAGMA foreign_keys = ON;

-- ========== LOOKUP TABLES ==========

-- Movie Genre lookup
CREATE TABLE IF NOT EXISTS movie_genre (
    genre_id INTEGER PRIMARY KEY,
    movie_genre TEXT NOT NULL
);

-- Movie Status lookup
CREATE TABLE IF NOT EXISTS movie_status (
    status_id INTEGER PRIMARY KEY,
    movie_status TEXT NOT NULL
);

-- Age Rating lookup
CREATE TABLE IF NOT EXISTS age_rating (
    age_rate_id INTEGER PRIMARY KEY,
    movie_age_rating TEXT NOT NULL
);

-- Seat Type lookup (with pricing)
CREATE TABLE IF NOT EXISTS seat_type (
    seat_type_id INTEGER PRIMARY KEY,
    seat_type TEXT NOT NULL,
    seat_description TEXT NOT NULL,
    ticket_price REAL NOT NULL
);

-- ========== MAIN TABLES ==========

-- Movie table
CREATE TABLE IF NOT EXISTS movie (
    movie_id INTEGER PRIMARY KEY,
    movie_title TEXT NOT NULL,
    genre_id INTEGER NOT NULL,
    movie_duration TEXT NOT NULL,
    duration_code INTEGER NOT NULL,
    release_date TEXT NOT NULL,
    status_id INTEGER NOT NULL,
    age_rate_id INTEGER NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES movie_genre(genre_id),
    FOREIGN KEY (status_id) REFERENCES movie_status(status_id),
    FOREIGN KEY (age_rate_id) REFERENCES age_rating(age_rate_id)
);

-- Admin table
CREATE TABLE IF NOT EXISTS admin (
    admin_id INTEGER PRIMARY KEY,
    role TEXT NOT NULL,
    admin_name TEXT NOT NULL,
    admin_username TEXT UNIQUE NOT NULL,
    admin_pass TEXT NOT NULL
);

-- Customer table
CREATE TABLE IF NOT EXISTS customer (
    customer_no INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER NOT NULL,
    email_address TEXT NOT NULL,
    app_user TEXT NOT NULL DEFAULT 'No',
    customer_username TEXT,
    customer_pass TEXT,
    mobile_no TEXT NOT NULL
);

-- Cinema Seat table (10 rows A-J, 10 columns each = 100 seats per cinema)
CREATE TABLE IF NOT EXISTS cinema_seat (
    seat_no TEXT PRIMARY KEY,
    row TEXT NOT NULL,
    col INTEGER NOT NULL
);

-- Screenings table
CREATE TABLE IF NOT EXISTS screenings (
    screening_id TEXT PRIMARY KEY,
    screening_day TEXT NOT NULL,
    screening_date TEXT NOT NULL,
    time_slot TEXT NOT NULL,
    seat_type_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    cinema_no INTEGER NOT NULL,
    FOREIGN KEY (seat_type_id) REFERENCES seat_type(seat_type_id),
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id)
);

-- Transaction table
CREATE TABLE IF NOT EXISTS "transaction" (
    transaction_id TEXT PRIMARY KEY,
    transaction_date TEXT NOT NULL,
    transaction_time TEXT NOT NULL,
    customer_no INTEGER NOT NULL,
    seat_no TEXT,
    screening_id TEXT NOT NULL,
    movie_id INTEGER NOT NULL,
    seat_type_id INTEGER NOT NULL,
    reservation_type TEXT NOT NULL,
    admin_id TEXT,
    booking_fee REAL NOT NULL DEFAULT 0,
    ticket_price REAL NOT NULL,
    discount_type TEXT DEFAULT 'N/A',
    discount_amount REAL NOT NULL DEFAULT 0,
    payment_method TEXT,
    total_payment REAL NOT NULL DEFAULT 0,
    status TEXT NOT NULL DEFAULT 'CONFIRMED',
    FOREIGN KEY (customer_no) REFERENCES customer(customer_no),
    FOREIGN KEY (screening_id) REFERENCES screenings(screening_id),
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
    FOREIGN KEY (seat_type_id) REFERENCES seat_type(seat_type_id)
);

-- ========== INDEXES ==========
CREATE INDEX IF NOT EXISTS idx_movie_genre ON movie(genre_id);
CREATE INDEX IF NOT EXISTS idx_movie_status ON movie(status_id);
CREATE INDEX IF NOT EXISTS idx_screening_movie ON screenings(movie_id);
CREATE INDEX IF NOT EXISTS idx_screening_seat_type ON screenings(seat_type_id);
CREATE INDEX IF NOT EXISTS idx_transaction_customer ON "transaction"(customer_no);
CREATE INDEX IF NOT EXISTS idx_transaction_screening ON "transaction"(screening_id);

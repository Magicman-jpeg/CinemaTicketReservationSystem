-- Cinema Ticket Reservation System - Database Schema
-- COMP 013 OOP Final Project - Group 2
-- SQLite Database

PRAGMA foreign_keys = ON;

-- Movie table
CREATE TABLE IF NOT EXISTS movie (
    movie_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    genre TEXT NOT NULL,
    duration_minutes INTEGER NOT NULL,
    rating TEXT NOT NULL,
    director TEXT NOT NULL,
    release_date TEXT NOT NULL
);

-- Admin table
CREATE TABLE IF NOT EXISTS admin (
    admin_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    full_name TEXT NOT NULL,
    email TEXT NOT NULL,
    department TEXT NOT NULL DEFAULT 'Operations',
    access_level TEXT NOT NULL DEFAULT 'FULL'
);

-- Customer table
CREATE TABLE IF NOT EXISTS customer (
    customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    full_name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone_number TEXT,
    membership_type TEXT NOT NULL DEFAULT 'REGULAR',
    registration_date TEXT NOT NULL
);

-- Screenings table
CREATE TABLE IF NOT EXISTS screenings (
    screening_id INTEGER PRIMARY KEY AUTOINCREMENT,
    movie_id INTEGER NOT NULL,
    screen_date TEXT NOT NULL,
    screen_time TEXT NOT NULL,
    hall_number INTEGER NOT NULL,
    ticket_price REAL NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON DELETE CASCADE
);

-- Cinema Seat table
CREATE TABLE IF NOT EXISTS cinema_seat (
    seat_id INTEGER PRIMARY KEY AUTOINCREMENT,
    screening_id INTEGER NOT NULL,
    seat_row TEXT NOT NULL,
    seat_column INTEGER NOT NULL,
    status TEXT NOT NULL DEFAULT 'AVAILABLE',
    FOREIGN KEY (screening_id) REFERENCES screenings(screening_id) ON DELETE CASCADE,
    UNIQUE(screening_id, seat_row, seat_column)
);

-- Transaction table
CREATE TABLE IF NOT EXISTS "transaction" (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER NOT NULL,
    screening_id INTEGER NOT NULL,
    seat_id INTEGER NOT NULL,
    seat_label TEXT NOT NULL,
    transaction_date TEXT NOT NULL,
    transaction_time TEXT NOT NULL,
    amount_paid REAL NOT NULL,
    payment_method TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (screening_id) REFERENCES screenings(screening_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES cinema_seat(seat_id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_screening_movie ON screenings(movie_id);
CREATE INDEX IF NOT EXISTS idx_seat_screening ON cinema_seat(screening_id);
CREATE INDEX IF NOT EXISTS idx_transaction_customer ON "transaction"(customer_id);
CREATE INDEX IF NOT EXISTS idx_transaction_screening ON "transaction"(screening_id);

-- ============================================
-- Cinema Ticket Reservation System Database Schema (SQLite)
-- Original File: GROUP 2_cinema_hall_ticket_sales.xlsx
-- Author: Group 2 (BSIT 2-3)
-- Course: COMP 009 Object Oriented Programming
-- ============================================

PRAGMA foreign_keys = ON;

-- ================= LOOKUP TABLES =================
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


-- ================= MAIN TABLES =================
-- Movie table
CREATE TABLE IF NOT EXISTS movie (
    movie_id INTEGER PRIMARY KEY,
    movie_title TEXT NOT NULL, 
    genre_id INTEGER NOT NULL, -- FK to movie_genre
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
    role TEXT NOT NULL, -- Role (Manager, Box Office Staff)
    admin_name TEXT NOT NULL,
    admin_username TEXT UNIQUE NOT NULL, -- Admin's login username
    admin_pass TEXT NOT NULL -- Admin's password
);

-- Customer table
CREATE TABLE IF NOT EXISTS customer (
    customer_no INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER NOT NULL, -- Age for movie age rating compliance
    email_address TEXT NOT NULL,
    app_user TEXT NOT NULL DEFAULT 'No', -- Indicates if the customer is an app user
    customer_username TEXT, -- Customer's login username (NULL if customer is not an app user)
    customer_pass TEXT, -- Customer's password (NULL if customer is not an app user)
    mobile_no TEXT NOT NULL
);

-- Cinema Seat table (10 rows A-J, 10 columns each = 100 seats per cinema)
CREATE TABLE IF NOT EXISTS cinema_seat (
    seat_no TEXT PRIMARY KEY, -- Seat identifier (e.g., A1, B5)
    row TEXT NOT NULL, -- Row letter (A–J)
    col INTEGER NOT NULL -- Column number (1–10)
);

-- Screenings table
CREATE TABLE IF NOT EXISTS screenings (
    screening_id TEXT PRIMARY KEY,
    screening_day TEXT NOT NULL, -- Day code (SUN, MON, TUE)
    screening_date TEXT NOT NULL,
    time_slot TEXT NOT NULL,
    seat_type_id INTEGER NOT NULL, -- FK to seat_type
    movie_id INTEGER NOT NULL, -- FK to movie
    cinema_no INTEGER NOT NULL, -- Screen number
    FOREIGN KEY (seat_type_id) REFERENCES seat_type(seat_type_id),
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id)
);

-- Transaction table
CREATE TABLE IF NOT EXISTS "transaction" (
    transaction_id TEXT PRIMARY KEY,
    transaction_date TEXT NOT NULL,
    transaction_time TEXT NOT NULL,
    customer_no INTEGER NOT NULL, -- FK to customer
    seat_no TEXT, -- FK to cinema_seat
    screening_id TEXT NOT NULL, -- FK to screenings
    movie_id INTEGER NOT NULL, -- FK to movie 
    seat_type_id INTEGER NOT NULL, -- FK to seat_type
    reservation_type TEXT NOT NULL, -- Indicated if transaction is made Online / Onsite
    admin_id TEXT, -- FK to admin (NULL if transaction is Online)
    booking_fee REAL NOT NULL DEFAULT 0, -- Extra fee for online booking
    ticket_price REAL NOT NULL,
    discount_type TEXT DEFAULT 'N/A', -- Discount type (Senior Citizen, PWD)
    discount_amount REAL NOT NULL DEFAULT 0,
    payment_method TEXT, -- Payment method (Cash, E-Wallet, Bank)
    total_payment REAL NOT NULL DEFAULT 0, -- Total payment amount to be made after discount/extra fee
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

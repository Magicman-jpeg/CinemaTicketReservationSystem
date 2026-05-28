PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS [transaction];
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS cinema_seat;
DROP TABLE IF EXISTS screenings;
DROP TABLE IF EXISTS movie;

CREATE TABLE movie (
    Movie_ID INTEGER PRIMARY KEY,
    Movie_Title TEXT NOT NULL,
    Genre_ID INTEGER,
    Movie_Duration TEXT,
    Duration_Code INTEGER,
    Release_Date TEXT,
    Age_Rate_ID INTEGER,
    Status_ID INTEGER
);

CREATE TABLE screenings (
    Screening_ID TEXT PRIMARY KEY,
    Screening_Day TEXT,
    Screening_Date TEXT,
    Time_Slot TEXT,
    Cinema_No INTEGER,
    Seat_Type_ID INTEGER,
    Movie_ID INTEGER NOT NULL,
    FOREIGN KEY (Movie_ID) REFERENCES movie (Movie_ID)
);

CREATE TABLE cinema_seat (
    Seat_No TEXT PRIMARY KEY,
    [Row] TEXT,
    [Column] INTEGER,
    Avail_Row TEXT,
    Avail_Col INTEGER
);

CREATE TABLE admin (
    Admin_ID INTEGER PRIMARY KEY,
    Role TEXT,
    Admin_Name TEXT,
    Admin_Username TEXT NOT NULL,
    [Admin _Pass] TEXT NOT NULL
);

CREATE TABLE customer (
    Customer_No INTEGER PRIMARY KEY,
    Name TEXT NOT NULL,
    Age INTEGER,
    PWD TEXT,
    Customer_Type TEXT,
    Mobile_No TEXT,
    Email_Address TEXT,
    [App User] TEXT,
    Customer_Username TEXT,
    Customer_Pass TEXT
);

CREATE TABLE [transaction] (
    Transaction_ID TEXT PRIMARY KEY,
    Transaction_Date TEXT,
    Transaction_Time TEXT,
    Customer_No INTEGER,
    Seat_No TEXT,
    Screening_ID TEXT,
    Seat_Type_ID INTEGER,
    Movie_ID INTEGER,
    Reservation_Type TEXT,
    Admin_ID TEXT,
    Booking_Fee REAL,
    Ticket_Price REAL,
    Discount_Type TEXT,
    Discount_Amount REAL,
    Payment_Method TEXT,
    Total_Payment REAL,
    Payment_Status TEXT,
    FOREIGN KEY (Customer_No) REFERENCES customer (Customer_No),
    FOREIGN KEY (Seat_No) REFERENCES cinema_seat (Seat_No),
    FOREIGN KEY (Screening_ID) REFERENCES screenings (Screening_ID),
    FOREIGN KEY (Movie_ID) REFERENCES movie (Movie_ID)
);

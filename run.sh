#!/bin/bash
# Cinema Ticket Reservation System - Build and Run Script
# COMP 013 - OOP Final Project - Group 2

echo "=============================================="
echo "  Cinema Ticket Reservation System"
echo "  COMP 013 - OOP Final Project"
echo "=============================================="
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java not found. Please install JDK 17+."
    exit 1
fi

# Check sqlite3
if ! command -v sqlite3 &> /dev/null; then
    echo "[ERROR] sqlite3 not found. Please install SQLite3."
    exit 1
fi

echo "[*] Java version: $(java -version 2>&1 | head -1)"
echo "[*] SQLite version: $(sqlite3 --version)"
echo ""

# Compile
echo "[*] Compiling Java sources..."
rm -rf out
javac -d out $(find src -name "*.java")
if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi
echo "[+] Compilation successful."

# Initialize database if needed
if [ ! -f "db/cinema.db" ]; then
    echo "[*] Initializing database..."
    sqlite3 db/cinema.db < db/schema.sql
    sqlite3 db/cinema.db < db/sample_data.sql
    echo "[+] Database initialized with sample data."
else
    echo "[*] Database already exists."
fi

echo ""
echo "[*] Starting application..."
echo "=============================================="
echo ""

# Run
java -cp out com.cinema.Main

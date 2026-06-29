#!/bin/bash
echo "=============================================="
echo "  Cinema Ticket Reservation System"
echo "  COMP 009 - OOP Final Project - Group 1"
echo "=============================================="
echo ""

echo "[*] Compiling..."
rm -rf out
javac -d out src/*.java
if [ $? -ne 0 ]; then echo "[ERROR] Compilation failed!"; exit 1; fi
echo "[+] Done."

if [ ! -f "db/cinema.db" ]; then
    echo "[*] Initializing database..."
    sqlite3 db/cinema.db < db/schema.sql
    sqlite3 db/cinema.db < db/sample_data.sql
    echo "[+] Database ready."
fi

echo ""
java -cp out Main

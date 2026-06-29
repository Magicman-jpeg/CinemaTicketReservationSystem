@echo off
echo ==============================================
echo   Cinema Ticket Reservation System
echo   COMP 009 - OOP Final Project - Group 1
echo ==============================================
echo.

REM Check Java
where javac >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java JDK not found. Please install JDK 17+ and add to PATH.
    pause
    exit /b 1
)

echo [*] Compiling Java sources...
if exist out rmdir /s /q out
javac -d out src\*.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)
echo [+] Compilation successful.

REM Initialize database if needed
if not exist "db\cinema.db" (
    echo [*] Initializing database...
    where sqlite3 >nul 2>nul
    if %ERRORLEVEL% NEQ 0 (
        echo [i] sqlite3 not found on PATH.
        echo [i] The app will try to initialize automatically.
    ) else (
        sqlite3 db\cinema.db < db\schema.sql
        sqlite3 db\cinema.db < db\sample_data.sql
        echo [+] Database initialized with sample data.
    )
) else (
    echo [*] Database already exists.
)

echo.
echo [*] Starting application...
echo ==============================================
echo.

java -cp out Main
pause

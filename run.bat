@echo off
echo ==============================================
echo   Cinema Ticket Reservation System
echo   COMP 013 - OOP Final Project - Group 2
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
javac -d out -sourcepath src src\com\cinema\Main.java
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
        echo [i] sqlite3 not found - database will be created on first run.
        echo [i] The app will auto-initialize if schema.sql exists.
    ) else (
        sqlite3 db\cinema.db < db\schema.sql
        sqlite3 db\cinema.db < db\sample_data.sql
        echo [+] Database initialized.
    )
) else (
    echo [*] Database already exists.
)

echo.
echo [*] Starting application...
echo ==============================================
echo.

java -cp out com.cinema.Main
pause

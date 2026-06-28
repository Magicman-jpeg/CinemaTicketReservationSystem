/**
 * MODULE 3: AddRecordModule
 * Allows users to add new records to the database.
 * Validates required fields before saving.
 */
public class AddRecordModule {

    public static void show() {
        System.out.println("\n  -------- ADD RECORD --------");
        System.out.println("  [1] Add Movie");
        System.out.println("  [2] Add Customer");
        System.out.println("  [3] Add Screening");
        System.out.println("  [4] Back");

        int choice = InputValidator.getInt("Choose", 1, 4);
        switch (choice) {
            case 1 -> addMovie();
            case 2 -> addCustomer();
            case 3 -> addScreening();
        }
    }

    private static void addMovie() {
        System.out.println("\n  -- Add New Movie --");
        String title = InputValidator.getString("Movie Title");
        System.out.println("  Genres: 1=Romance 2=Action 3=Horror 4=Comedy 5=Thriller 6=Fantasy 7=Adventure 8=Drama 9=Sci-Fi");
        int genre = InputValidator.getInt("Genre ID", 1, 9);
        String duration = InputValidator.getString("Duration (e.g. '2 hrs 07 mins')");
        int durCode = InputValidator.getInt("Duration in minutes");
        String release = InputValidator.getString("Release Date (YYYY-MM-DD)");
        System.out.println("  Status: 1=Upcoming 2=Showing 3=Post-Screening");
        int status = InputValidator.getInt("Status ID", 1, 3);
        System.out.println("  Rating: 1=PG 2=R13 3=R16");
        int rating = InputValidator.getInt("Age Rating ID", 1, 3);

        boolean success = DatabaseHelper.execute(String.format(
            "INSERT INTO movie (movie_title, genre_id, movie_duration, duration_code, release_date, status_id, age_rate_id) " +
            "VALUES ('%s', %d, '%s', %d, '%s', %d, %d)",
            DatabaseHelper.escape(title), genre, DatabaseHelper.escape(duration),
            durCode, DatabaseHelper.escape(release), status, rating));

        if (success) System.out.println("  [+] Movie added successfully!");
        else System.out.println("  [!] Failed to add movie.");
        InputValidator.pause();
    }

    private static void addCustomer() {
        System.out.println("\n  -- Add New Customer --");
        String name = InputValidator.getString("Full Name");
        int age = InputValidator.getInt("Age");

        String email;
        while (true) {
            email = InputValidator.getString("Email");
            if (InputValidator.isValidEmail(email)) break;
            ExceptionHandler.handleInvalidInput("Email", "Must contain @ and .");
        }

        String phone;
        while (true) {
            phone = InputValidator.getString("Mobile No (09XXXXXXXXX)");
            if (InputValidator.isValidPhone(phone)) break;
            ExceptionHandler.handleInvalidInput("Phone", "Must be 11 digits starting with 09");
        }

        String appUser = InputValidator.confirm("Is this an app user?") ? "Yes" : "No";
        String username = null, password = null;
        if ("Yes".equals(appUser)) {
            username = InputValidator.getString("Username");
            password = InputValidator.getString("Password");
        }

        boolean success = DatabaseHelper.execute(String.format(
            "INSERT INTO customer (name, age, email_address, app_user, customer_username, customer_pass, mobile_no) " +
            "VALUES ('%s', %d, '%s', '%s', %s, %s, '%s')",
            DatabaseHelper.escape(name), age, DatabaseHelper.escape(email), appUser,
            username != null ? "'" + DatabaseHelper.escape(username) + "'" : "NULL",
            password != null ? "'" + DatabaseHelper.escape(password) + "'" : "NULL",
            DatabaseHelper.escape(phone)));

        if (success) System.out.println("  [+] Customer added successfully!");
        else System.out.println("  [!] Failed to add customer.");
        InputValidator.pause();
    }

    private static void addScreening() {
        System.out.println("\n  -- Add New Screening --");
        String id = InputValidator.getString("Screening ID (e.g. WED-1)");
        String day = InputValidator.getString("Day (SUN/MON/TUE/WED/THU/FRI/SAT)").toUpperCase();
        String date = InputValidator.getString("Date (YYYY-MM-DD)");
        String time = InputValidator.getString("Time (HH:MM)");
        System.out.println("  Seat Types: 1=VIP(PHP590) 2=Premium(PHP385) 3=Regular(PHP275)");
        int seatType = InputValidator.getInt("Seat Type ID", 1, 3);
        int movieId = InputValidator.getInt("Movie ID");
        int cinema = InputValidator.getInt("Cinema No", 1, 5);

        boolean success = DatabaseHelper.execute(String.format(
            "INSERT INTO screenings (screening_id, screening_day, screening_date, time_slot, seat_type_id, movie_id, cinema_no) " +
            "VALUES ('%s', '%s', '%s', '%s', %d, %d, %d)",
            DatabaseHelper.escape(id), DatabaseHelper.escape(day), DatabaseHelper.escape(date),
            DatabaseHelper.escape(time), seatType, movieId, cinema));

        if (success) System.out.println("  [+] Screening added successfully!");
        else System.out.println("  [!] Failed to add screening.");
        InputValidator.pause();
    }
}

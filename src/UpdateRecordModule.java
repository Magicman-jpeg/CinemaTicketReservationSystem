import java.util.*;

/**
 * MODULE 6: UpdateRecordModule
 * Allows users to update/edit existing records.
 * Confirms that the record exists before updating.
 */
public class UpdateRecordModule {

    public static void show() {
        System.out.println("\n  -------- UPDATE RECORD --------");
        System.out.println("  [1] Update Movie");
        System.out.println("  [2] Update Customer");
        System.out.println("  [3] Update Screening");
        System.out.println("  [4] Back");

        int choice = InputValidator.getInt("Choose", 1, 4);
        switch (choice) {
            case 1 -> updateMovie();
            case 2 -> updateCustomer();
            case 3 -> updateScreening();
        }
    }

    private static void updateMovie() {
        int id = InputValidator.getInt("Enter Movie ID to update");

        // Verify record exists
        var results = DatabaseHelper.query(String.format(
            "SELECT * FROM movie WHERE movie_id = %d", id));
        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Movie", String.valueOf(id));
            InputValidator.pause();
            return;
        }

        var movie = results.get(0);
        System.out.println("  Current Title: " + movie.get("movie_title"));
        System.out.println("  (Press Enter to keep current value)\n");

        String title = InputValidator.getOptionalString("New Title");
        String status = InputValidator.getOptionalString("New Status (1=Upcoming, 2=Showing, 3=Post-Screening)");

        StringBuilder sql = new StringBuilder("UPDATE movie SET ");
        boolean hasUpdate = false;
        if (!title.isEmpty()) {
            sql.append("movie_title='").append(DatabaseHelper.escape(title)).append("'");
            hasUpdate = true;
        }
        if (!status.isEmpty()) {
            if (hasUpdate) sql.append(", ");
            sql.append("status_id=").append(status);
            hasUpdate = true;
        }
        sql.append(" WHERE movie_id=").append(id);

        if (hasUpdate) {
            DatabaseHelper.execute(sql.toString());
            System.out.println("  [+] Movie updated successfully!");
        } else {
            System.out.println("  [i] No changes made.");
        }
        InputValidator.pause();
    }

    private static void updateCustomer() {
        int no = InputValidator.getInt("Enter Customer No to update");

        var results = DatabaseHelper.query(String.format(
            "SELECT * FROM customer WHERE customer_no = %d", no));
        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Customer", String.valueOf(no));
            InputValidator.pause();
            return;
        }

        var cust = results.get(0);
        System.out.println("  Current Name: " + cust.get("name"));
        System.out.println("  Current Email: " + cust.get("email_address"));
        System.out.println("  (Press Enter to keep current value)\n");

        String name = InputValidator.getOptionalString("New Name");
        String email = InputValidator.getOptionalString("New Email");
        String phone = InputValidator.getOptionalString("New Mobile No");

        StringBuilder sql = new StringBuilder("UPDATE customer SET ");
        boolean hasUpdate = false;
        if (!name.isEmpty()) {
            sql.append("name='").append(DatabaseHelper.escape(name)).append("'");
            hasUpdate = true;
        }
        if (!email.isEmpty()) {
            if (hasUpdate) sql.append(", ");
            sql.append("email_address='").append(DatabaseHelper.escape(email)).append("'");
            hasUpdate = true;
        }
        if (!phone.isEmpty()) {
            if (hasUpdate) sql.append(", ");
            sql.append("mobile_no='").append(DatabaseHelper.escape(phone)).append("'");
            hasUpdate = true;
        }
        sql.append(" WHERE customer_no=").append(no);

        if (hasUpdate) {
            DatabaseHelper.execute(sql.toString());
            System.out.println("  [+] Customer updated successfully!");
        } else {
            System.out.println("  [i] No changes made.");
        }
        InputValidator.pause();
    }

    private static void updateScreening() {
        String id = InputValidator.getString("Enter Screening ID to update");

        var results = DatabaseHelper.query(String.format(
            "SELECT * FROM screenings WHERE screening_id = '%s'",
            DatabaseHelper.escape(id)));
        if (results.isEmpty()) {
            ExceptionHandler.handleNotFound("Screening", id);
            InputValidator.pause();
            return;
        }

        System.out.println("  Current Time: " + results.get(0).get("time_slot"));
        String time = InputValidator.getOptionalString("New Time (HH:MM)");

        if (!time.isEmpty()) {
            DatabaseHelper.execute(String.format(
                "UPDATE screenings SET time_slot='%s' WHERE screening_id='%s'",
                DatabaseHelper.escape(time), DatabaseHelper.escape(id)));
            System.out.println("  [+] Screening updated!");
        } else {
            System.out.println("  [i] No changes made.");
        }
        InputValidator.pause();
    }
}

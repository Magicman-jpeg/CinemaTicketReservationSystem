import java.util.Scanner;

/**
 * MODULE 9: InputValidator
 * Validates all user input throughout the system.
 * Ensures data integrity before database operations.
 */
public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Gets a non-empty string input from user.
     */
    public static String getString(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  [!] This field cannot be empty.");
        }
    }

    /**
     * Gets an optional string (can be empty).
     */
    public static String getOptionalString(String prompt) {
        System.out.print("  " + prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Gets a valid integer input.
     */
    public static int getInt(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    /**
     * Gets a valid integer within a range.
     */
    public static int getInt(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt + " (" + min + "-" + max + ")");
            if (value >= min && value <= max) return value;
            System.out.println("  [!] Must be between " + min + " and " + max + ".");
        }
    }

    /**
     * Gets a valid double input.
     */
    public static double getDouble(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    /**
     * Gets a yes/no confirmation.
     */
    public static boolean confirm(String prompt) {
        System.out.print("  " + prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    /**
     * Validates email format (basic check).
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Validates phone number (must be 11 digits starting with 09).
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("09\\d{9}");
    }

    /**
     * Validates seat format (letter A-J followed by number 1-10).
     */
    public static boolean isValidSeat(String seat) {
        if (seat == null || seat.length() < 2 || seat.length() > 3) return false;
        char row = seat.charAt(0);
        if (row < 'A' || row > 'J') return false;
        try {
            int col = Integer.parseInt(seat.substring(1));
            return col >= 1 && col <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Waits for user to press Enter.
     */
    public static void pause() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }
}

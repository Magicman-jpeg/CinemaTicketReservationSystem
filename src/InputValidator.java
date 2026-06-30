/* ============================================
 * MODULE 9: Input Validation Module
 * Cinema Ticket Reservation System
 * Author: Group 1 (BSIT 2-3)
 * Course: COMP 009 Object Oriented Programming
 * Purpose: Validates all user inputs.
            Ensures data integrity before operations.
 * ============================================ */

import java.util.Scanner;

public class InputValidator {
    // FUNCTION: Scanner instance for user inputs
    private static final Scanner scanner = new Scanner(System.in);

    // FUNCTION: Gets a non-empty string input from user
    public static String getString(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  [!] This field cannot be empty.");
        }
    }

    // FUNCTION: Gets an optional string (can be empty)
    public static String getOptionalString(String prompt) {
        System.out.print("  " + prompt + ": ");
        return scanner.nextLine().trim();
    }

    // FUNCTION: Gets a valid integer input
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

    // FUNCTION: Gets a valid integer within a range.
    public static int getInt(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt + " (" + min + "-" + max + ")");
            if (value >= min && value <= max) return value;
            System.out.println("  [!] Must be between " + min + " and " + max + ".");
        }
    }

    // FUNCTION: Gets a valid double input
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

    // FUNCTION: Gets a yes/no confirmation
    public static boolean confirm(String prompt) {
        System.out.print("  " + prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    // FUNCTION: Validates email format
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    // FUNCTION: Validates phone number (must be 11 digits starting with 09)
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("09\\d{9}");
    }

    // FUNCTION: Validates seat format (letter A-J followed by number 1-10)
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

    // FUNCTION: Waits for user to press Enter
    public static void pause() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }
}

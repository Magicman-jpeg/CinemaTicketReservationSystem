package com.cinema.ui;

import java.util.Scanner;

/**
 * Utility class for console UI formatting and input handling.
 * Provides reusable methods for menus, headers, and input validation.
 */
public class ConsoleUtils {

    private static final Scanner scanner = new Scanner(System.in);

    public static final String RESET = "";
    public static final String DIVIDER = "════════════════════════════════════════════════════════════════";
    public static final String THIN_DIVIDER = "────────────────────────────────────────────────────────────────";

    private ConsoleUtils() {} // Prevent instantiation

    /**
     * Displays the application banner.
     */
    public static void showBanner() {
        System.out.println();
        System.out.println(DIVIDER);
        System.out.println("       CINEMA TICKET RESERVATION SYSTEM");
        System.out.println("       OOP Final Project - COMP 013");
        System.out.println("       Group 2 - Cinema Hall Ticket Sales");
        System.out.println(DIVIDER);
        System.out.println();
    }

    /**
     * Displays a section header.
     */
    public static void showHeader(String title) {
        System.out.println();
        System.out.println(THIN_DIVIDER);
        System.out.println("  " + title);
        System.out.println(THIN_DIVIDER);
    }

    /**
     * Prompts user for input and returns the string.
     */
    public static String getInput(String prompt) {
        System.out.print("  " + prompt + ": ");
        return scanner.nextLine().trim();
    }


    /**
     * Prompts for integer input with validation.
     */
    public static int getIntInput(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Please try again.");
            }
        }
    }

    /**
     * Prompts for integer input with a valid range.
     */
    public static int getIntInput(String prompt, int min, int max) {
        while (true) {
            int value = getIntInput(prompt + " (" + min + "-" + max + ")");
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("  [!] Value must be between " + min + " and " + max + ".");
        }
    }

    /**
     * Prompts for double input with validation.
     */
    public static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Please try again.");
            }
        }
    }

    /**
     * Prompts for yes/no confirmation.
     */
    public static boolean confirm(String prompt) {
        System.out.print("  " + prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    /**
     * Displays a success message.
     */
    public static void showSuccess(String message) {
        System.out.println("  [+] " + message);
    }

    /**
     * Displays an error message.
     */
    public static void showError(String message) {
        System.out.println("  [!] ERROR: " + message);
    }

    /**
     * Displays an info message.
     */
    public static void showInfo(String message) {
        System.out.println("  [i] " + message);
    }

    /**
     * Pauses and waits for user to press Enter.
     */
    public static void pause() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Clears the console (approximation using newlines).
     */
    public static void clearScreen() {
        System.out.println("\n".repeat(3));
    }

    /**
     * Displays a menu and returns the user's choice.
     */
    public static int showMenu(String title, String[] options) {
        showHeader(title);
        for (int i = 0; i < options.length; i++) {
            System.out.printf("  [%d] %s%n", i + 1, options[i]);
        }
        System.out.println();
        return getIntInput("Enter your choice", 1, options.length);
    }
}

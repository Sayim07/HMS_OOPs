package hospital.util;

import java.util.Scanner;

/**
 * Utility helper class for robust console input parsing.
 * Prevents Scanner crashes on mismatched types (e.g. typing characters when integer is expected).
 */
public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        String val = scanner.nextLine().trim();
        while (val.isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            val = scanner.nextLine().trim();
        }
        return val;
    }

    public static String readOptionalString(String prompt, String defaultValue) {
        System.out.print(prompt);
        String val = scanner.nextLine().trim();
        if (val.isEmpty()) {
            return defaultValue;
        }
        return val;
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Please enter a valid whole number.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal! Please enter a valid numeric value.");
            }
        }
    }

    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y") || input.equals("true")) {
                return true;
            } else if (input.equals("no") || input.equals("n") || input.equals("false")) {
                return false;
            } else {
                System.out.println("Invalid choice! Enter 'yes' or 'no'.");
            }
        }
    }
}


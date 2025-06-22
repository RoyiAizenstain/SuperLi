package Deliveries.Presentation;

import java.util.Date;

/**
 * ConsoleInput.java
 * This class handles user input from the console.
 * It provides methods to get various types of input such as String, int, double, Date, and boolean.
 */
public class ConsoleInput {

    // Get a string input from the user
    public static String getString(String prompt) {
        System.out.print(prompt);
        String data = new java.util.Scanner(System.in).nextLine();
        if (data.isEmpty()) {
            System.out.println("Input cannot be empty. Please try again.");
            return getString(prompt);
        }
        return data;
    }

    // Get an integer input from the user
    public static int getInt(String prompt) {
        System.out.print(prompt);
        try {
            return new java.util.Scanner(System.in).nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer.");
            return getInt(prompt);
        }
    }

    // Get a double input from the user
    public static double getDouble(String prompt) {
        System.out.print(prompt);
        try {
            return new java.util.Scanner(System.in).nextDouble();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a double.");
            return getDouble(prompt);
        }
    }

    // Get a date input from the user
    public static Date getDate(String prompt) {
        String input = getString(prompt);
        // Assuming the date is in the format "yyyy-MM-dd"
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(input);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }

    // Get a time input from the user
    public static Date getTime(String prompt) {
        String input = getString(prompt);
        // Assuming the time is in the format "HH:mm"
        try {
            return new java.text.SimpleDateFormat("HH:mm").parse(input);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid time format. Please use HH:mm.");
            return null;
        }
    }

    // Get a choice input from the user
    public static int getChoice(String prompt, int min, int max) {
        int choice = -1;
        do {
            System.out.print(prompt);
            try {
                choice = new java.util.Scanner(System.in).nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                continue;
            }

            if (choice < min || choice > max) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (choice < min || choice > max);
        return choice;
    }

    // Get a boolean input from the user
    public static boolean getBoolean(String prompt) {
        String input = getString(prompt);
        if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
            return true;
        } else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
            return false;
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            return getBoolean(prompt);
        }
    }

}

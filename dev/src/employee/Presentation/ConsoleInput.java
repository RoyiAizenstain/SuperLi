package employee.Presentation;

import employee.Enums.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ConsoleInput {

    public Days inputDay() {
        return this.getEnumInput(Days.class, "Day: ");
    }

    public int inputPositiveNumber(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int number;

        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                if (number > 0) {
                    break;
                } else {
                    System.out.println("number must be positive.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return number;
    }

    public Date inputDate() {
        Scanner scanner = new Scanner(System.in);
        Date date = null;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Date (yyyy-MM-dd):");
                String dateString = scanner.nextLine();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false); // Strict date parsing
                date = dateFormat.parse(dateString);
                validInput = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
        return date;
    }

    public ShiftType inputShiftType() {
        return this.getEnumInput(ShiftType.class, "shift Type: ");
    }

    public  <T extends Enum<T>> T getEnumInput(Class<T> enumClass, String prompt) {
        Scanner scanner = new Scanner(System.in);
        T[] enumValues = enumClass.getEnumConstants();
        T selectedValue = null;

        while (selectedValue == null) {
            // Display options
            System.out.println(prompt);
            for (int i = 0; i < enumValues.length; i++) {
                System.out.println((i + 1) + " - " + enumValues[i].name().replace('_', ' '));
            }

            try {
                System.out.print("Choose: ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= enumValues.length) {
                    selectedValue = enumValues[choice - 1];
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + enumValues.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }

        return selectedValue;
    }

    public LocalTime[] getValidTimeRange() {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = null;
        LocalTime endTime = null;
        boolean validRange = false;

        while (!validRange) {
            // Get start time
            startTime = getTimeInput(scanner, "Enter start time (HH:mm):", formatter);

            // Get end time
            endTime = getTimeInput(scanner, "Enter end time (HH:mm):", formatter);

            // Validate start time is before end time
            if (startTime.isBefore(endTime)) {
                validRange = true;
            } else {
                System.out.println("Error: Start time must be before end time. Please try again.");
            }
        }

        return new LocalTime[] {startTime, endTime};
    }

    public LocalTime getTimeInput(Scanner scanner, String prompt, DateTimeFormatter formatter) {
        LocalTime time = null;

        while (time == null) {
            try {
                System.out.println(prompt);
                String input = scanner.nextLine();
                time = LocalTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:mm (e.g., 12:30)");
            }
        }
        return time;
    }

    public boolean isDateInNextWeek(Date dateToCheck) {
        Calendar today = Calendar.getInstance();

        //find the Sunday of next week
        Calendar nextSunday = (Calendar) today.clone();
        nextSunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        nextSunday.add(Calendar.WEEK_OF_YEAR, 1);
        nextSunday.set(Calendar.HOUR_OF_DAY, 0);
        nextSunday.set(Calendar.MINUTE, 0);
        nextSunday.set(Calendar.SECOND, 0);
        nextSunday.set(Calendar.MILLISECOND, 0);

        //find the Saturday of next week
        Calendar nextSaturday = (Calendar) nextSunday.clone();
        nextSaturday.add(Calendar.DAY_OF_YEAR, 6);
        nextSaturday.set(Calendar.HOUR_OF_DAY, 0);
        nextSaturday.set(Calendar.MINUTE, 0);
        nextSaturday.set(Calendar.SECOND, 0);
        nextSaturday.set(Calendar.MILLISECOND, 0);

        //check if the date is in the range between Sunday and Saturday of next week
        if (dateToCheck.before(nextSunday.getTime()) || dateToCheck.after(nextSaturday.getTime())) {
            return false;
        }
        return true;
    }

}

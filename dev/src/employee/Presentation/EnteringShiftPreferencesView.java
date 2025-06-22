package employee.Presentation;

import employee.Service.*;
import employee.Enums.*;
import employee.Exceptions.*;

import java.util.*;

public class EnteringShiftPreferencesView {

    void enterPreferences(Date date, int userId) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        //loop until the user chooses to exit
        while (!exit) {
            final int ADD_PREFERENCE_OPTION = 1;
            final int REMOVE_PREFERENCE_OPTION = 2;
            final int EXIT_VIEW_OPTION = 3;
            System.out.println("--- Entering Shift Preferences Menu ---");
            System.out.println(ADD_PREFERENCE_OPTION + ". Add preference");
            System.out.println(REMOVE_PREFERENCE_OPTION + ". Remove preference");
            System.out.println(EXIT_VIEW_OPTION + ". Exit View");
            System.out.print("Choose: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice > 3 || choice < 1) {
                    throw new NumberFormatException("choice should be between 1 to 3");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case ADD_PREFERENCE_OPTION: {
                    addPreference(date, userId);
                    break;
                }

                case REMOVE_PREFERENCE_OPTION: {
                    removePreference(date, userId);
                    break;
                }

                case EXIT_VIEW_OPTION: {
                    System.out.println("bye..");
                    exit = true;
                    break;
                }
            }
        }
    }

    public boolean ReachedDeadline(Date date){ //Thursday is the deadline for shift arrangement
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.THURSDAY;
    }

    public void addPreference(Date date,int userId) {
        ServiceController serviceController = new ServiceController();
        ConsoleInput consoleInput = new ConsoleInput();
        try {
            if (ReachedDeadline(date)) {
                serviceController.printNonPreferredShiftsDetails(userId);
                Days shiftDay = consoleInput.inputDay();
                ShiftType shiftType = consoleInput.inputShiftType();
                boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                if (shiftExists) {
                    try {
                        serviceController.addPreference(userId, shiftDay, shiftType);
                        System.out.println("Preference added to this shift successfully!");
                    } catch (InvalidInputException e) {
                        System.out.println("can't add this preference because: " + e.getMessage());
                    }
                } else {
                    System.out.println("shift not found...");
                }
            } else {
                System.out.println("Shift preferences can only be entered in Thursdays.");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void removePreference(Date date,int userId) {
        ServiceController serviceController = new ServiceController();
        ConsoleInput consoleInput = new ConsoleInput();
        try {
            if (ReachedDeadline(date)) {
                serviceController.printPreferredShiftsDetails(userId);
                Days shiftDay = consoleInput.inputDay();
                ShiftType shiftType = consoleInput.inputShiftType();
                boolean shiftExists = serviceController.isShiftExists(shiftDay, shiftType);
                if (shiftExists) {
                    try {
                        serviceController.removePreference(userId, shiftDay, shiftType);
                        System.out.println("Preference removed from this shift successfully!");
                    } catch (InvalidInputException e) {
                        System.out.println("can't remove this preference because: " + e.getMessage());
                    }
                } else {
                    System.out.println("shift not found...");
                }
            } else {
                System.out.println("Remove shift preferences can only be happen in Thursdays.");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

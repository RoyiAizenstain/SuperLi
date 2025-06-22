package employee.Presentation;

import employee.Service.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class RegularEmployeeMenuView {

    public void showRegularEmployeeMenu(int userId){
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        EnteringShiftPreferencesView shiftPreferencesView = new EnteringShiftPreferencesView();
        //loop until the user chooses to exit
        while (!exit) {
            System.out.println("--- Regular Employee Menu ---");
            System.out.println("1. View my personal details");
            System.out.println("2. Enter shift preferences - Thursday simulation");
            System.out.println("3. Enter shift preferences - other days simulation");
            System.out.println("4. View my shift history");
            System.out.println("5. View my shifts for this week");
            System.out.println("6. Logout");
            System.out.print("Choose: ");
            int choice;
            try { //get user choice and handle errors
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            //process the user's choice
            switch (choice) {
                case 1:
                    EmployeePersonalDetailsView personalDetailsView = new EmployeePersonalDetailsView();
                    personalDetailsView.showMyDetailsMenu(userId);
                    break;
                case 2:
                    Calendar cal1 = Calendar.getInstance();
                    cal1.set(2025, Calendar.APRIL, 17);
                    Date thursday = new Date(cal1.getTimeInMillis());
                    shiftPreferencesView.enterPreferences(thursday, userId);
                    break;
                case 3:
                    Calendar cal2 = Calendar.getInstance();
                    cal2.set(2025, Calendar.APRIL, 14);
                    Date monday = new Date(cal2.getTimeInMillis());
                    shiftPreferencesView.enterPreferences(monday, userId);
                    break;
                case 4:
                    try {
                        serviceController.printShiftHistory(userId);
                    }
                    catch (Exception e){
                        System.out.println("Cant show your work history because: " + e.getMessage());
                    }
                    break;
                case 5:
                    Date date = new Date();
                    try {
                        serviceController.printUserWeekShifts(userId, date);
                    }
                    catch (Exception e){
                        System.out.println("Cant show your shifts for this week because: " + e.getMessage());
                    }
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 6.");
            }
        }
    }
}

package employee.Presentation;

import employee.Service.ServiceController;

import java.util.Date;
import java.util.Scanner;

public class ShiftManagerMenuView {

    public void showShiftManagerMenu(int userId){
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        //loop until the user chooses to exit
        while (!exit) {
            System.out.println("--- Shift Manager Employee Menu ---");
            System.out.println("1. Enter as a regular employee");
            System.out.println("2. View details of employees on my shifts this week");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    RegularEmployeeMenuView regularEmployeeMenuView = new RegularEmployeeMenuView();
                    regularEmployeeMenuView.showRegularEmployeeMenu(userId);
                    break;
                case 2:
                    Date date = new Date();
                    try {
                        serviceController.printManagerWeekShifts(userId, date);
                    }
                    catch (Exception e){
                        System.out.println("Cant show employees details in your managing shifts for this week because: " + e.getMessage());
                    }
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 3.");
            }
        }
    }

}


package employee.Presentation;

import employee.Service.*;

import java.util.*;

public class EmployeePersonalDetailsView {

    public void showMyDetailsMenu(int userId){
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        //loop until the user chooses to exit
        while (!exit){
            System.out.println("--- Personal Details Menu (ID - " + userId +") ---");
            System.out.println("1. View general details");
            System.out.println("2. View holiday and sick days Status");
            System.out.println("3. View bank details");
            System.out.println("4. View employment terms");
            System.out.println("5. View abilities and roles");
            System.out.println("6. Back to the previous menu");
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
                    try {
                        serviceController.printEmployee(userId);
                    }
                    catch (Exception e){
                        System.out.println("Can't show employee details because: " + e.getMessage());
                    }
                    break;
                case 2:
                    try{
                        int holidays = serviceController.getHolidays(userId);
                        int sickDays = serviceController.getSickDays(userId);
                        System.out.println("Holiday Status: " + holidays);
                        System.out.println("Sick Days Status: " + sickDays);
                    }
                    catch (Exception e){
                        System.out.println("Can't show employee details because: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        serviceController.printBankDetails(userId);
                    }
                    catch (Exception e){
                        System.out.println("Can't show employee details because: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        serviceController.printEmploymentTerms(userId);
                    }
                    catch (Exception e){
                        System.out.println("Can't show employee details because: " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        serviceController.printAbilities(userId);
                        serviceController.printRelevantRoles(userId);
                    }
                    catch (Exception e){
                        System.out.println("Can't show employee details because: " + e.getMessage());
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



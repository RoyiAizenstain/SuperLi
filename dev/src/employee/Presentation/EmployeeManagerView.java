package employee.Presentation;

import employee.Service.ServiceController;

import java.util.Scanner;

public class EmployeeManagerView {

    public void showAllEmployeesMenu(){
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        //loop until the user chooses to exit
        while (!exit){
            System.out.println("--- Show All Employees Menu ---");
            System.out.println("1. Show all employees");
            System.out.println("2. Show all active employees");
            System.out.println("3. Show all former employees");
            System.out.println("4. Show details of a specific employee by ID");
            System.out.println("5. Back to the previous menu");
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
                        serviceController.printAllEmployees();
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        serviceController.printActiveEmployees();
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        serviceController.printOldEmployees();
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    showEmployeeDetailsByID();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 5.");
            }

        }
    }

    public void showEmployeeDetailsByID() {
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee ID: ");
        int searchId;
        try {
            searchId = Integer.parseInt(scanner.nextLine());
            serviceController.isValidEmployee(searchId);
            EmployeePersonalDetailsView employeePersonalDetailsView = new EmployeePersonalDetailsView();
            employeePersonalDetailsView.showMyDetailsMenu(searchId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

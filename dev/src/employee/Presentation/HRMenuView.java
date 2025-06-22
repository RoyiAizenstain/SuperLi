package employee.Presentation;

import java.util.Scanner;

public class HRMenuView {

    public void showHRMenu(int userId){
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        //loop until the user chooses to exit
        while (!exit) {
            System.out.println("--- HR Employee Menu ---");
            System.out.println("1. View my personal details");
            System.out.println("2. View details of all employees");
            System.out.println("3. Update employee profile and status");
            System.out.println("4. Add a new employee");
            System.out.println("5. Assign shifts");
            System.out.println("6. View shift history");
            System.out.println("7. Logout");
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
                    EmployeePersonalDetailsView employeePersonalDetailsView = new EmployeePersonalDetailsView();
                    employeePersonalDetailsView.showMyDetailsMenu(userId);
                    break;
                case 2:
                    EmployeeManagerView employeeManagerView = new EmployeeManagerView();
                    employeeManagerView.showAllEmployeesMenu();
                    break;
                case 3:
                    UpdateEmployeeMenuView updateEmployeeMenuView = new UpdateEmployeeMenuView();
                    updateEmployeeMenuView.updateEmployeeMenu();
                    break;
                case 4:
                    AddEmployeeMenuView addEmployeeMenuView = new AddEmployeeMenuView();
                    addEmployeeMenuView.addEmployeeMenu();
                    break;
                case 5:
                    ShiftAssignmentsView shiftAssignmentsView = new ShiftAssignmentsView();
                    shiftAssignmentsView.assignShifts();
                    break;
                case 6:
                    YearlyShiftsView yearlyShiftsView = new YearlyShiftsView();
                    yearlyShiftsView.YearlyShiftMenu();
                    break;
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 7.");
            }
        }
    }

}

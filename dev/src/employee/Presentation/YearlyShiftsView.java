package employee.Presentation;

import employee.Service.*;

import java.util.Scanner;

public class YearlyShiftsView {

    public void YearlyShiftMenu(){
        Scanner scanner = new Scanner(System.in);
        ServiceController serviceController = new ServiceController();
        boolean exit = false;
        while (!exit) {
            System.out.println("--- Shift History Menu ---");
            System.out.println("1. View available years in the system");
            System.out.println("2. View shifts in a specific year");
            System.out.println("3. View shifts in a specific week and year");
            System.out.println("4. Back to main menu");
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
                        serviceController.printAvailableYears();
                        break;
                    }
                    catch (Exception e){
                        System.out.println("Cant view available years in the system because: " + e.getMessage());
                    }

                case 2: {
                    int year;
                    while (true) {
                        try {
                            System.out.print("Enter year: ");
                            year = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }
                    try {
                        serviceController.printShiftsInYear(year);
                    } catch (Exception e) {
                        System.out.println("Cant view shifts in this specific year because: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    int year;
                    int week;
                    while (true) {
                        try {
                            System.out.print("Enter year: ");
                            year = Integer.parseInt(scanner.nextLine());
                            System.out.print("Enter week number (0-52): ");
                            week = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }
                    try {
                        serviceController.printShiftsInWeekAndYear(year, week);
                    }
                    catch (Exception e){
                        System.out.println("Cant view shifts in this specific week and year because: " + e.getMessage());
                    }
                    break;
                }
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 4.");
            }
        }
    }

}

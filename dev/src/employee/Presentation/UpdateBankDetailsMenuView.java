package employee.Presentation;

import employee.Service.*;

import java.util.Scanner;

public class UpdateBankDetailsMenuView {

    public void updateBankDetailsMenu(int employeeId) {
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        int choice;
        while (!exit) {
            try {
                System.out.println("--- Update Bank Details Menu ---");
                System.out.print("* The current bank account information in the system: \n   ");
                serviceController.printBankDetails(employeeId);
                System.out.println("1. Update Bank Number");
                System.out.println("2. Update Branch Number");
                System.out.println("3. Update Account Number");
                System.out.println("4. Back to previous menu");
                System.out.print("Choose: ");
                //get user choice and handle errors

            }catch (Exception e){
                System.out.println("Cant print bank details because: " + e.getMessage());
            }
            try {
                choice = Integer.parseInt(scanner.nextLine());
                //process the user's choice
                switch (choice) {
                    case 1:
                        System.out.print("Enter new 2-digit bank number: ");
                        int bankNum = Integer.parseInt(scanner.nextLine());
                        serviceController.setBankNumber(employeeId, bankNum);
                        System.out.println("Bank number updated successfully!");
                        break;
                    case 2:
                        System.out.print("Enter new 9-digit branch number: ");
                        int branchNum = Integer.parseInt(scanner.nextLine());
                        serviceController.setBranchNumber(employeeId, branchNum);
                        System.out.println("Branch number updated successfully!");
                        break;
                    case 3:
                        System.out.print("Enter new 3-digit account number: ");
                        int accountNum = Integer.parseInt(scanner.nextLine());
                        serviceController.setAccountNumber(employeeId, accountNum);
                        System.out.println("Account number updated successfully!");
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e){
                System.out.println("Cant update bank number because: " + e.getMessage());
            }
        }
    }
}

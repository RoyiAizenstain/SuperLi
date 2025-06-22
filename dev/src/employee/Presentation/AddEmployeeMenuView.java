package employee.Presentation;

import employee.Exceptions.InvalidInputException;
import employee.Service.ServiceController;
import employee.Enums.*;

import java.util.Scanner;

public class AddEmployeeMenuView {

    public void addEmployeeMenu() {
        ConsoleInput consoleInput = new ConsoleInput();
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Add New Employee ---");
        String name;
        int id = 0;
        int accountNumber;
        int bankNumber;
        int branchNumber;
        int salary;
        String contract;
        String phoneNumber;
        int branch;
        while (true) {
            try {
                //get the employee's details
                System.out.print("Enter employee name: ");
                name = scanner.nextLine();
                if (!isValidName(name)) {
                    throw new InvalidInputException("employee name must be less than 50 chars");
                }
                break;
            } catch (InvalidInputException e) {
                System.out.println("Invalid employee name. Please try again.");
            }
        }
        //get the employee's details
        while (true) {
            try {
                id = consoleInput.inputPositiveNumber("Enter employee ID: ");
                serviceController.isValidEmployee(id);
            } catch (NumberFormatException e) {
                System.out.println("Invalid employee id. Please try again.");
            } catch (Exception e) {//the employee is not exists
                break;
            }
            System.out.println("Employee with ID " + id + " already exists in the system.");
        }
        while (true) {
            try {
                //get the employee's details
                bankNumber = consoleInput.inputPositiveNumber("Enter bank number (2 digits): ");
                if (String.valueOf(bankNumber).length() != 2) {
                    throw new InvalidInputException("bank number must be 2 digits");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid bank number. " + e.getMessage());
            }
        }
        while (true) {
            try {
                //get the employee's details
                branchNumber = consoleInput.inputPositiveNumber("Enter branch number (9 digits): ");
                if (String.valueOf(branchNumber).length() != 9) {
                    throw new InvalidInputException("bank number must be 9 digits");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid branch number. " + e.getMessage());
            }
        }
        while (true) {
            try {
                //get the employee's details
                accountNumber = consoleInput.inputPositiveNumber("Enter account number (3 digits): ");
                if (String.valueOf(accountNumber).length() != 3) {
                    throw new InvalidInputException("bank number must be 3 digits");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid account number. " + e.getMessage());
            }
        }
        while (true) {
            //get the employee's details
            salary = consoleInput.inputPositiveNumber("Enter salary: ");
            break;
        }
        while (true) {
            try {
                //get the employee's details
                System.out.print("Enter contract type (e.g., Full-time, Part-time): ");
                contract = scanner.nextLine();
                if (!isValidName(contract)) {
                    throw new InvalidInputException("contract must be less than 50 chars");
                }
                break;
            } catch (InvalidInputException e) {
                System.out.println("Invalid contract content. Please try again.");
            }
        }
        Job job = consoleInput.getEnumInput(Job.class,"Select job: " );
        while (true) {
            try {
                //get the employee's details
                System.out.print("Enter phone number (10 digits only, starting with 0): ");
                phoneNumber = scanner.nextLine();
                if (!isValidPhoneNumber(phoneNumber)) {
                    throw new InvalidInputException("Invalid phone number. Please try again.");
                }
                break;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            try {
                //get the employee's details
                serviceController.printAvaliableSuperBranches();
                branch = consoleInput.inputPositiveNumber("Enter super branch: ");
                if (!serviceController.isSuperBranchExists(branch)) {
                    throw new InvalidInputException("super branch not exists");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid Super Branch. " + e.getMessage());
            }
        }
        //add the employee to the system
        try {
            serviceController.addEmployee(name, id, bankNumber, branchNumber, accountNumber, salary, contract, job, phoneNumber,branch);
            System.out.println("Employee added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isValidName (String s){
        return !s.isEmpty()
                && s.length() <= 50;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 10) {
            return false;
        }
        if (!phoneNumber.matches("\\d{10}")) {
            return false;
        }
        if (phoneNumber.charAt(0) != '0') {
            return false;
        }
        return true;
    }

}

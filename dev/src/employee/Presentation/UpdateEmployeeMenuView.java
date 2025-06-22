package employee.Presentation;

import employee.Service.ServiceController;
import employee.Enums.*;
import employee.Exceptions.*;

import java.util.Scanner;

public class UpdateEmployeeMenuView {

    public void updateEmployeeMenu(){
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        try {
           serviceController.isThereEmployees();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            exit = true;
        }
        while (!exit){
            System.out.println("--- Update Employee Menu ---");
            System.out.print("Enter the ID of the employee you want to update: ");
            int id;

            try {
                id = Integer.parseInt(scanner.nextLine());
                serviceController.isValidEmployee(id);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            boolean switchEmployee = false;
            while (!switchEmployee && !exit) {
                System.out.println("--- Update Menu for Employee ID: " + id + " ---");
                System.out.println("1. Update abilities");
                System.out.println("2. Update work limits");
                System.out.println("3. Update employment status");
                System.out.println("4. Update bank account information");
                System.out.println("5. Update salary");
                System.out.println("6. Update contract");
                System.out.println("7. Update phone number");
                System.out.println("8. Reduce sick days");
                System.out.println("9. Reduce Holidays");
                System.out.println("10. Update Super Branch");
                System.out.println("11. Update another employee");
                System.out.println("12. Back to previous menu");
                System.out.print("Choose: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    //process the user's choice
                    switch (choice) {
                        case 1:
                            UpdateAbilitiesMenuView updateAbilitiesMenuView = new UpdateAbilitiesMenuView();
                            updateAbilitiesMenuView.updateAbilitiesMenu(id);
                            break;
                        case 2:
                            try {
                                String workLimits = serviceController.getWorkLimits(id);
                                System.out.println("The current work limits in the system - " + workLimits);
                                System.out.print("Enter new work limits: ");
                                String newWorkLimits = scanner.nextLine();
                                serviceController.setWorkLimits(id,newWorkLimits);
                                System.out.println("Work limits updated successfully!");
                            }
                            catch (Exception e){
                                System.out.println("Work limits cant be updated because: " + e.getMessage());
                            }
                            break;
                        case 3:
                            try {
                                WorkStatus workStatus = serviceController.getWorkStatus(id);
                                System.out.println("The current employment status in the system - " + workStatus);
                                WorkStatus newStatus = selectWorkStatus();
                                serviceController.setWorkStatus(id,newStatus);
                                System.out.println("Employment status updated successfully!");
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number.");
                            } catch (Exception e){
                                System.out.println("Work status cant be updated because: " + e.getMessage());
                            }
                            break;
                        case 4:
                            UpdateBankDetailsMenuView updateBankDetailsMenuView = new UpdateBankDetailsMenuView();
                            updateBankDetailsMenuView.updateBankDetailsMenu(id);
                            break;
                        case 5:
                            try {
                                int salary = serviceController.getSalary(id);
                                System.out.println("The current salary in the system - " + salary);
                                try {
                                    System.out.print("Enter new salary: ");
                                    int newSalary = Integer.parseInt(scanner.nextLine());
                                    serviceController.setSalary(id,newSalary);
                                    System.out.println("Salary updated successfully!");
                                }
                                catch(NumberFormatException e){
                                    System.out.println("Invalid input. Please enter a number.");
                                }
                            }
                            catch (Exception e){
                                System.out.println("Salary cant be updated because: " + e.getMessage());
                            }
                            break;
                        case 6:
                            try {
                                String contract = serviceController.getContract(id);
                                System.out.println("The current contract limits in the system - " + contract);
                                System.out.print("Enter new contract: ");
                                String newContract = scanner.nextLine();
                                serviceController.setContract(id,newContract);
                                System.out.println("Contract updated successfully!");
                            } catch (Exception e){
                                System.out.println("Contract cant be updated because: " + e.getMessage());
                            }
                            break;
                        case 7:
                            try {
                                String phoneNumber = serviceController.getPhoneNumber(id);
                                System.out.println("The current phone number in the system - " + phoneNumber);
                                System.out.print("Enter new phone number: ");
                                String newPhoneNumber = scanner.nextLine();
                                serviceController.setPhoneNumber(id,newPhoneNumber);
                                System.out.println("Phone number updated successfully!");
                            } catch (Exception e){
                                System.out.println("Phone number cant be updated because: " + e.getMessage());
                            }
                            break;
                        case 8:
                            try {
                                int sickDays = serviceController.getSickDays(id);
                                System.out.println("The current amount of sick days in the system - " + sickDays);
                                System.out.print("By how much would you like to reduce sick days? ");
                                int amount = Integer.parseInt(scanner.nextLine());
                                serviceController.reduceSickDays(id, amount);
                                System.out.println("Sick days updated successfully!");
                            } catch(NumberFormatException e){
                                System.out.println("Invalid input. Please enter a number.");
                            } catch (Exception e){
                                System.out.println("Sick Days cant be updated because: " + e.getMessage());
                            }
                            break;
                        case 9:
                            try {
                                int holidays = serviceController.getHolidays(id);
                                System.out.println("The current amount of holidays in the system - " + holidays);
                                System.out.print("By how much would you like to reduce holidays? ");
                                int amount = Integer.parseInt(scanner.nextLine());
                                serviceController.reduceHoliDays(id, amount);
                                System.out.println("Holidays updated successfully!");
                            } catch(NumberFormatException e){
                                System.out.println("Invalid input. Please enter a number.");
                            } catch (Exception e){
                                System.out.println("Holidays cant be updated because: " + e.getMessage());
                            }
                            break;
                        case 10 :
                        {
                            try {
                                int branch = serviceController.getSuperBranch(id);
                                System.out.println("The current Super Branch in the system - " + branch);
                                serviceController.printAvaliableSuperBranches();
                                System.out.print("Enter new Super branch: ");
                                int newNum = Integer.parseInt(scanner.nextLine());
                                serviceController.updateSuperBranch(id,newNum);
                                System.out.println("Super branch updated successfully!");
                            } catch(NumberFormatException e){
                                System.out.println("Invalid input. Please enter a number.");
                            } catch (Exception e){
                                System.out.println("Super Branch cant be updated because: " + e.getMessage());
                            }
                            break;
                        }
                        case 11:
                            switchEmployee = true;
                            break;
                        case 12:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please choose a number between 1 and 12.");
                    }

                }catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }
    }

    private WorkStatus selectWorkStatus() throws InvalidInputException, NumberFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose new employment status:");
        WorkStatus[] statuses = WorkStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }
        System.out.print("Enter choice: ");

        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > statuses.length) {
            throw new InvalidInputException("Choice out of range. Please select a valid option.");
        }
        return statuses[choice - 1];
    }

}

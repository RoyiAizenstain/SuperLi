package employee.Presentation;

import employee.Service.*;
import employee.Enums.*;
import employee.Exceptions.*;

import java.util.Scanner;

public class UpdateAbilitiesMenuView {

    public void updateAbilitiesMenu(int employeeId) {
        ServiceController serviceController = new ServiceController();
        Scanner scanner = new Scanner(System.in);
        try {
            boolean exit = false;

            //loop until the user chooses to exit
            while (!exit) {
                System.out.println("--- Manage Abilities ---");
                System.out.print("* The current situation in the system: \n  ");
                serviceController.printAbilities(employeeId);
                System.out.println("1. Add new ability");
                System.out.println("2. Remove existing ability");
                System.out.println("3. Back to previous menu");
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
                            Ability abilityToAdd = chooseAbility();
                            serviceController.addAbility(employeeId, abilityToAdd);
                            System.out.println("Ability added successfully!");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        } catch (Exception e) {
                            System.out.println("Can't add ability: " + e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            Ability abilityToRemove = chooseAbility();
                            serviceController.removeAbility(employeeId, abilityToRemove);
                            System.out.println("Ability removed successfully!");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        } catch (Exception e) {
                            System.out.println("Can't add ability: " + e.getMessage());
                        }
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a number between 1 and 3.");
                }
            }
        } catch (Exception e) {
            System.out.println("Cant update abilities because: " + e.getMessage());
        }
    }

    private Ability chooseAbility() throws InvalidInputException, NumberFormatException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available options:");
        Ability[] allAbilities = Ability.values();
        for(int i=0; i<allAbilities.length; i++) {
            System.out.println((i+1) + ". " + allAbilities[i].name().replace('_', ' '));
        }
        System.out.print("Enter ability number: ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > allAbilities.length) {
            throw new InvalidInputException("Invalid choice. Please choose a number from the list.");
        }
        return allAbilities[choice - 1];
    }
}

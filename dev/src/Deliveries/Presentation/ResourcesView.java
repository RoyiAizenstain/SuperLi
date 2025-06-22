package Deliveries.Presentation;

import Deliveries.DTO.TruckDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Service.DeliveryControler;
import Deliveries.DTO.*;
import java.sql.SQLException;

/**
 * ResourcesView.java
 * This class handles the user interface for managing resources such as drivers and trucks.
 * It allows users to add, delete, and update drivers and trucks.
 */
public class ResourcesView {
    static DeliveryControler deliveryControler = new DeliveryControler();

    // Display the driver management menu
    public static void showDriverMenu() {
        System.out.println("=========================== Driver Management ===========================");
        System.out.println("1. View Drivers");
        System.out.println("2. Add licence details for driver");
        System.out.println("3. Back to Main Menu");
        int choice = ConsoleInput.getChoice("Select an option (1-3): ", 1, 3);
        processDriverMenuChoice(choice);
    }

    /// Process the user's choice from the driver menu
    private static void processDriverMenuChoice(int choice) {
        switch (choice) {
            case 1:
                showDrivers();
                break;
            case  2:
                addLicenceDetailsForDriver();
                break;
            case 3:
                UI.showMainMenu();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                showDriverMenu();
        }
    }

    // Show all drivers with exception handling using DTO methods
    public static void showDrivers() {
        System.out.println("=========================== View Drivers ===========================");
        try {
            System.out.println(deliveryControler.allDriversToString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        showDriverMenu();
    }



    // Add licence details for a driver
    public static void addLicenceDetailsForDriver() {
        System.out.println("=========================== Add Licence Details ===========================");
        String driverID = ConsoleInput.getString("Enter driver's ID: ");
        String licenseType = ConsoleInput.getString("Enter driver's license type: ");
        String licenseNumber = ConsoleInput.getString("Enter driver's license number: ");
        try {
            deliveryControler.addLicenceDetailsForDriver(driverID, licenseType, licenseNumber);
        } catch (Exceptions.InputNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        showDriverMenu();
    }


    /// Show the truck management menu
    public static void showTruckMenu() {
        System.out.println("=========================== Truck Management ===========================");
        System.out.println("1. Add Truck");
        System.out.println("2. Delete Truck");
        System.out.println("3. View Trucks");
        System.out.println("4. Back to Main Menu");

        int choice = ConsoleInput.getChoice("Select an option (1-4): ", 1, 4);
        processTruckMenuChoice(choice);
    }

    // Process the user's choice from the truck menu
    public static void processTruckMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addTruck();
                break;
            case 2:
                deleteTruck();
                break;
            case 3:
                // Update Truck functionality
                showTrucks();
                break;
            case 4:
                // View Trucks functionality
                UI.showMainMenu();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                showTruckMenu();
        }
    }

    // Show all trucks with exception handling using DTO methods
    private static void showTrucks() {
        System.out.println("=========================== View Trucks ===========================");
        try {
            System.out.println(deliveryControler.allTrucksToString());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        showTruckMenu();
    }


    // Add a new truck using DTO
    public static void addTruck() {
        System.out.println("=========================== Add Truck ===========================");
        String truckID = ConsoleInput.getString("Enter truck's ID: ");
        String type = ConsoleInput.getString("Enter truck's type: ");
        String licensePlate = ConsoleInput.getString("Enter truck license plate: ");
        Double tareWeight = ConsoleInput.getDouble("Enter truck's tare weight: ");
        Double maxWeight = ConsoleInput.getDouble("Enter truck's max weight: ");
        TruckDTO truck = new TruckDTO(truckID, type, licensePlate, tareWeight, maxWeight, true);
        try {
            deliveryControler.addTruck(truck);
        } catch (Exceptions.InputNotFoundException | Exceptions.AlreadyExist | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        showTruckMenu();
    }

    // Delete an existing truck using DTO
    public static void deleteTruck() {
        System.out.println("=========================== Delete Truck ===========================");
        String truckID = ConsoleInput.getString("Enter truck's ID: ");
        try {
            deliveryControler.removeTruck(truckID);
        } catch (Exceptions.InputNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        showTruckMenu();
    }

}

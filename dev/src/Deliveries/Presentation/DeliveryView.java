package Deliveries.Presentation;

import Deliveries.Domain.Exceptions;
import Deliveries.Service.DeliveryControler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Deliveries.DTO.*;

/**
 * DeliveryView.java
 * This class handles the user interface for managing deliveries.
 * It allows users to plan new deliveries, view existing deliveries,
 * and update delivery statuses.
 */
public class DeliveryView {
    static DeliveryControler deliveryControler = new DeliveryControler();

    // Display the delivery management menu
    public static void showDeliveryMenu() {
        // Display the delivery management menu
        System.out.println("=========================== Delivery Management ===========================");
        System.out.println("1. Plan New Delivery");
        System.out.println("2. View Deliveries");
        System.out.println("3. Update Delivery Status");
        System.out.println("4. Back to Main Menu");

        int choice = ConsoleInput.getChoice("Select an option (1-4): ", 1, 4);
        processDeliveryMenuChoice(choice);
    }

    // Process the user's choice from the delivery menu
    private static void processDeliveryMenuChoice(int choice) {
        switch (choice) {
            case 1:
                showPlanDelivery();
                break;
            case 2:
                showAllDeliveries();
                break;
            case 3:
                updateDeliveryStatus();
                break;
            case 4:
                UI.showMainMenu();
                return; // Back to the main menu
            default:
                System.out.println("Invalid option. Please try again.");
                showDeliveryMenu();
        }
    }

    /// Plan a new delivery
    private static void showPlanDelivery() {
        System.out.println("=========================== Plan Delivery ===========================");

        //TODO: PLAN DELIVERY FOR NOW ONLY CONSIDER TIME
        // Get delivery date
        Date deliveryDate = ConsoleInput.getDate("Enter delivery date (yyyy-MM-dd): ");
        String time = ConsoleInput.getString("Enter delivery time (HH:mm): ");

        // Get shipping zone ID
        String shippingZoneId = ConsoleInput.getString("Enter shipping zone ID: ");

        // Get origin ID
        String originId = ConsoleInput.getString("Enter origin ID: ");

        // Get preferred orders
        List<String> preferredOrders = new ArrayList<>();
        boolean addPreferred = ConsoleInput.getBoolean("Do you want to add preferred orders? (yes/no): ");

        if (addPreferred) {
            while (true) {
                String orderId = ConsoleInput.getString("Enter preferred order ID (or 'done' to finish): ");
                if (orderId.equalsIgnoreCase("done")) {
                    break;
                }
                preferredOrders.add(orderId);
            }
        }

        try {
            deliveryControler.planDelivery(deliveryDate, shippingZoneId, preferredOrders, originId, time);
            System.out.println("Delivery planned successfully!");
        } catch (Exceptions.InputNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error planning delivery: " + e.getMessage());
        }

        // Return to the delivery menu
        showDeliveryMenu();
    }

    //View all deliveries
    private static void showAllDeliveries() {
        System.out.println("=========================== View Deliveries ===========================");
        // Display all deliveries
        try{
            System.out.println(deliveryControler.allDeliveriesToString());
        }catch (Exception e){
            System.out.println("Error viewing Deliveries");
        }
        showDeliveryMenu();

    }

    /// Update delivery status
    private static void updateDeliveryStatus() {
        // Implementation for updating delivery status
        System.out.println("=========================== Update Delivery Status ===========================");
        String deliveryId = ConsoleInput.getString("Enter delivery ID: ");
        String newStatus = ConsoleInput.getString("Enter new status:(PENDING, IN_PROGRESS, COMPLETED): ");
        if (newStatus == "COMPLETED") deliveryControler.endDelivery();
        try {
            deliveryControler.updateDeliveryStatus(deliveryId, newStatus);
            System.out.println("Delivery status updated successfully!");
        } catch (Exceptions.InputNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating delivery status: " + e.getMessage());
        }

        // Return to the delivery menu
        showDeliveryMenu();

    }
}
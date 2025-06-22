package Deliveries.Presentation;

import Deliveries.DTO.CargoDTO;
import Deliveries.DTO.OrderDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Service.DeliveryControler;

import java.util.ArrayList;
import java.util.List;
import Deliveries.DTO.*;
/**
 * OrderView.java
 * This class handles the user interface for managing orders.
 * It allows users to create, view, and delete orders.
 */
public class OrderView {
    static DeliveryControler deliveryControler = new DeliveryControler();

    // Display the order management menu
    public static void showOrderMenu() {
        System.out.println("=========================== Order Management ===========================");
        System.out.println("1. Create Order");
        System.out.println("2. View Orders");
        //System.out.println("3. Update Order");
        System.out.println("3. Delete Order");
        System.out.println("4. Back to Main Menu");
        System.out.print("Select an option: ");
        int choice = ConsoleInput.getChoice("Select an option (1-4): ", 1, 4);
        processOrderMenuChoice(choice);
    }

    // Process the user's choice from the order menu
    private static void processOrderMenuChoice(int choice) {
        switch (choice) {
            case 1:
                // Create Order functionality
                showCreateOrder();
                break;
            case 2:
                // View Orders functionality
                showOrders();
                break;
            case 3:
                // Delete Order functionality
                showDeleteOrder();
                break;
            case 4:
                UI.showMainMenu();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                showOrderMenu();
        }
    }

    public static void showCreateOrder() {
        System.out.println("=========================== Create Order ===========================");
        String orderID = ConsoleInput.getString("Enter Order ID: ");
        String address = ConsoleInput.getString("Enter address: ");
        String siteID = ConsoleInput.getString("Enter Site ID: ");
        String shippingZone = ConsoleInput.getString("Enter Shipping Zone: ");

        // Get cargo items
        List<CargoDTO> cargoList = new ArrayList<>();
        while (true) {
            String addMore = ConsoleInput.getString("Do you want to add cargo? (yes/no): ");
            if (addMore.equalsIgnoreCase("no")) {
                break;
            } else if (!addMore.equalsIgnoreCase("yes")) {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                continue;
            }
            String cargoID = ConsoleInput.getString("Enter Cargo ID: ");
            String description = ConsoleInput.getString("Enter Cargo Description: ");
            double weight = ConsoleInput.getDouble("Enter Cargo Weight (kg): ");
            int quantity = ConsoleInput.getInt("Enter Cargo Quantity: ");
            CargoDTO cargo = new CargoDTO(cargoID,orderID, description, weight, quantity);
            cargoList.add(cargo);
        }

        // Print the order details for confirmation
        System.out.println("==== Order Details: ====");
        System.out.println("Order ID: " + orderID);
        System.out.println("Site ID: " + siteID);
        System.out.println("Shipping Zone: " + shippingZone);
        System.out.println("Cargo List:");
        for (CargoDTO cargo : cargoList) {
            System.out.println("Cargo ID: " + cargo.id());
            System.out.println("Description: " + cargo.description());
            System.out.println("Weight: " + cargo.weight() + " kg");
            System.out.println("Quantity: " + cargo.quantity());
        }

        // Create an OrderDTO and add it using the DTO-based method
        OrderDTO order = new OrderDTO(orderID, siteID, shippingZone, false);
        try {
            deliveryControler.addOrder(order, cargoList);
        } catch (Exceptions.InputNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            showOrderMenu();
            return;
        } catch (Exceptions.AlreadyExist e) {
            System.out.println(e.getMessage());
            showOrderMenu();
            return;
        }catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
            showOrderMenu();
            return;
        }
        System.out.println("Order created successfully!");
        showOrderMenu();
    }


    // View all orders
    public static void showOrders() {
        System.out.println("==== Orders ====");
        try {
            System.out.println(deliveryControler.allOrdersToString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        showOrderMenu();
    }

    // Delete an order with exception handling
    public static void showDeleteOrder() {
        System.out.println("==== Delete Order ====");
        String orderID = ConsoleInput.getString("Enter Order ID to delete: ");
        try {
            deliveryControler.removeOrder(orderID);
        } catch (Exceptions.InputNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            showOrderMenu();
            return;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            showOrderMenu();
            return;
        }
        System.out.println("Order deleted successfully!");
        showOrderMenu();
    }
}




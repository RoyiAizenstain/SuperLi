package Deliveries.Presentation;

import Deliveries.DTO.ShippingZoneDTO;
import Deliveries.DTO.SiteDTO;
import Deliveries.DTO.TruckDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.Site;
import Deliveries.Service.DeliveryControler;
import com.sun.tools.javac.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UI class is responsible for handling the user interface of the application.
 * It provides methods to display information and interact with the user.
 */
public class UI {
    static DeliveryControler deliveryControler = new DeliveryControler();

    /// Main method to start the application
    public static void showMainMenu() {
        System.out.println("=========================== Super-Li Transportation System ===========================");
        System.out.println("1. Delivery Management");
        System.out.println("2. Order Management");
        System.out.println("3. Truck Management");
        System.out.println("4. Driver Management");
        System.out.println("5. Shipping Zone Management");
        System.out.println("0. Exit");

        int choice = ConsoleInput.getChoice("Select an option (0-5): ", 0, 5);
        processMainMenuChoice(choice);

    }

    // Process the user's choice from the main menu
    private static void processMainMenuChoice(int choice) {
        switch (choice) {
            case 0:
                System.out.println("Exiting system. Goodbye!");
                break;
            case 1:
                // Transport Management functionality
                DeliveryView.showDeliveryMenu();
                break;
            case 2:
                // Order Management functionality
                OrderView.showOrderMenu();
                break;
            case 3:
                ResourcesView.showTruckMenu();
                break;
            case 4:
                ResourcesView.showDriverMenu();
                break;
            case 5:
                // Site Management functionality
                ShippingZoneView.showShippingZoneMenu();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                showMainMenu();

        }
    }

}

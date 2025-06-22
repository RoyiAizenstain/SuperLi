package Deliveries.Presentation;

import Deliveries.DTO.ShippingZoneDTO;
import Deliveries.DTO.SiteDTO;
import Deliveries.Domain.DeliveryManager;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.Site;
import Deliveries.Service.DeliveryControler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Deliveries.DTO.*;
/**
 * ShippingZoneView.java
 * This class handles the user interface for managing shipping zones.
 * It allows users to add, delete, and view shipping zones.
 */
public class ShippingZoneView {
    static DeliveryControler deliveryControler = new DeliveryControler();

    /// Display the shipping zone management menu
    public static void showShippingZoneMenu() {
        System.out.println("=========================== Shipping Zone Management ===========================");
        System.out.println("1. Add Shipping Zone");
        System.out.println("2. Delete Shipping Zone");
        System.out.println("3. View Shipping Zones");
        System.out.println("4. Back to Main Menu");
        int choice = ConsoleInput.getChoice("Select an option (1-4): ", 1, 4);
        processShippingZoneMenuChoice(choice);
    }

    /// Process the user's choice from the shipping zone menu
    private static void processShippingZoneMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addShippingZone();
                break;
            case 2:
                deleteShippingZone();
                break;
            case 3:
                // View Shipping Zones functionality
                viewShippingZones();
                break;
            case 4:
                UI.showMainMenu();
                return; // Back to the main menu
            default:
                System.out.println("Invalid option. Please try again.");
                showShippingZoneMenu();
        }
    }

    // Add a new shipping zone using DTO
    private static void addShippingZone() {
        System.out.println("=========================== Add Shipping Zone ===========================");
        String zoneID = ConsoleInput.getString("Enter Shipping Zone ID: ");
        String description = ConsoleInput.getString("Enter Shipping Zone Description: ");

        // Collect SiteDTO objects for the zone
        List<SiteDTO> sites = new ArrayList<>();
        boolean addMore = true;
        while (addMore) {
            String siteID = ConsoleInput.getString("- Enter site ID: ");
            String address = ConsoleInput.getString("- Enter site address: ");
            String contactPerson = ConsoleInput.getString("- Enter contact person: ");
            String contactNumber = ConsoleInput.getString("- Enter contact number: ");
            String siteName = ConsoleInput.getString("- Enter site name: ");
            SiteDTO site = new SiteDTO(siteID, address, contactPerson, contactNumber, siteName);
            sites.add(site);
            addMore = ConsoleInput.getBoolean("Do you want to add another site? (yes/no): ");
        }
        if (sites.isEmpty()) {
            System.out.println("You need to supply sites to add shipping zone.");
            showShippingZoneMenu();
        }

        ShippingZoneDTO shippingZoneDTO = new ShippingZoneDTO(zoneID, description, sites);
        try {
            deliveryControler.addShippingZone(shippingZoneDTO);
            System.out.println("Shipping zone added successfully!");
        } catch (Exceptions.AlreadyExist | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        showShippingZoneMenu();
    }

    /// Delete an existing shipping zone
    private static void deleteShippingZone() {
        System.out.println("=========================== Delete Shipping Zone ===========================");
        String zoneID = ConsoleInput.getString("Enter Shipping Zone ID: ");
        try {
            deliveryControler.removeShippingZone(zoneID);
        } catch (Exceptions.InputNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            showShippingZoneMenu();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        showShippingZoneMenu();
    }

    // View all shipping zones with exception handling
    private static void viewShippingZones() {
        System.out.println("=========================== View Shipping Zones ===========================");
        try {
            System.out.println(deliveryControler.allShippingZonesToStrings());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        showShippingZoneMenu();
    }
}

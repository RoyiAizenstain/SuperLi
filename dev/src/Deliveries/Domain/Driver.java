package Deliveries.Domain;

import java.util.List;

/**
 * Driver.java
 * This class represents a driver in the transportation system.
 * It contains information about the driver's name, ID, license type,
 * phone number, license number, and availability status.
 */
public class Driver {
    // Driver class represents a driver in the transportation system.
    private String name;
    private String driverID;
    private List<Trucktype> licenseType;
    private String phoneNumber;
    private String licenseNumber;
    private boolean availability;

    // Constructor
    public Driver(String name, String driverID, Trucktype licenseType, String phoneNumber, String licenseNumber, boolean availability) {
        this.name = name;
        this.driverID = driverID;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        if(licenseType == null) {
            this.licenseType = List.of();
        }
        else {
            this.licenseType = List.of(licenseType);
        }
        this.availability = availability;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getDriverID() {
        return driverID;
    }

    public boolean hasLicenseType(Trucktype truckType) {
        return licenseType.contains(truckType);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    // Check if the driver is available
    public boolean isAvailability() {
        return availability;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addLicenseType(Trucktype licenseType) {
        this.licenseType.add(licenseType);
    }

    public void removeLicenseType(Trucktype licenseType) {
        this.licenseType.remove(licenseType);
    }



    // Print driver details
    public void printDriverDetails() {
        System.out.println("Driver ID: " + driverID);
        System.out.println("Name: " + name);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("License Number: " + licenseNumber);
        System.out.println("Availability: " + availability);
    }

    // Override toString method for better readability
    public String toString() {
        if(licenseType.isEmpty()) {
            return "Driver ID: " + driverID + "\n" +
                    "Name: " + name + "\n" +
                    "Phone Number: " + phoneNumber + "\n" +
                    "License Number: not assign yet " + "\n" +
                    "Availability: " + true + "\n" +
                    "License Type: not assign yet";
        }

        StringBuilder licenseTypes = new StringBuilder();
        for (Trucktype type : licenseType) {
            licenseTypes.append(type).append(" ");
        }
        return "Driver ID: " + driverID + "\n" +
                "Name: " + name + "\n" +
                "Phone Number: " + phoneNumber + "\n" +
                "License Number: " + licenseNumber + "\n" +
                "Availability: " + availability + "\n" +
                "License Types: " + licenseTypes.toString().trim();
    }


}


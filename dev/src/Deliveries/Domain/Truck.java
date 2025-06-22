package Deliveries.Domain;

/**
 * Truck class represents a truck in the transportation system.
 * It contains information about the truck's ID, type, license plate,
 * tare weight, maximum weight, and availability status.
 * It also provides methods to manage the truck's weight and print its details.
 */
public class Truck {
    // Truck class represents a truck in the transportation system.
    private String truckID;
    private Trucktype truckType;
    private String licensePlate;
    private double tareWeight;
    private double maxWeight;
    private boolean availability;

    // Constructor
    public Truck(String truckID, Trucktype truckType, String licensePlate, double tareWeight, double maxWeight, boolean availability) {
        this.truckID = truckID;
        this.truckType = truckType;
        this.licensePlate = licensePlate;
        this.tareWeight = tareWeight;
        this.maxWeight = maxWeight;
        this.availability = availability;
    }

    // Getters and Setters
    public String getTruckID() {
        return truckID;
    }

    public Trucktype getTruckType() {
        return truckType;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public double getTareWeight() {
        return tareWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    // Method to check if the truck is available
    public boolean isAvailability() {
        return availability;
    }

    // Method to check if the truck can carry a given weight
    public void addWeight(double weight) {
        this.tareWeight += weight;
    }

    // Method to check if the truck can carry a given weight
    public void removeWeight(double weight) {
        this.tareWeight -= weight;
    }

    // Method to check if the truck can carry a given weight
    public void printTruckDetails() {
        System.out.println("Truck ID: " + truckID);
        System.out.println("Truck Type: " + truckType);
        System.out.println("License Plate: " + licensePlate);
        System.out.println("Tare Weight: " + tareWeight);
        System.out.println("Max Weight: " + maxWeight);
        System.out.println("Availability: " + availability);
    }

    public String toString() {
        return "Truck ID: " + truckID + ", Type: " + truckType + ", License Plate: " + licensePlate +
                ", Tare Weight: " + tareWeight + ", Max Weight: " + maxWeight + ", Availability: " + availability;
    }


}

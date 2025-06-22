package Deliveries.Domain;

/**
 * Cargo class represents a cargo item with its ID, description, weight, and quantity.
 */
public class Cargo {
    private String CargoID;
    private String description;
    private double weight;          // in kg
    private int quantity;

    // Constructor
    public Cargo(String cargoID, String description, double weight, int quantity) {
        this.CargoID = cargoID;
        this.description = description;
        this.weight = weight;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getCargoID() {
        return CargoID;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCargoID(String cargoID) {
        CargoID = cargoID;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Override toString method for better readability
    public String toString() {
        return "Cargo ID: " + CargoID + ", Description: " + description + ", Weight: " + weight + " kg, Quantity: " + quantity;
    }


}

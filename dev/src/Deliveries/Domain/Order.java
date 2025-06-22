package Deliveries.Domain;

import java.util.List;

/*
 * Order class represents an order in the system.
 * It contains information about the order ID, destination site, shipping zone, and a list of cargo items.
 */
public class Order {
    private String orderId;
    private Site destination;
    private ShippingZone shipingZone;
    private List<Cargo> cargoList;

    // Constructor
    public Order(String orderId, Site destination, ShippingZone shippingZone, List<Cargo> cargoList) {
        this.orderId = orderId;
        this.destination = destination;
        this.shipingZone = shippingZone;
        this.cargoList = cargoList;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Site getDestination() {
        return destination;
    }

    public void setDestination(Site destination) {
        this.destination = destination;
    }

    public ShippingZone getShipingZone() {
        return shipingZone;
    }

    public void setShipingZone(ShippingZone shippingZone) {
        this.shipingZone = shippingZone;
    }

    public List<Cargo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<Cargo> cargoList) {
        this.cargoList = cargoList;
    }

    // Method to calculate the total weight of the cargo items in the order
    double getTotalWeight() {
        double totalWeight = 0;
        for (Cargo cargo : cargoList) {
            totalWeight += cargo.getWeight() * cargo.getQuantity();
        }
        return totalWeight;
    }

    // Override toString method for better readability
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Destination: ").append(destination.getAddress()).append("\n");
        sb.append("Shipping Zone: ").append(shipingZone.getShippingZoneName()).append("\n");
        sb.append("Cargo List: \n");
        for (Cargo cargo : cargoList) {
            sb.append(cargo.toString()).append("\n");
        }
        return sb.toString();
    }
}

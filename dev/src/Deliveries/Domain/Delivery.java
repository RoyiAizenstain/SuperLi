package Deliveries.Domain;

import java.util.Date;
import java.util.List;

public class Delivery {
    // Delivery class represents a delivery in the transportation system.
    private final String deliveryID;
    private Status status;
    private Date deliveryDate;
    private List<Site> destinations;
    private Truck assignedTruck;
    private Driver assignedDriver;
    private List<Order> ordersToDeliver;
    private Site origin;


    // Constructor
    public Delivery(String deliveryID, Status status, Date deliveryDate, List<Site> destinations, Truck assignedTruck, Driver assignedDriver, List<Order> ordersToDeliver, Site origin) {
        this.deliveryID = deliveryID;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.destinations = destinations;
        this.assignedTruck = assignedTruck;
        this.assignedDriver = assignedDriver;
        this.ordersToDeliver = ordersToDeliver;
        this.origin = origin;
    }

    // Getters and Setters
    public String getDeliveryID() {
        return deliveryID;
    }

    public Status getStatus() {
        return status;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public List<Site> getDestinations() {
        return destinations;
    }

    public Truck getAssignedTruck() {
        return assignedTruck;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public List<Order> getOrdersToDeliver() {
        return ordersToDeliver;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void addDestination(Site destination) {
        this.destinations.add(destination);
    }

    // Remove a destination from the delivery
    public void removeDestination(Site destination) {
        this.destinations.remove(destination);
    }

    // Calculate the total weight of the delivery
    public double calculateTotalWeight() {
        return assignedTruck.getTareWeight();
    }

    //getters and setters for assignedTruck and assignedDriver
    public void setAssignedTruck(Truck assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

    public void setAssignedDriver(Driver assignedDriver) {
        this.assignedDriver = assignedDriver;
    }

    // Add an order to the delivery
    public void addOrder(Order order) {
        this.ordersToDeliver.add(order);
        assignedTruck.addWeight(order.getTotalWeight());
    }

    // Remove an order from the delivery
    public void removeOrder(String orderID) {
        ordersToDeliver.removeIf(order -> order.getOrderId().equals(orderID));
        for (Order order : ordersToDeliver) {
            if (order.getOrderId().equals(orderID)) {
                assignedTruck.removeWeight(order.getTotalWeight());
                break;
            }
        }
    }

    // Getters and Setters for the origin
    public Site getOrigin() {
        return origin;
    }

    public void setOrigin(Site origin) {
        this.origin = origin;
    }

    //print the delivery details
    public void printDeliveryDetails() {
        System.out.println("Delivery ID: " + deliveryID);
        System.out.println("Status: " + status);
        System.out.printf("Delivery Date: %td-%tm-%tY%n", deliveryDate, deliveryDate, deliveryDate);
        System.out.println("Assigned Truck: " + assignedTruck.getTruckID());
        System.out.println("Assigned Driver: " + assignedDriver.getDriverID());
        System.out.println("Origin: " + origin.getAddress());
    }

    // Override toString method for better readability
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Delivery ID: ").append(deliveryID).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Delivery Date: ").append(deliveryDate).append("\n");
        sb.append("Assigned Truck: ").append(assignedTruck.getTruckID()).append("\n");
        sb.append("Assigned Driver: ").append(assignedDriver.getDriverID()).append("\n");
        sb.append("Origin: ").append(origin.getAddress()).append("\n");
        sb.append("Destinations: \n");
        for (Site destination : destinations) {
            sb.append("  - ").append(destination.getAddress()).append("\n");
        }
        return sb.toString();
    }

}



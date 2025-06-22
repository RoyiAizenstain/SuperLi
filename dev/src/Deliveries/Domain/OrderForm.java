package Deliveries.Domain;

/*this class represents the order form that is used to create an order
 * it contains the order ID, the destination site and the order itself
 *
 * */
public class OrderForm {
    private String orderID;
    private Site destination;
    private Order order;
    private static int formNumberCounter = 0;


    public OrderForm(String orderID, Site destination, Order order) {
        this.orderID = orderID;
        this.destination = destination;
        this.order = order;
    }

    //getters and setters
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Site getDestination() {
        return destination;
    }

    public void setDestination(Site destination) {
        this.destination = destination;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void incrementFormNumberCounter() {
        formNumberCounter++;
    }

    // Method to get the current form number
    public void printOrderForm() {
        System.out.println("==== ORDER FORM #" + formNumberCounter + " ====");
        System.out.println("Order ID: " + orderID);

        // Print destination details
        System.out.println("\nDestination Information:");
        System.out.println("  Address: " + destination.getAddress());
        System.out.println("  Contact: " + destination.getContactPerson() + " (" + destination.getPhoneNumber() + ")");

        // Print cargo items
        System.out.println("\nCargo Items:");
        for (Cargo cargo : order.getCargoList()) {
            System.out.println("  - " + cargo.getDescription() + " (ID: " + cargo.getCargoID() + ")");
            System.out.println("    Quantity: " + cargo.getQuantity());
            System.out.println("    Weight per unit: " + cargo.getWeight() + " kg");
            System.out.println("    Total weight: " + (cargo.getWeight() * cargo.getQuantity()) + " kg");
        }

        // Print total order weight
        System.out.println("\nTotal Order Weight: " + order.getTotalWeight() + " kg");
        System.out.println("============================");
    }

    //ToString method to print the order form
    public String toString() {
        return "OrderForm{" +
                "orderID='" + orderID + '\'' +
                ", destination=" + destination +
                ", order=" + order +
                '}';
    }

}

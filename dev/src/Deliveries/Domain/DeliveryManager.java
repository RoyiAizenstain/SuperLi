package Deliveries.Domain;

import Deliveries.DTO.*;
import Deliveries.Domain.Repository.ShippingZoneRepositoryImpl;
import Deliveries.Domain.Repository.TruckRepositoryImpl;
import Deliveries.Domain.Repository.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DeliveryManager.java
 * This class manages the delivery process, including planning and executing deliveries.
 */
public class DeliveryManager {
    // Maps to store deliveries, drivers, orders, sites, trucks, and shipping zones

    private static int formNumberCounter = 0;

    private DeliveriesRepositoryImpl deliveriesRepository;
    private DriverRepositoryImpl driversRepository;
    private TruckRepositoryImpl trucksRepository;
    private ShippingZoneRepositoryImpl shippingZoneRepository;
    private OrderRepositoryImpl orderRepository;
    private SiteRepositoryImpl sitesRepository;
    private CargoRepositoryImpl cargoRepository;



    // Constructor
    public DeliveryManager() {

        this.deliveriesRepository = DeliveriesRepositoryImpl.getInstance();
        this.driversRepository = DriverRepositoryImpl.getInstance();
        this.trucksRepository = TruckRepositoryImpl.getInstance();
        this.shippingZoneRepository = ShippingZoneRepositoryImpl.getInstance();
        this.orderRepository = OrderRepositoryImpl.getInstance();
        this.sitesRepository = SiteRepositoryImpl.getInstance();
        this.cargoRepository = CargoRepositoryImpl.getInstance();
    }

    public void connectDao(String url) throws Exception {
        this.sitesRepository.connectDao(url);
        this.shippingZoneRepository.connectDao(url);
        this.driversRepository.connectDao(url);
        this.cargoRepository.connectDao(url);
        this.orderRepository.connectDao(url);
        this.trucksRepository.connectDao(url);
        this.deliveriesRepository.connectDao(url);
    }

    /*
     * planDelivery method is responsible for planning a delivery based on the provided parameters.
     * It checks the availability of drivers and trucks, assigns orders to the delivery,
     * and updates the status of the delivery.
     */
    public String planDelivery(Date deliveryDate, String shippingZoneId, String originId, List<String> prefersOrdersToDeliver, String time) throws Exception {
        // Step 1: Validate inputs
        String delveryID = "";
        ShippingZone shippingZone = shippingZoneRepository.getShippingZoneById(shippingZoneId);
        if (shippingZone == null) throw new Exceptions.InputNotFoundException("Shipping Zone not found");
        Site origin = sitesRepository.getSiteById(originId);
        if (origin == null) throw new Exceptions.InputNotFoundException("Origin site not found");

        // Step 2: Get available destination sites with storekeeper
        List<Site> availableSites = sitesRepository.getAllSitesWithStoreKeeper(shippingZoneId, deliveryDate, time);
        if (availableSites.isEmpty()) throw new Exceptions.DeliveryNotCompletedException("No available delivery sites found due to lack of available storekeepers."

        );

        // Step 3: Filter orders to available destination sites
        List<Order> allOrders = orderRepository.getAllOrders();
        Set<String> availableSiteIds = availableSites.stream()
                .map(Site::getSiteID)
                .collect(Collectors.toSet());

        List<Order> zoneOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (availableSiteIds.contains(order.getDestination().getSiteID())
                    && order.getShipingZone().getShippingZoneID().equals(shippingZoneId)) {
                zoneOrders.add(order);
            }
        }

        if (zoneOrders.isEmpty()) throw new Exceptions.DeliveryNotCompletedException("No orders found for delivery in the selected shipping zone");

        // Step 4: Prioritize preferred orders
        if (prefersOrdersToDeliver != null) {
            Collections.reverse(prefersOrdersToDeliver);
            for (String preferredId : prefersOrdersToDeliver) {
                zoneOrders.stream()
                        .filter(o -> o.getOrderId().equals(preferredId))
                        .findFirst()
                        .ifPresent(order -> {
                            zoneOrders.remove(order);
                            zoneOrders.add(0, order);
                        });
            }
        }

        // Step 5: Find available driver and truck for the given time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(deliveryDate);

        List<Driver> availableDrivers = driversRepository.getAvailableDrivers(dateStr, time);
        List<Truck> allTrucks = trucksRepository.getAllTrucks();
        boolean deliveryCreated = false;

        for (Driver driver : availableDrivers) {
            if (driver == null || !driver.isAvailability()) continue;

            for (Truck truck : allTrucks) {
                if (!truck.isAvailability()) continue;
                if (!driver.hasLicenseType(truck.getTruckType())) continue;

                double currentWeight = truck.getTareWeight();
                List<Order> assignedOrders = new ArrayList<>();
                List<Site> destinations = new ArrayList<>();

                for (Order order : zoneOrders) {
                    double orderWeight = order.getTotalWeight();
                    if (currentWeight + orderWeight <= truck.getMaxWeight()) {
                        currentWeight += orderWeight;
                        assignedOrders.add(order);
                        destinations.add(order.getDestination());
                    }
                }

                if (!assignedOrders.isEmpty()) {
                    // Step 6: Finalize delivery
                    truck.addWeight(currentWeight - truck.getTareWeight());
                    String deliveryId = UUID.randomUUID().toString();

                    Delivery delivery = new Delivery(
                            deliveryId,
                            Status.IN_PROGRESS,
                            deliveryDate,
                            destinations,
                            truck,
                            driver,
                            assignedOrders,
                            origin
                    );

                    DeliveryDTO dto = new DeliveryDTO(
                            delivery.getDeliveryID(),
                            delivery.getStatus().toString(),
                            delivery.getDeliveryDate(),
                            delivery.getAssignedTruck().getTruckID(),
                            delivery.getAssignedDriver().getDriverID(),
                            delivery.getOrigin().getSiteID(),
                            new ArrayList<>(delivery.getOrdersToDeliver().stream().map(Order::getOrderId).toList()),
                            new ArrayList<>(delivery.getDestinations().stream().map(Site::getSiteID).toList())

                    );

                    deliveriesRepository.addDelivery(dto);
                    delveryID = delivery.getDeliveryID();


                    // Step 7: Update statuses
                    //driversRepository.updateDriver(driver);
                    //trucksRepository.updateTruck(truck);

                    for (Order o : assignedOrders) {
                        orderRepository.markOrderAsShipped(o.getOrderId());
                    }

                    // Step 8: Print and generate forms
                    delivery.printDeliveryDetails();
                    for (Order order : assignedOrders) {
                        generateOrderForm(order, order.getDestination(), formNumberCounter);
                    }
                    deliveryCreated = true;
                    break; // Done with truck
                }
            }
            if (deliveryCreated) break; // Done with a driver
        }
        if (!deliveryCreated) {
            throw new Exceptions.DeliveryNotCompletedException("No available driver or truck meets the requirements for this delivery."
            );
        }
        return delveryID;
    }


    //Method to generate an order form
    public void generateOrderForm(Order order, Site source, int formNumberCounter) throws Exceptions.InputNotFoundException {
        // Implementation for removing an order
        if (order == null) {
            throw new Exceptions.InputNotFoundException("Order not found");

        }
        if (source == null) {
            throw new Exceptions.InputNotFoundException("Site not found");
        }
        if (formNumberCounter < 0)
            formNumberCounter = 0;
        OrderForm orderForm = new OrderForm(order.getOrderId(), order.getDestination(), order);
        orderForm.printOrderForm();
        orderForm.incrementFormNumberCounter();

    }

    // Method to add an order using DTOs
    public void addOrder(OrderDTO orderDTO, List<CargoDTO> cargoDTOS) throws Exception {
        orderRepository.addOrder(orderDTO);
        for (CargoDTO cargoDTO : cargoDTOS) {
            cargoRepository.addCargo(cargoDTO);
        }
    }

    //Method to add a truck
    public void addTruck(TruckDTO truck) throws  SQLException {
        trucksRepository.addTruck(truck);
    }

    //Method to remove a truck
    public void removeTruck(String truckID) throws Exceptions.InputNotFoundException, SQLException {
        trucksRepository.removeTruck(truckID);
    }


    //Method to remove a driver
    public void removeDriver(String driverID) throws Exception {
        driversRepository.removeDriver(driverID);
    }

    //Method to add a site
    public void addSite(SiteDTO siteDTO, int zoneId) throws Exceptions.AlreadyExist, SQLException {
        sitesRepository.addSite(siteDTO, zoneId);
    }

    //Method to remove a site
    public void removeSite(String siteID) throws Exceptions.InputNotFoundException, SQLException {
       sitesRepository.removeSite(siteID);
    }

    //Method to add a shipping zone
    public void addShippingZone(ShippingZoneDTO zone) throws  SQLException {
      shippingZoneRepository.addShippingZone(zone);
    }

    //Method to remove a shipping zone
    public void removeShippingZone(String zoneID) throws  SQLException {
        shippingZoneRepository.removeShippingZone(zoneID);
    }

    //Method to remove an order
    public void removeOrder(String orderID) throws Exception {
        Order order = orderRepository.getOrderById(orderID);
        if (order == null) {
            throw new Exceptions.InputNotFoundException("Order not found");
        }
        orderRepository.deleteOrder(orderID);
        // Remove associated cargo
        cargoRepository.getCargoByOrderId(orderID).forEach(cargo -> {
            try {
                cargoRepository.removeCargo(cargo.getCargoID());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Method to remove a delivery
    public String shippingZonesToString() throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (ShippingZone zone : shippingZoneRepository.getAllShippingZones()) {
            sb.append(zone.toString()).append("\n");
        }
        return sb.toString();
    }

    //Method to remove a delivery
    public String driversToString() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Driver driver : driversRepository.getAllDrivers()) {
            sb.append(driver.toString()).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }

    //Method to remove a delivery
    public String trucksToString() throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (Truck truck : trucksRepository.getAllTrucks()) {
            sb.append(truck.toString()).append("\n");
        }
        return sb.toString();
    }

    //Method to remove a delivery
    public String ordersToString() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Order order : orderRepository.getAllOrders()) {
            sb.append(order.toString()).append("\n");
        }
        return sb.toString();
    }

    //Method to remove a delivery
    public String deliveriesToString() throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (Delivery delivery : deliveriesRepository.getAllDeliveries()) {
            sb.append(delivery.toString()).append("\n");
        }
        return sb.toString();
    }

    //Method to remove a delivery
    public void updateDeliveryStatus(String deliveryID, String status) throws Exceptions.InputNotFoundException, SQLException {
        Delivery delivery = deliveriesRepository.getDeliveryById(deliveryID);
        if (delivery == null) {
            throw new Exceptions.InputNotFoundException("Delivery not found");
        }
        Status newStatus = null;
        try {
            newStatus = Status.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new Exceptions.InputNotFoundException("Invalid status. Expected: IN_PROGRESS, COMPLETED, or PENDING");
        }
        deliveriesRepository.getDeliveryById(deliveryID).setStatus(newStatus);
    }
    public Delivery getDeliveryById(String deliveryID) throws Exceptions.InputNotFoundException, SQLException {
        Delivery delivery = deliveriesRepository.getDeliveryById(deliveryID);
        if (delivery == null) {
            throw new Exceptions.InputNotFoundException("Delivery not found");
        }
        return delivery;
    }

    public void deleteDelivery(String deliveryID) throws  SQLException {
        deliveriesRepository.removeDelivery(deliveryID);
    }
    public List<Truck> getAllTrucks() throws Exception {
        return trucksRepository.getAllTrucks();
    }
    public void addLicenceDetailsForDriver(String driverID, String licenseType, String licenseNumber) throws Exception {
        if(driversRepository.getDriverById(driverID) == null) {
            throw new Exceptions.InputNotFoundException("Driver not found");
        }
        driversRepository.addLicenseToDriver(driverID, licenseType, licenseNumber);
    }

}

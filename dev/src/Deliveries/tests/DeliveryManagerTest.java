package Deliveries.tests;

import Deliveries.Domain.*;
import Deliveries.Domain.Repository.*;
import employee.DataAccess.UserDao;
import org.testng.annotations.Test;
import org.junit.jupiter.api.BeforeEach;
import Deliveries.DAO.*;
import Deliveries.DTO.*;
import java.sql.SQLException;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import static org.junit.jupiter.api.Assertions.*;


public class DeliveryManagerTest {

    private DeliveryManager deliveryManager;

    @BeforeEach
    public void setUp() {
    }

    // tests for Repository

    @Test
    public void testGetAllOrdersReturnsList() throws Exception {
        SiteRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        ShippingZoneRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        CargoRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        OrderRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");

        List<Order> orders = OrderRepositoryImpl.getInstance().getAllOrders();
        assertNotNull(orders);
        assertTrue(orders instanceof List);
    }
    @Test
    public void testGetSiteById_ExistingSite() throws Exception {
        SiteRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        Site site = SiteRepositoryImpl.getInstance().getSiteById("1");
        assertNotNull(site);
        assertEquals("1", site.getSiteID());
    }
    @Test
    public void testGetAvailableDrivers() throws Exception {
        DriverRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        String date = "2025-05-11";
        String time = "10:00";

        List<Driver> availableDrivers = DriverRepositoryImpl.getInstance().getAvailableDrivers(date,time);
        for (Driver d : availableDrivers) {
            assertTrue(d.isAvailability());
        }
    }
    @Test
    public void testGetTruckById_ExistingTruck() throws Exception {
        TruckRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        Truck truck = TruckRepositoryImpl.getInstance().getTruckById("T1");
        assertNotNull(truck);
        assertEquals("T1", truck.getTruckID());
    }
    @Test
    public void testGetTruckById_NonExistentTruck() throws Exception {
        TruckRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        Truck truck = TruckRepositoryImpl.getInstance().getTruckById("2");
        assertNull(truck, "Expected null when requesting a non-existent truck by ID");
    }

    @Test
    public void testCargoRepository() throws SQLException {

        CargoRepositoryImpl cargoRepository = CargoRepositoryImpl.getInstance();
        cargoRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        CargoDTO cargoDTO = new CargoDTO(
                "C6",// cargo_id
                "2",
                "Chairs", // description
                100.0, // weight
                4 // quantity
        );
        // Add cargo
        cargoRepository.addCargo(cargoDTO);

        // Retrieve cargo
        Cargo retrievedCargo = cargoRepository.getCargoById("C6");
        assertNotNull(retrievedCargo, "Cargo should be retrieved from the repository");
        assertEquals("Chairs", retrievedCargo.getDescription(), "Cargo description should match");
        assertEquals(100, retrievedCargo.getWeight(), "Cargo weight should match");
        cargoRepository.removeCargo("C6");
        //check if the cargo is removed
        Cargo removedCargo = cargoRepository.getCargoById("C6");
        assertNull(removedCargo, "Cargo should be removed from the repository");
    }

    @Test
    public void testTruckRepository() throws Exception {
        TruckRepositoryImpl truckRepository = TruckRepositoryImpl.getInstance();
        truckRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        TruckDTO truckDTO = new TruckDTO(
                "123",
                "A",
                "EA-123",
                100.0,
                2000.0,
                true
        );
        truckRepository.addTruck(truckDTO);
        Truck retrievedTruck = truckRepository.getTruckById("123");
        assertNotNull(retrievedTruck, "Truck should be retrieved from the repository");
        assertEquals("EA-123", retrievedTruck.getLicensePlate(), "Truck license plate should match");
        truckRepository.removeTruck("123");
        Truck removedTruck = truckRepository.getTruckById("123");
        assertNull(removedTruck, "Truck should be removed from the repository");
    }

    @Test
    public void testSiteRepository() throws Exception {
        SiteRepositoryImpl siteRepository = SiteRepositoryImpl.getInstance();
        siteRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        SiteDTO siteDTO = new SiteDTO(
                "89",
                "tel aviv 24",
                "shay",
                "0525600526",
                "super-li"
        );
        siteRepository.addSite(siteDTO, 2);
        Site retrievedSite = siteRepository.getSiteById("89");
        assertNotNull(retrievedSite, "Site should be retrieved from the repository");
        assertEquals("tel aviv 24", retrievedSite.getAddress(), "Site address should match");
        siteRepository.removeSite("89");
        Site removedSite = siteRepository.getSiteById("89");
        assertNull(removedSite, "Site should be removed from the repository");
        List<Site> allSites = siteRepository.getAllSites();
        assertFalse(allSites.isEmpty(), "Site repository should not be empty after adding a site");

    }

    @Test
    public void testGetAllShippingZones() throws SQLException {
        ShippingZoneRepositoryImpl zoneRepository = ShippingZoneRepositoryImpl.getInstance();
        zoneRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");

        List<ShippingZone> zones = zoneRepository.getAllShippingZones();

        assertNotNull(zones, "Shipping zones list should not be null");
        assertFalse(zones.isEmpty(), "Shipping zones list should not be empty");

        boolean hasNorth = zones.stream().anyMatch(zone -> zone.getShippingZoneName().equals("North Zone"));
        assertTrue(hasNorth, "There should be a zone named 'North'");
    }

    @Test
    public void testOrderRepository() throws Exception {
        OrderRepositoryImpl orderRepository = OrderRepositoryImpl.getInstance();
        ShippingZoneRepositoryImpl shippingZoneRepository = ShippingZoneRepositoryImpl.getInstance();
        CargoRepositoryImpl cargoRepository = CargoRepositoryImpl.getInstance();
        SiteRepositoryImpl siteRepository = SiteRepositoryImpl.getInstance();

        shippingZoneRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        siteRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        cargoRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        orderRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");


        // Create a new order
        OrderDTO orderDTO = new OrderDTO("O100", "1", "Z1", false);
        orderRepository.addOrder(orderDTO);

        // Retrieve the order
        Order retrievedOrder = orderRepository.getOrderById("O100");
        assertNotNull(retrievedOrder, "Order should be retrieved from the repository");
        assertEquals("O100", retrievedOrder.getOrderId(), "Order ID should match");

        // Remove the order
        orderRepository.deleteOrder("O100");
        Order removedOrder = orderRepository.getOrderById("O100");
        assertNull(removedOrder, "Order should be removed from the repository");
    }

    // test for delivery manager

    @Test
    public void testAddOrder() throws Exception {
        deliveryManager = new DeliveryManager();

        OrderDTO orderDTO = new OrderDTO("O1", "1", "1", false);
        CargoDTO cargoDTO = new CargoDTO("C1", "O1", "Chairs", 100.0, 4);

        deliveryManager.addOrder(orderDTO, List.of(cargoDTO));

        System.out.println("Order added successfully!");

        deliveryManager.removeOrder("O1");

    }

    @Test
    public void testAddDelivery() throws Exception {
        SiteRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        ShippingZoneRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        CargoRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        TruckRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        DriverRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        OrderRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        DeliveriesRepositoryImpl deliveriesRepository = DeliveriesRepositoryImpl.getInstance();
        deliveriesRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");

        OrderDTO orderDTO = new OrderDTO("123", "1", "1", false);
        CargoDTO cargoDTO = new CargoDTO("C5", "123", "Chairs", 100.0, 4);

       CargoRepositoryImpl.getInstance().addCargo(cargoDTO);
        OrderRepositoryImpl.getInstance().addOrder(orderDTO);

        String deliveryId = "D002";
        String status = "PENDING";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date deliveryDate = sdf.parse("2025-06-10");

        String truckId = "T1";
        String driverId = "D5";
        String originId = "1";
        ArrayList<String> orderIds = new ArrayList<>(List.of("123"));
        ArrayList<String> destinationIds = new ArrayList<>(List.of("S002"));

        DeliveryDTO dto = new DeliveryDTO(deliveryId, status, deliveryDate, truckId, driverId, originId, orderIds, destinationIds);

        deliveriesRepository.addDelivery(dto);

        Delivery added = deliveriesRepository.getDeliveryById(deliveryId);
        assertNotNull(added, "Delivery should be in the Database");
        assertEquals(deliveryId, added.getDeliveryID(), "deliveryId");
        assertEquals(Status.PENDING, added.getStatus(), "Status should be Pending");
        assertEquals(1, added.getOrdersToDeliver().size(), "there should be one order to deliver");
        OrderRepositoryImpl.getInstance().deleteOrder("123");
        CargoRepositoryImpl.getInstance().removeCargo("C5");
        deliveriesRepository.removeDelivery(deliveryId);

        assertNull(deliveriesRepository.getDeliveryById(deliveryId), "delivery should be removed");
    }

    @Test
    public void testDeleveriesDao() throws SQLException {
        DeliveriesDAO deliveriesDAO = new DeliveriesDAO("jdbc:sqlite:dev/src/Resources/my_database.db");
        DeliveryDTO deliveryDTO = new DeliveryDTO(
                "D01",
                "Pending",
                new Date(),
                "T1",
                "D5",
                "1",
                new ArrayList<>(List.of("O1")),
                new ArrayList<>(List.of("S002"))
        );

        deliveriesDAO.addDelivery(deliveryDTO);

        // Verify the delivery was added
        List<DeliveryDTO> deliveries = deliveriesDAO.getDeliveries();
        assertFalse(deliveries.isEmpty(), "Deliveries list should not be empty");
        assertEquals("D01", deliveries.get(deliveries.size()-1).id(), "Delivery ID should match");

        // Clean up
        deliveriesDAO.deleteDelivery("D01");

    }
    @Test
    public void testGetAvailableSitesWithStoreKeeper() throws Exception {
        // Arrange
        String dbUrl = "jdbc:sqlite:dev/src/Resources/my_database.db";
        SiteDAO siteDAO = new SiteDAO(dbUrl);
        String zoneId = "1";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date deliveryDate = sdf.parse("2025-05-11");
        String time = "10:00";

        // Act
        List<SiteDTO> availableSites = siteDAO.getAvailableSitesWithStoreKeeper(zoneId, deliveryDate, time);

        // Assert
        assertNotNull(availableSites, "Result should not be null");
        assertFalse(availableSites.isEmpty(), "At least one site should be available with a Store_Keeper");

        for (SiteDTO site : availableSites) {
            assertNotNull(site.id(), "Site ID should not be null");
            assertNotNull(site.name(), "Site name should not be null");
            System.out.println(" Site with Store_Keeper: " + site.id() + " - " + site.name());
        }

        System.out.println("Found " + availableSites.size() + " available sites with Store_Keeper for zone " + zoneId);
    }

    @Test
    public void testPlanDelivery_WithValidData_CreatesDeliverySuccessfully() throws Exception {
        deliveryManager = new DeliveryManager();

        OrderDTO orderDTO = new OrderDTO("12", "1", "1", false);
        CargoDTO cargoDTO = new CargoDTO("C2", "12", "Chairs", 100.0, 4);

        deliveryManager.addOrder(orderDTO, List.of(cargoDTO));

        String id = "";
        String zoneId = "1";
        String originId = "1";
        String time = "10:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2025-05-11");

        // Act
        try {
            id = deliveryManager.planDelivery(date, zoneId, originId, null, time);

            // Assert
            System.out.println("Delivery was successfully created for zone: " + zoneId + " at " + time);
            System.out.println(deliveryManager.deliveriesToString());


        } catch (Exception e) {
            // If exception is thrown, we want to know why
            System.out.println("Delivery failed for zone: " + zoneId + " at " + time);
        }
        deliveryManager.deleteDelivery(id);
        deliveryManager.removeOrder("12");

    }
    @Test
    public void getAllDeliveries() throws Exception {
        SiteRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        ShippingZoneRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        CargoRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        TruckRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        DriverRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");
        OrderRepositoryImpl.getInstance().connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");

        DeliveriesRepositoryImpl deliveriesRepository = DeliveriesRepositoryImpl.getInstance();
        deliveriesRepository.connectDao("jdbc:sqlite:dev/src/Resources/my_database.db");

        List<Delivery> deliveries =  deliveriesRepository.getAllDeliveries();

        assertNotNull(deliveries, "Deliveries list should not be null");

    }

    @Test
    public void testPlanDelivery_NoTruckCanCarryWeight_ThrowsException() throws Exception {


        OrderDTO orderDTO = new OrderDTO("13", "1", "1", false);
        CargoDTO cargoDTO = new CargoDTO("C3", "13", "Super Heavy", 50000.0, 1); // כבד מדי

        deliveryManager.addOrder(orderDTO, List.of(cargoDTO));

        String zoneId = "1";
        String originId = "1";
        String time = "13:00";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2025-05-11");

        assertThrows(Exceptions.DeliveryNotCompletedException.class, () -> {
            deliveryManager.planDelivery(date, zoneId, originId, null, time);
        });

        deliveryManager.removeOrder("13");
    }
    @Test
    public void testPlanDelivery_NoAvailableDrivers_ThrowsException() throws Exception {
        deliveryManager = new DeliveryManager();

        OrderDTO orderDTO = new OrderDTO("888", "1", "1", false);
        CargoDTO cargoDTO = new CargoDTO("CG888", "888", "Boxes", 200.0, 5);
        deliveryManager.addOrder(orderDTO, List.of(cargoDTO));


        String zoneId = "1";
        String originId = "1";
        String time = "04:00";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2025-05-11");

        assertThrows(Exceptions.DeliveryNotCompletedException.class, () -> {
            deliveryManager.planDelivery(date, zoneId, originId, null, time);
        });

        deliveryManager.removeOrder("888");
    }
    @Test
    public void testPlanDelivery_NoAvailableStoreKeeperAtDestination_ThrowsException() throws Exception {
        deliveryManager = new DeliveryManager();

        OrderDTO orderDTO = new OrderDTO("999", "1", "1", false);
        CargoDTO cargoDTO = new CargoDTO("CG999", "999", "Electronics", 150.0, 2);
        deliveryManager.addOrder(orderDTO, List.of(cargoDTO));

        String zoneId = "1";
        String originId = "1";
        String time = "05:00";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2025-06-11");

        assertThrows(Exceptions.DeliveryNotCompletedException.class, () -> {
            deliveryManager.planDelivery(date, zoneId, originId, null, time);
        });

        deliveryManager.removeOrder("999");
    }
    @Test
    public void testAddAndRemoveTruck() throws Exception {
        deliveryManager = new DeliveryManager();

        List<Truck> originalTrucks = deliveryManager.getAllTrucks();
        int originalSize = originalTrucks.size();

        TruckDTO newTruck = new TruckDTO("T-999", "A", "tsgs", 2000.0, 10000.0,true);

        deliveryManager.addTruck(newTruck);

        List<Truck> afterAddTrucks = deliveryManager.getAllTrucks();
        assertEquals(originalSize + 1, afterAddTrucks.size(), "Truck count should have increased by 1");

        deliveryManager.removeTruck("T-999");

        List<Truck> afterRemoveTrucks = deliveryManager.getAllTrucks();
        assertEquals(originalSize, afterRemoveTrucks.size(), "Truck count should return to original after deletion");
    }


}

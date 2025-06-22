package Deliveries.Service;

import Deliveries.DTO.ShippingZoneDTO;
import Deliveries.DTO.TruckDTO;
import Deliveries.Domain.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Deliveries.DTO.*;


public class DeliveryControler {
    static DeliveryManager deliveryManager = new DeliveryManager();

    public DeliveryControler() {
    }

    public void planDelivery(Date deliveryDate, String shippingZone, List<String> prefersOrdersToDeliver, String originId, String time) throws Exceptions.DeliveryNotCompletedException, Exceptions.InputNotFoundException {
        try {
            deliveryManager.planDelivery(deliveryDate, shippingZone, originId, prefersOrdersToDeliver, time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void endDelivery() {
    }

    public void addOrder(OrderDTO orderDTO, List<CargoDTO> cargoList) throws Exception {
        deliveryManager.addOrder(orderDTO, cargoList);
    }

    public void removeOrder(String orderID) throws Exception {
        deliveryManager.removeOrder(orderID);
    }

    public void addTruck(TruckDTO truck) throws Exceptions.InputNotFoundException, Exceptions.AlreadyExist, SQLException {
        deliveryManager.addTruck(truck);
    }

    public void removeTruck(String truckID) throws Exceptions.InputNotFoundException, SQLException {
        deliveryManager.removeTruck(truckID);
    }


    public void removeDriver(String driverID) throws Exception {
        deliveryManager.removeDriver(driverID);
    }

    public void addSite(SiteDTO siteDTO, int zoneId) throws Exceptions.AlreadyExist, SQLException {
        // Implementation for adding a site
        deliveryManager.addSite(siteDTO, zoneId);

    }

    public void removeSite(String siteID) throws Exceptions.InputNotFoundException, SQLException {
        // Implementation for removing a site
        deliveryManager.removeSite(siteID);
    }

    public void addShippingZone(ShippingZoneDTO shippingZoneDTO) throws Exceptions.AlreadyExist, SQLException {
        // Implementation for adding a shipping zone
        deliveryManager.addShippingZone(shippingZoneDTO);

    }

    public void removeShippingZone(String zoneID) throws Exceptions.InputNotFoundException, SQLException {
        // Implementation for removing a shipping zone
        deliveryManager.removeShippingZone(zoneID);
    }

    public String allShippingZonesToStrings() throws SQLException {
        // Implementation for printing all shipping zones
        return deliveryManager.shippingZonesToString();
    }

    public String allOrdersToString() throws Exception {
        // Implementation for printing all orders
        return deliveryManager.ordersToString();
    }

    public String allDriversToString() throws Exception {
        // Implementation for printing all drivers
        return deliveryManager.driversToString();
    }

    public String allTrucksToString() throws SQLException {
        // Implementation for printing all trucks
        return deliveryManager.trucksToString();
    }

    public String allDeliveriesToString() throws SQLException {
        // Implementation for printing all deliveries
        return deliveryManager.deliveriesToString();
    }

    public void updateDeliveryStatus(String deliveryID, String status) throws Exceptions.InputNotFoundException, SQLException {
        // Implementation for updating delivery status
        deliveryManager.updateDeliveryStatus(deliveryID, status);
    }


    public void addLicenceDetailsForDriver(String driverID, String licenseType, String licenseNumber) throws Exception {
        // Implementation for adding license details for a driver
        deliveryManager.addLicenceDetailsForDriver(driverID, licenseType, licenseNumber);

    }
}

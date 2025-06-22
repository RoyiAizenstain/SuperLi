package Deliveries.Domain.Repository;

import Deliveries.DTO.DeliveryDTO;
import Deliveries.Domain.Delivery;
import Deliveries.Domain.Exceptions;
import employee.Exceptions.UserNotFound;

import java.sql.SQLException;
import java.util.List;

public interface DeliveriesRepository {
    // Define methods for interacting with delivery data, such as adding, removing, or retrieving deliveries.

    void addDelivery(DeliveryDTO delivery) throws Exception;
    void removeDelivery(String deliveryId) throws SQLException;
    Delivery getDeliveryById(String deliveryId) throws SQLException;
    List<Delivery> getAllDeliveries() throws SQLException;
    void connectDao(String url) throws Exception;
    public void loadDeliveries() throws Exception;

}

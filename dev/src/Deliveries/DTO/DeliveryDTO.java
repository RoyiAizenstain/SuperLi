package Deliveries.DTO;

import java.util.ArrayList;
import java.util.Date;

// This class represents a Data Transfer Object (DTO) for Delivery information.
public record DeliveryDTO(String id,
                          String status,
                          Date deliveryDate,
                          String truckId,
                          String driverId,
                          String originId,
                          ArrayList<String> orderIds,
                          ArrayList<String> destinationIds

) {}

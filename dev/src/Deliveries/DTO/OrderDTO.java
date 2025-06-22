package Deliveries.DTO;

// This file defines the OrderDTO record, which is used to encapsulate order form data.
public record OrderDTO(
        String id,
        String destinationID,
        String shippingZoneID,
        boolean isShipped
    ) {
}

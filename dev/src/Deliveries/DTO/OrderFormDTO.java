package Deliveries.DTO;

//TODO: It's not complete yet, but it will be used to create a new order form
//This file defines the OrderFormDTO record, which is used to encapsulate order form data.
public record OrderFormDTO(
        String orderId,
        String orderDate,
        String orderStatus,
        String customerName,
        String customerAddress,
        String customerPhoneNumber,
        String customerEmail,
        String shippingZoneId,
        String siteId,
        String truckId,
        String driverId) {
}

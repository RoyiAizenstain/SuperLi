package Deliveries.DTO;
// This class represents a Data Transfer Object (DTO) for Cargo information.
public record CargoDTO(
        String id,
        String orderId,
        String description,
        double weight,
        int quantity
) {
}

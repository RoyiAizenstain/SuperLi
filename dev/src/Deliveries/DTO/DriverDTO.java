package Deliveries.DTO;

// this is a Data Transfer Object (DTO) for Driver information
public record DriverDTO(
        String id,
        String name,
        String licenseType,
        String phoneNumber,
        String licenseNumber,
        boolean availability
       ) {
}

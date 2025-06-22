package Deliveries.DTO;

//this class represents a Data Transfer Object (DTO) for Truck information
public record TruckDTO(
        String truckId,
        String truckType,
        String licensePlate,
        Double tareWeight,
        Double maxWeight,
        Boolean availability
)  {

}

package Deliveries.Domain.Repository;
import Deliveries.DTO.DriverDTO;
import Deliveries.Domain.Driver;

import java.util.List;

public interface DriverRepository {
    // Define methods for interacting with driver data, such as adding, removing, or retrieving drivers.

    void removeDriver(String driverId) throws Exception;
    Driver getDriverById(String driverId) throws Exception;
    List<Driver> getAllDrivers() throws Exception;
    void connectDao(String url) throws Exception;
    public void loadDrivers() throws Exception;
    public void addLicenseToDriver(String driverId, String licenseType, String licenseNumber) throws Exception;
    public List<Driver> getAvailableDrivers(String date, String time) throws Exception;

}

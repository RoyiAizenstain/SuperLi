package Deliveries.Domain.Repository;

import Deliveries.DAO.DriverDAO;
import Deliveries.DTO.DriverDTO;
import Deliveries.Domain.Driver;
import Deliveries.Domain.Trucktype;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRepositoryImpl implements DriverRepository{
    private DriverDAO driverDAO;
    private Map<String, Driver> driverMap;
    private static DriverRepositoryImpl instance;


    public static DriverRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new DriverRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void removeDriver(String driverId) throws Exception {
        // Implementation code here
        driverDAO.removeDriver(driverId);
        driverMap.remove(driverId);
    }

    @Override
    public Driver getDriverById(String driverId) throws Exception {
        DriverDTO dto = driverDAO.getDriverById(driverId);
        if (dto == null) return null;
        Trucktype trucktype = Trucktype.valueOf(dto.licenseType().toUpperCase());
        return new Driver(dto.name(),dto.id(), trucktype, dto.phoneNumber(), dto.licenseNumber(), dto.availability());
    }


    @Override
    public List<Driver> getAllDrivers() throws Exception {
        List<DriverDTO> driverDTOs = driverDAO.getDrivers();
        List<Driver> drivers = new ArrayList<Driver>();
        for (DriverDTO dto : driverDTOs) {
            Trucktype trucktype;
            try {
                trucktype = Trucktype.valueOf(dto.licenseType().toUpperCase());
            } catch (IllegalArgumentException e) {
                trucktype = null;
            }
            Driver driver = new Driver(dto.id(), dto.name(), trucktype, dto.phoneNumber(), dto.licenseNumber(), dto.availability());
            drivers.add(driver);
            driverMap.put(dto.id(), driver);
        }
        return drivers;
    }

    @Override
    public void connectDao(String url) throws Exception {
        driverDAO = new DriverDAO(url);
        driverMap = new HashMap<String, Driver>();
        loadDrivers(); // Load existing drivers from the database

    }

    public void loadDrivers() throws Exception {
        List<DriverDTO> driverDTOs = driverDAO.getDrivers();
        for (DriverDTO dto : driverDTOs) {
            Trucktype trucktype = null;
            try {
                if(dto.licenseType() != "") {
                    trucktype = Trucktype.valueOf(dto.licenseType().toUpperCase());
                }
            } catch (IllegalArgumentException e) {
                trucktype = null;
            }
            Driver driver = new Driver(dto.id(), dto.name(), trucktype, dto.phoneNumber(), dto.licenseNumber(), dto.availability());
            driverMap.put(dto.id(), driver);
        }
    }

    public void addLicenseToDriver(String driverId, String licenseType, String licenseNumber) throws Exception {
        loadDrivers();
        driverDAO.addLicenceDetails(driverId, licenseType, licenseNumber);
        loadDrivers();

    }

    public List<Driver> getAvailableDrivers(String date, String time) throws Exception {
        loadDrivers();
        List<DriverDTO> availableDriversDTO = driverDAO.getAvailableDrivers(date, time);
        List<Driver> availableDrivers = new ArrayList<>();
        for (DriverDTO dto : availableDriversDTO) {
            Driver driver = getDriverById(dto.id());
            if (driver != null && dto.availability()) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }
    public void updateDriver(Driver driver) throws Exception {
        loadDrivers();
        driverDAO.updateDriverAvailability(driver.getDriverID(), driver.isAvailability());
    }



}

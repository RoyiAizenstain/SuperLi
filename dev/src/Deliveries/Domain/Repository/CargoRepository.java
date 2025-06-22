package Deliveries.Domain.Repository;

import Deliveries.DTO.CargoDTO;
import Deliveries.Domain.Cargo;
import employee.Exceptions.UserNotFound;

import java.sql.SQLException;
import java.util.List;

public interface CargoRepository {
    // Define methods for interacting with cargo data, such as adding, removing, or retrieving cargo.
    void addCargo(CargoDTO cargo) throws SQLException;
    void removeCargo(String cargoId) throws SQLException;
    Cargo getCargoById(String cargoId) throws SQLException;
    List<Cargo> getAllCargo() throws SQLException;
    void connectDao(String url) throws UserNotFound, SQLException;
    public void loadCargo() throws SQLException;
    public List<Cargo> getCargoByOrderId(String orderId) throws SQLException;

}


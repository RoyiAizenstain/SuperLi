package Deliveries.Domain.Repository;



import Deliveries.DTO.TruckDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.Truck;
import Deliveries.Domain.Trucktype;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TruckRepository {

    Truck getTruckById(String truckId) throws SQLException, Exceptions.InputNotFoundException;

    List<Truck> getAllTrucks() throws SQLException;

    void addTruck(TruckDTO truck) throws SQLException;

    void removeTruck(String truckId) throws SQLException, Exceptions.InputNotFoundException;

    void connectDao(String url) throws SQLException;

    public void loadTrucks() throws SQLException;



}

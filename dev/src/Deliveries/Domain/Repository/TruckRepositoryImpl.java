package Deliveries.Domain.Repository;

import Deliveries.DAO.CargoDAO;
import Deliveries.DAO.TruckDAO;
import Deliveries.DTO.TruckDTO;
import Deliveries.Domain.Cargo;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.Truck;
import Deliveries.Domain.Trucktype;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruckRepositoryImpl implements TruckRepository {
    private TruckDAO truckDao;
    private Map<String,Truck> trucks;
    private static TruckRepositoryImpl instance;

    public static TruckRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new TruckRepositoryImpl();
        }
        return instance;
    }

    @Override
    public List<Truck> getAllTrucks() throws SQLException {
        List<TruckDTO> dtos = truckDao.getTrucks();
        List<Truck> truckList = new ArrayList<>();
        for (TruckDTO dto : dtos) {
            Truck truck = new Truck(dto.truckId(),Trucktype.valueOf(dto.truckType()),dto.licensePlate(),dto.tareWeight(),dto.maxWeight(),dto.availability());
            truckList.add(truck);
            trucks.put(dto.truckId(), truck);
        }
        return truckList;
    }


    @Override
    public Truck getTruckById(String truckId) throws SQLException, Exceptions.InputNotFoundException {
        Truck truck = trucks.get(truckId);
        if (truck != null) {
            return truck;
        }
        return null;

    }

    @Override
    public void addTruck(TruckDTO truck) throws SQLException {
        truckDao.addTruck(truck);
        Truck newTruck = new Truck(truck.truckId(),Trucktype.valueOf(truck.truckType()),truck.licensePlate(),truck.tareWeight(),truck.maxWeight(),truck.availability());
        trucks.put(truck.truckId(),newTruck);
    }

    @Override
    public void removeTruck(String truckId) throws SQLException, Exceptions.InputNotFoundException {
        Truck truck = getTruckById(truckId);
        if (truck != null) {
            trucks.remove(truckId);
            truckDao.removeTruck(truckId);
        } else {
            throw new Exceptions.InputNotFoundException("Truck not found");
        }
    }
    public void loadTrucks() throws SQLException {
        List<TruckDTO> dtos = truckDao.getTrucks();
        for (TruckDTO dto : dtos) {
            Truck truck = new Truck(dto.truckId(),Trucktype.valueOf(dto.truckType()),dto.licensePlate(),dto.tareWeight(),dto.maxWeight(),dto.availability());
            trucks.put(dto.truckId(), truck);
        }
    }
    @Override
    public void connectDao(String url) throws SQLException {
        truckDao = new TruckDAO(url);
        trucks = new HashMap<String, Truck>();
        loadTrucks(); // Load existing cargo from the database
    }
    public void updateTruck(Truck truck) throws SQLException {
        truckDao.updateTruckAvailability(truck.getTruckID());
    }
}


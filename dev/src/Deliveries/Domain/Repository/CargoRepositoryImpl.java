package Deliveries.Domain.Repository;

import Deliveries.DAO.CargoDAO;
import Deliveries.DTO.CargoDTO;
import Deliveries.Domain.Cargo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoRepositoryImpl implements CargoRepository {
    private CargoDAO cargoDAO;
    private Map<String, Cargo> cargoMap;
    private static CargoRepositoryImpl instance;

    public static CargoRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new CargoRepositoryImpl();
        }
        return instance;
    }
    @Override
    public void connectDao(String url) throws SQLException {
        cargoDAO = new CargoDAO(url);
        cargoMap = new HashMap<String,Cargo>();
        loadCargo(); // Load existing cargo from the database
    }


    @Override
    public void addCargo(CargoDTO cargo) throws SQLException {
        // Implementation code here
        cargoDAO.addCargo(cargo);
        Cargo newCargo = new Cargo(cargo.id(), cargo.description(), cargo.weight(), cargo.quantity());
        cargoMap.put(cargo.id(), newCargo);
    }

    @Override
    public void removeCargo(String cargoId) throws SQLException {
        // Implementation code here
        cargoDAO.removeCargo(cargoId);
        cargoMap.remove(cargoId);
    }

    @Override
    public Cargo getCargoById(String cargoId) throws SQLException {
        // Implementation code here
        Cargo cargo = cargoMap.get(cargoId);
        if (cargo != null) {
            return cargo;
        }
        return null; // Placeholder return statement
    }

    @Override
    public List<Cargo> getAllCargo() throws SQLException {
        // Implementation code here
        List<CargoDTO> cargoDTOs = cargoDAO.getCargos();
        List<Cargo> cargoList = new ArrayList<>();
        for (CargoDTO cargoDTO : cargoDTOs) {
            Cargo cargo = new Cargo(cargoDTO.id(), cargoDTO.description(), cargoDTO.weight(), cargoDTO.quantity());
            cargoList.add(cargo);
            cargoMap.put(cargoDTO.id(), cargo);
        }
        return cargoList; // Placeholder return statement
    }

    public void loadCargo() throws SQLException {
        // Load cargo from the database
        List<CargoDTO> cargoDTOs = cargoDAO.getCargos();
        for (CargoDTO cargoDTO : cargoDTOs) {
            Cargo cargo = new Cargo(cargoDTO.id(), cargoDTO.description(), cargoDTO.weight(), cargoDTO.quantity());
            cargoMap.put(cargoDTO.id(), cargo);
        }
    }
    public List<Cargo> getCargoByOrderId(String orderId) throws SQLException {
        // Implementation code here
        List<CargoDTO> cargoDTOs = cargoDAO.getCargoByOrderId(orderId);
        List<Cargo> cargos = new ArrayList<>();
        for (CargoDTO cargoDTO : cargoDTOs) {
            Cargo cargo = new Cargo(cargoDTO.id(), cargoDTO.description(), cargoDTO.weight(), cargoDTO.quantity());
            cargos.add(cargo);
            cargoMap.put(cargoDTO.id(), cargo);
        }
        return cargos; // Placeholder return statement
    }

}

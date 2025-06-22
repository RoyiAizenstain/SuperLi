package Deliveries.Domain.Repository;

import Deliveries.DTO.ShippingZoneDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.ShippingZone;

import java.sql.SQLException;
import java.util.List;

public interface ShippingZoneRepository {

    ShippingZone getShippingZoneById(String zoneId) throws Exceptions.InputNotFoundException, SQLException;
    List<ShippingZone> getAllShippingZones() throws SQLException;
    void addShippingZone(ShippingZoneDTO zone) throws SQLException;
    void removeShippingZone(String zoneId) throws SQLException;
    void connectDao(String url) throws SQLException;
    public void loadShippingZones() throws SQLException;
}

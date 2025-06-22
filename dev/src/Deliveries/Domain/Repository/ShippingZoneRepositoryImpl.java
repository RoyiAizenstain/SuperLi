package Deliveries.Domain.Repository;

import Deliveries.DAO.ShippingZoneDAO;
import Deliveries.DTO.ShippingZoneDTO;
import Deliveries.DTO.SiteDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.ShippingZone;
import Deliveries.Domain.Site;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShippingZoneRepositoryImpl implements ShippingZoneRepository {
    private ShippingZoneDAO zoneDAO;
    private Map<String,ShippingZone> zones;
    private static ShippingZoneRepositoryImpl instance;

    public static ShippingZoneRepositoryImpl getInstance(){
        if(instance == null){
            instance = new ShippingZoneRepositoryImpl();
        }
        return instance;
    }

    @Override
    public ShippingZone getShippingZoneById(String zoneId) throws Exceptions.InputNotFoundException, SQLException {
        ShippingZone zone = zones.get(zoneId);
        if(zone != null){
            return zone;
        }
        return null;

    }
    @Override
    public List<ShippingZone> getAllShippingZones() throws SQLException {
        List<ShippingZoneDTO> dtos = zoneDAO.getShippingZones();
        List<ShippingZone> zonesList = new ArrayList<>();

        for (ShippingZoneDTO dto : dtos) {
            List<SiteDTO> siteDTOs = zoneDAO.getSitesByZoneId(dto.shippingZoneID());

            List<Site> siteList = new ArrayList<>();
            for (SiteDTO siteDto : siteDTOs) {
                Site site = new Site(
                        siteDto.id(),
                        siteDto.address(),
                        siteDto.contactPerson(),
                        siteDto.phoneNumber(),
                        siteDto.name()
                );
                siteList.add(site);
            }

            ShippingZone zone = new ShippingZone(
                    dto.shippingZoneID(),
                    dto.shippingZoneName(),
                    siteList
            );

            zonesList.add(zone);
            zones.put(dto.shippingZoneID(), zone);
        }

        return zonesList;
    }

    @Override
    public void addShippingZone(ShippingZoneDTO zone) throws SQLException {
        zoneDAO.addShippingzone(zone);
        List<Site> domainSites = zone.sites().stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());

        ShippingZone newZone = new ShippingZone(zone.shippingZoneID(), zone.shippingZoneName(), domainSites);
        zones.put(zone.shippingZoneID(), newZone);
    }

    @Override
    public void removeShippingZone(String zoneId) throws SQLException {
        zoneDAO.removeShippingZone(zoneId);
        zones.remove(zoneId);

    }
    public void loadShippingZones() throws SQLException {
        List<ShippingZoneDTO> dtos = zoneDAO.getShippingZones();
        for (ShippingZoneDTO dto : dtos) {
            List<Site> domainSites = dto.sites().stream()
                    .map(this::convertToDomain)
                    .collect(Collectors.toList());
            ShippingZone Szone = new ShippingZone(dto.shippingZoneID(),dto.shippingZoneName(),domainSites);
            zones.put(dto.shippingZoneID(), Szone);
        }
    }
    @Override
    public void connectDao(String url) throws SQLException {
        zoneDAO = new ShippingZoneDAO(url);
        zones = new HashMap<String, ShippingZone>();
        loadShippingZones(); // Load existing cargo from the database
    }
    private Site convertToDomain(SiteDTO dto) {
        return new Site(dto.id(), dto.name(), dto.address(), dto.contactPerson(), dto.phoneNumber());
    }




}

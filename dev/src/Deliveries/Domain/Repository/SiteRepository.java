package Deliveries.Domain.Repository;

import Deliveries.DTO.SiteDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.Site;

import java.sql.SQLException;
import java.util.List;

public interface SiteRepository {
    // Define methods for interacting with site data, such as adding, removing, or retrieving sites.

     void addSite(SiteDTO site, int zoneId) throws SQLException;
     void removeSite(String siteId) throws SQLException, Exceptions.InputNotFoundException;
     Site getSiteById(String siteId) throws SQLException, Exceptions.InputNotFoundException;
     List<Site> getAllSites() throws SQLException;
     void connectDao(String url) throws SQLException;
     public void loadSites() throws SQLException;
}

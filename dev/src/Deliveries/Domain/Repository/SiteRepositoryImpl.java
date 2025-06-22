package Deliveries.Domain.Repository;

import Deliveries.DAO.SiteDAO;
import Deliveries.DTO.SiteDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.Site;

import java.sql.SQLException;
import java.util.*;

public class SiteRepositoryImpl implements SiteRepository {
    private SiteDAO siteDao;
    private static SiteRepositoryImpl instance;
    private Map<String,Site> sites;

    public static SiteRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new SiteRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void addSite(SiteDTO site, int zoneId) throws SQLException {
        siteDao.addSite(site, zoneId);
        Site newSite = new Site(site.id(), site.address(), site.contactPerson(), site.phoneNumber(), site.name());
        sites.put(site.id(), newSite);
    }

    @Override
    public void removeSite(String siteId) throws SQLException, Exceptions.InputNotFoundException {
        Site site = getSiteById(siteId);
        if (site != null) {
            siteDao.removeSite(siteId);
            sites.remove(siteId);
            // Logic to remove the site from the database or storage
        } else {
            throw new Exceptions.InputNotFoundException("Site id " + siteId + " not found");
        }

    }
    @Override
    public Site getSiteById(String siteId) {
       Site site = sites.get(siteId);
       if (site != null) {
           return site;
       }
       return null;
    }
    @Override
    public List<Site> getAllSites() throws SQLException{
        List<SiteDTO> dtos = siteDao.getSites();
        List<Site> siteList = new ArrayList<>();
        for (SiteDTO dto : dtos) {
            Site site = new Site(dto.id(), dto.address(), dto.contactPerson(), dto.phoneNumber(), dto.name());
            siteList.add(site);
            sites.put(dto.id(), site);
        }
        return siteList;

    }

    public void loadSites() throws SQLException {
        List<SiteDTO> dtos = siteDao.getSites();
        for (SiteDTO dto : dtos) {
            Site site = new Site(dto.id(), dto.address(), dto.contactPerson(), dto.phoneNumber(), dto.name());
            sites.put(dto.id(), site);
        }
    }

    @Override
    public void connectDao(String url) throws SQLException {
        siteDao = new SiteDAO(url);
        sites = new HashMap<String, Site>();
        loadSites();
    }

    public List<Site> getAllSitesWithStoreKeeper(String zoneId, Date date, String time) throws SQLException {
        List<Site> sites = new ArrayList<>();
        List<SiteDTO> siteDTOS = siteDao.getAvailableSitesWithStoreKeeper(zoneId, date, time);
        for (SiteDTO dto : siteDTOS) {
            Site site = new Site(dto.id(),  dto.address(), dto.contactPerson(), dto.phoneNumber(), dto.name());
            sites.add(site);
        }
        return sites;
    }

}

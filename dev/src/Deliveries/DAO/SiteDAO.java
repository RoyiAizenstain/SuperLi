package Deliveries.DAO;

import Deliveries.DTO.SiteDTO;
import Deliveries.Util.SQLiteClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SiteDAO {
    private SQLiteClient sqliteClient;

    public SiteDAO(String url) {
        this.sqliteClient =  SQLiteClient.getInstance(url);
    }

    public List<SiteDTO> getSites() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        List<SiteDTO> sites = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Sites");
        createTable();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            sites.add(new SiteDTO(
                    rs.getString("site_id"),
                    rs.getString("address"),
                    rs.getString("contact_person"),
                    rs.getString("phone_number"),
                    rs.getString("site_name")));
        }
        pstmt.close();
        return sites;
    }

    private void createTable() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Sites (" +
                "site_id INTEGER PRIMARY KEY," +
                "address TEXT NOT NULL," +
                "contact_person TEXT NOT NULL," +
                "phone_number TEXT NOT NULL," +
                "site_name TEXT NOT NULL" +
                ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();

    }
    public void addSite(SiteDTO site, int zoneId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "INSERT INTO Sites (site_id, address, contact_person, phone_number, site_name) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, site.id());
        pstmt.setString(2, site.address());
        pstmt.setString(3, site.contactPerson());
        pstmt.setString(4, site.phoneNumber());
        pstmt.setString(5, site.name());
        pstmt.executeUpdate();
        pstmt.close();


        addSiteToShippingZone(site.id(), zoneId);
    }

    public void addSiteToShippingZone(String siteId, int zoneId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "INSERT INTO ShippingZone_Sites (shipping_zone_id, site_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, zoneId);
            pstmt.setString(2, siteId);
            pstmt.executeUpdate();
        }

    }
    public void removeSite(String siteID) throws SQLException {
        Connection connection = sqliteClient.getConnection();

        // First, delete the links in ShippingZone_Sites table
        String deleteLinkSql = "DELETE FROM ShippingZone_Sites WHERE site_id = ?";
        PreparedStatement linkStmt = connection.prepareStatement(deleteLinkSql);
        linkStmt.setString(1, siteID);
        linkStmt.executeUpdate();
        linkStmt.close();

        // Then, delete the site from Sites table
        String deleteSiteSql = "DELETE FROM Sites WHERE site_id = ?";
        PreparedStatement siteStmt = connection.prepareStatement(deleteSiteSql);
        siteStmt.setString(1, siteID);
        siteStmt.executeUpdate();
        siteStmt.close();
    }
    public SiteDTO getSiteById(String siteId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "SELECT * FROM Sites WHERE site_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, siteId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            SiteDTO site = new SiteDTO(
                    rs.getString("site_id"),
                    rs.getString("address"),
                    rs.getString("contact_person"),
                    rs.getString("phone_number"),
                    rs.getString("site_name"));
            pstmt.close();
            return site;
        } else {
            pstmt.close();
            return null; // or throw an exception if preferred
        }
    }
    public List<SiteDTO> getSitesByDeliveryId(String deliveryId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "SELECT s.* FROM Sites s " +
                "JOIN Deliveries_Sites ds ON s.site_id = ds.site_id " +
                "WHERE ds.delivery_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, deliveryId);
        ResultSet rs = pstmt.executeQuery();
        List<SiteDTO> sites = new ArrayList<>();
        while (rs.next()) {
            sites.add(new SiteDTO(
                    rs.getString("site_id"),
                    rs.getString("address"),
                    rs.getString("contact_person"),
                    rs.getString("phone_number"),
                    rs.getString("site_name")));
        }
        pstmt.close();
        return sites;
    }
    public List<SiteDTO> getAvailableSitesWithStoreKeeper(String zoneId, Date date, String time) throws SQLException {
        List<SiteDTO> availableSites = new ArrayList<>();
        Connection connection = sqliteClient.getConnection();

        String sql = """
    WITH Input AS (
        SELECT ? AS target_date, ? AS target_time
    )
    SELECT DISTINCT s.*
    FROM Sites s
    JOIN ShippingZone_Sites szs ON s.site_id = szs.site_id
    JOIN employees e ON e.branch = s.site_id
    JOIN currentWeekWorkers w ON e.id = w.id
    JOIN currentWeekShifts sh ON w.date = sh.date AND w.day = sh.day AND w.shift_type = sh.shift_type
    JOIN Input i ON 1=1
    WHERE szs.shipping_zone_id = ?
      AND w.role = 'Store_Keeper'
      AND w.date = i.target_date
      AND TIME(i.target_time) BETWEEN TIME(SUBSTR(sh.time, 1, 5)) AND TIME(SUBSTR(sh.time, 7, 5));
    """;

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(date));
        pstmt.setString(2, time);
        pstmt.setString(3, zoneId);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            availableSites.add(new SiteDTO(
                    rs.getString("site_id"),
                    rs.getString("address"),
                    rs.getString("contact_person"),
                    rs.getString("phone_number"),
                    rs.getString("site_name")
            ));
        }

        return availableSites;
    }


}

package Deliveries.DAO;

import Deliveries.DTO.ShippingZoneDTO;

import Deliveries.DTO.SiteDTO;
import Deliveries.Util.SQLiteClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingZoneDAO {
    private SQLiteClient sqliteClient;

    //constructor to initialize the SQLiteClient with the database URL
    public ShippingZoneDAO(String url) {
        this.sqliteClient =  SQLiteClient.getInstance(url);
    }

    //get all shipping zones from the database
    public List<ShippingZoneDTO> getShippingZones() throws SQLException {
        createTable();
        Connection connection = sqliteClient.getConnection();
        List<ShippingZoneDTO> zones = new ArrayList<>();

        PreparedStatement zoneStmt = connection.prepareStatement("SELECT * FROM ShippingZones");
        ResultSet zoneRs = zoneStmt.executeQuery();

        while (zoneRs.next()) {
            String zoneId = zoneRs.getString("shipping_zone_id");
            String zoneName = zoneRs.getString("shipping_zone_name");

            // שימוש בפונקציה קיימת במקום לכתוב JOIN כאן
            List<SiteDTO> sites = getSitesByZoneId(String.valueOf(zoneId));

            zones.add(new ShippingZoneDTO(zoneId, zoneName, sites));
        }

        zoneStmt.close();
        return zones;
    }


    private void createTable() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS ShippingZones (" +
                "shipping_zone_id INTEGER PRIMARY KEY," +
                "shipping_zone_name TEXT NOT NULL" +
                ")";

        String linkTableSql = "CREATE TABLE IF NOT EXISTS ShippingZone_Sites (" +
                "shipping_zone_id VARCHAR," +
                "site_id VARCHAR," +
                "PRIMARY KEY (shipping_zone_id, site_id)," +
                "FOREIGN KEY (shipping_zone_id) REFERENCES ShippingZones(shipping_zone_id) ON DELETE CASCADE," +
                "FOREIGN KEY (site_id) REFERENCES Sites(site_id) ON DELETE CASCADE" +
                ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();

    }

    public void addShippingzone(ShippingZoneDTO zone) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "INSERT INTO ShippingZones (shipping_zone_id, shipping_zone_name) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, zone.shippingZoneID());
        pstmt.setString(2, zone.shippingZoneName());
        pstmt.executeUpdate();
        pstmt.close();
    }
    public void removeShippingZone(String zoneID) throws SQLException {
        Connection connection = sqliteClient.getConnection();

        // First, delete the links in the ShippingZone_Sites table
        String deleteLinks = "DELETE FROM ShippingZone_Sites WHERE shipping_zone_id = ?";
        PreparedStatement linkStmt = connection.prepareStatement(deleteLinks);
        linkStmt.setString(1, zoneID);
        linkStmt.executeUpdate();
        linkStmt.close();

        // Then, delete the shipping zone itself
        String deleteZone = "DELETE FROM ShippingZones WHERE shipping_zone_id = ?";
        PreparedStatement zoneStmt = connection.prepareStatement(deleteZone);
        zoneStmt.setString(1, zoneID);
        zoneStmt.executeUpdate();
        zoneStmt.close();
    }
    public List<SiteDTO> getSitesByZoneId(String zoneId) throws SQLException {
        List<SiteDTO> sites = new ArrayList<>();
        Connection connection = sqliteClient.getConnection();

        String sql = """
        SELECT s.site_id, s.address, s.contact_person, s.site_name, s.phone_number
        FROM Sites s
        JOIN ShippingZone_Sites szs ON s.site_id = szs.site_id
        WHERE szs.shipping_zone_id = ?
        """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, zoneId);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            sites.add(new SiteDTO(
                    rs.getString("site_id"),
                    rs.getString("address"),
                    rs.getString("contact_person"),
                    rs.getString("site_name"),
                    rs.getString("phone_number")
            ));
        }

        stmt.close();
        return sites;
    }



}


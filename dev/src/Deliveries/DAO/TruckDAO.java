package Deliveries.DAO;

import Deliveries.DTO.TruckDTO;
import Deliveries.Util.SQLiteClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TruckDAO {
    private SQLiteClient sqliteClient;

    public TruckDAO(String url) {
        this.sqliteClient =  SQLiteClient.getInstance( url);
    }


    public List<TruckDTO> getTrucks() throws SQLException {
       Connection connection = sqliteClient.getConnection();
         List<TruckDTO> trucks = new ArrayList<>();
         PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Trucks");
            // Check if table exists, if not create it
            createTable();
            // Execute query to get all trucks
         ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                trucks.add(new TruckDTO(rs.getString("truck_id"), rs.getString("truck_type")
                        , rs.getString("license_plate"),rs.getDouble("tare_weight") ,rs.getDouble("max_weight"), rs.getBoolean("availability")));
            }
            pstmt.close();
            return trucks;
    }
    private void createTable() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Trucks (" +
                "truck_id  PRIMARY KEY," +
                "truckType TEXT," +
                "licensePlate TEXT," +
                "tareWeight REAL," +
                "maxWeight REAL," +
                "availability BOOLEAN" +
                ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void addTruck(TruckDTO truck) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "INSERT INTO Trucks (truck_id, truck_type, license_plate,tare_weight, max_weight, availability) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, truck.truckId());
        pstmt.setString(2, truck.truckType());
        pstmt.setString(3, truck.licensePlate());
        pstmt.setDouble(4, truck.tareWeight());
        pstmt.setDouble(5, truck.maxWeight());
        pstmt.setBoolean(6, truck.availability());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void close() {
        if (sqliteClient != null) {
            sqliteClient.close();
        }
    }
    public TruckDTO getTruckById(String truckId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "SELECT * FROM Trucks WHERE truck_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, truckId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            TruckDTO truck = new TruckDTO(rs.getString("truck_id"), rs.getString("truck_type"),
                    rs.getString("license_plate"), rs.getDouble("max_weight"), rs.getDouble("tare_weight"),
                    rs.getBoolean("availability"));
            pstmt.close();
            return truck;
        } else {
            pstmt.close();
            return null; // or throw an exception if preferred
        }
    }
    public void removeTruck(String truckId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "DELETE FROM Trucks WHERE truck_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, truckId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    public void updateTruckAvailability(String truckId) throws SQLException {
        String sql = "UPDATE trucks SET availability = NOT availability WHERE truck_id = ?";
        try (Connection conn = sqliteClient.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, truckId);
            stmt.executeUpdate();
        }
    }
}

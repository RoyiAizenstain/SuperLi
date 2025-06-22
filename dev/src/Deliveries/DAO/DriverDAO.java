package Deliveries.DAO;

import Deliveries.DTO.DriverDTO;
import Deliveries.Util.SQLiteClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {
    private SQLiteClient sqliteClient;

    public DriverDAO(String url) {
        this.sqliteClient = SQLiteClient.getInstance(url);
    }

    public List<DriverDTO> getDrivers() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        createDriversLicenceTable(); // Ensure table exists

        List<DriverDTO> drivers = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(
                """
                        SELECT
                            e.id,
                            e.employee_name,
                            dl.license_type,
                            e.phoneNumber,
                            dl.license_number,
                            dl.is_available
                                FROM employees e
                                    JOIN abilitiesAndRoles a ON e.id = a.id
                                    LEFT JOIN DriverLicenses dl ON e.id = dl.driver_id
                                    WHERE a.ability = 'Driving_license' ;
                        """);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            drivers.add(new DriverDTO(
                    rs.getString("id"),
                    rs.getString("employee_name"),
                    rs.getString("license_type") != null ? rs.getString("license_type") : "",
                    rs.getString("phoneNumber"),
                    rs.getString("license_number") != null ? rs.getString("license_number") : "",
                    true));
        }
        pstmt.close();
        return drivers;
    }

    private void createDriversLicenceTable() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS DriverLicenses  (driver_id VARCHAR(10) PRIMARY KEY," +
                "driver_id VARCHAR PRIMARY KEY," +
                "license_type VARCHAR(5) NOT NULL," +
                "license_number VARCHAR(20) NOT NULL," +
                "is_available BOOLEAN DEFAULT TRUE," +
                " FOREIGN KEY (driver_id) REFERENCES Drivers(driver_id)" +
                ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    }

/*
    public void addDriver(DriverDTO driver) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        createDriversLicenceTable() ; // Ensure table exists

        String sql = "INSERT INTO Drivers (driver_id, name, license_type, phone_number, license_number, availability) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, driver.id());
        pstmt.setString(2, driver.name());
        pstmt.setString(3, driver.licenseType());
        pstmt.setString(4, driver.phoneNumber());
        pstmt.setString(5, driver.licenseNumber());
        pstmt.setBoolean(6, driver.availability());
        pstmt.executeUpdate();
        pstmt.close();
    }

 */



    public void removeDriver(String driverId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "DELETE FROM Drivers WHERE driver_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, driverId);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public DriverDTO getDriverById(String driverId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = """
                        SELECT
                            e.id,
                            e.employee_name,
                            dl.license_type,
                            e.phoneNumber,
                            dl.license_number,
                            dl.is_available
                                FROM employees e
                                    JOIN abilitiesAndRoles a ON e.id = a.id
                                    LEFT JOIN DriverLicenses dl ON e.id = dl.driver_id
                                    WHERE a.ability = 'Driving_license' AND e.id = ?;
                        """; // Added filter for driver ID
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, driverId); // Now parameter is properly bound
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            DriverDTO driver = new DriverDTO(
                    rs.getString("id"),
                    rs.getString("employee_name"),
                    rs.getString("license_type") != null ? rs.getString("license_type") : "",
                    rs.getString("phoneNumber"),
                    rs.getString("license_number") != null ? rs.getString("license_number") : "",
                    rs.getBoolean("is_available")); // Default to available
            pstmt.close();
            return driver;
        } else {
            pstmt.close();
            return null;
        }
    }
    public void addLicenceDetails(String driverId, String licenseType, String licenseNumber) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "INSERT INTO DriverLicenses (driver_id, license_type, license_number, is_available) " +
                "VALUES (?, ?, ?, true)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, driverId);
        pstmt.setString(2, licenseType);
        pstmt.setString(3, licenseNumber);
        pstmt.executeUpdate();
        pstmt.close();
    }
    public List<DriverDTO> getAvailableDrivers(String date, String time) throws Exception {
        Connection connection = sqliteClient.getConnection();
        String sql = """
        WITH Input AS (
            SELECT
                ? AS target_date,
                ? AS target_time
        )
        SELECT
            e.id AS employee_id
        FROM employees e
                 JOIN abilitiesAndRoles a ON e.id = a.id
                 JOIN currentWeekWorkers w ON e.id = w.id
                 JOIN currentWeekShifts s ON w.date = s.date AND w.day = s.day AND w.shift_type = s.shift_type
                 JOIN Input i ON 1=1
        WHERE a.ability = 'Driving_license'
          AND w.role = 'Driver'
          AND w.date = i.target_date
          AND TIME(i.target_time) BETWEEN TIME(SUBSTR(s.time, 1, 5)) AND TIME(SUBSTR(s.time, 7, 5));
        """;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, date); // Now parameter is properly bound
        pstmt.setString(2, time); // Now parameter is properly bound
        ResultSet rs = pstmt.executeQuery();
        List<DriverDTO> availableDrivers = new ArrayList<>();
        while (rs.next()) {
            DriverDTO driver = getDriverById(rs.getString("employee_id"));
            if (driver != null && driver.availability()) {
                availableDrivers.add(driver);
            }
        }
        pstmt.close();
        return availableDrivers;
    }
    public void updateDriverAvailability(String driverId, boolean available) throws SQLException {
        String sql = "UPDATE DriverLicenses SET availability = ? WHERE id = ?";
        try (Connection conn = sqliteClient.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setString(2, driverId);
            stmt.executeUpdate();
        }
    }



}
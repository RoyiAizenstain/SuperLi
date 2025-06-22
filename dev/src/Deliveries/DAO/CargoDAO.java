package Deliveries.DAO;

import Deliveries.Util.SQLiteClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Deliveries.DTO.CargoDTO;

public class CargoDAO {
    private SQLiteClient sqliteClient;
    public CargoDAO(String url) {
        this.sqliteClient = SQLiteClient.getInstance(url);
    }
    public List<CargoDTO> getCargos() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        List<CargoDTO> cargoList = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Cargo");
        createTable();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            cargoList.add(new CargoDTO(
                    rs.getString("cargo_id"),
                    rs.getString("order_id"),
                    rs.getString("description"),
                    rs.getDouble("weight"),
                    rs.getInt("quantity")
            ));
        }
        pstmt.close();
        return cargoList;
    }

    private void createTable() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Cargo (" +
                "cargo_id TEXT PRIMARY KEY," +
                "order_id TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "weight REAL NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE" +
                ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    }
    public void addCargo(CargoDTO cargo) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "INSERT INTO Cargo (cargo_id, order_id, description, weight, quantity) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, cargo.id());
        pstmt.setString(2, cargo.orderId());
        pstmt.setString(3, cargo.description());
        pstmt.setDouble(4, cargo.weight());
        pstmt.setInt(5, cargo.quantity());
        pstmt.executeUpdate();
        pstmt.close();
    }
    public void removeCargo(String cargoId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "DELETE FROM Cargo WHERE cargo_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, cargoId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    public List<CargoDTO> getCargoByOrderId(String orderId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        List<CargoDTO> cargoList = new ArrayList<>();
        String sql = "SELECT * FROM Cargo WHERE order_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, orderId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            cargoList.add(new CargoDTO(
                    rs.getString("cargo_id"),
                    rs.getString("order_id"),
                    rs.getString("description"),
                    rs.getDouble("weight"),
                    rs.getInt("quantity")
            ));
        }
        pstmt.close();
        return cargoList;
    }


}

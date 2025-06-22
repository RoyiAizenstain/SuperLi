package Deliveries.DAO;

import Deliveries.DTO.OrderDTO;
import Deliveries.Util.SQLiteClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private SQLiteClient sqliteClient;

    public OrderDAO(String url) {
        this.sqliteClient = SQLiteClient.getInstance(url);
    }

    public List<OrderDTO> getOrders() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        createTable(); // Move this before preparing the statement
        List<OrderDTO> orders = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Orders WHERE is_shipped = 0");
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            orders.add(new OrderDTO(
                    rs.getString("order_id"),
                    rs.getString("destination_id"),
                    rs.getString("shipping_zone_id"),
                    rs.getBoolean("is_shipped")

            ));
        }
        pstmt.close();
        return orders;
    }

    private void createTable() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Orders (" +
                "order_id INTEGER PRIMARY KEY," +
                "destination_id INTEGER NOT NULL," +
                "shipping_zone_id INTEGER NOT NULL," +
                "is_shipped BOOLEAN NOT NULL DEFAULT 0," +
                "FOREIGN KEY (destination_id) REFERENCES Sites(site_id) ON DELETE CASCADE," +
                "FOREIGN KEY (shipping_zone_id) REFERENCES ShippingZones(shipping_zone_id) ON DELETE CASCADE" +
                ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void addOrder(OrderDTO order) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        createTable(); // Add this line to ensure table exists before insertion

        String sql = "INSERT INTO Orders (order_id, destination_id, shipping_zone_id, is_shipped) VALUES (?, ?, ?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, order.id());
        pstmt.setString(2, order.destinationID());
        pstmt.setString(3, order.shippingZoneID());
        pstmt.setBoolean(4, false);

        pstmt.executeUpdate();
        pstmt.close();
    }
    public void deleteOrder(String orderId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, orderId);
        pstmt.executeUpdate();
        pstmt.close();
    }
    public void markOrderAsShipped(String orderId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "UPDATE Orders SET is_shipped = 1 WHERE order_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, orderId);
        pstmt.executeUpdate();
        pstmt.close();

    }
    public void connectDao(String url) throws Exception {
        if (sqliteClient == null) {
            sqliteClient = SQLiteClient.getInstance(url);
        } else {
            throw new Exception("SQLiteClient is already connected.");
        }
    }
    public OrderDTO getOrderById(String orderId) throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            OrderDTO order = new OrderDTO(
                    rs.getString("order_id"),
                    rs.getString("destination_id"),
                    rs.getString("shipping_zone_id"),
                    rs.getBoolean("is_shipped")
            );
            pstmt.close();
            return order;
        } else {
            pstmt.close();
            return null; // No order found with the given ID
        }
    }
}
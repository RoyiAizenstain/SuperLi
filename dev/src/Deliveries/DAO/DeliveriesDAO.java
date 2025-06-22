package Deliveries.DAO;

import Deliveries.DTO.DeliveryDTO;
import Deliveries.Util.SQLiteClient;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//TODO: Test it!
public class DeliveriesDAO {
    private SQLiteClient sqliteClient;
    public DeliveriesDAO(String url) {
        this.sqliteClient = SQLiteClient.getInstance(url);
    }

    public void createTable() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        String sql_deliveries = "CREATE TABLE IF NOT EXISTS Deliveries (" +
                "delivery_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "delivery_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "truck_id INTEGER NOT NULL," +
                "driver_id INTEGER NOT NULL," +
                "origin_id INTEGER NOT NULL," +
                "status VARCHAR(20) NOT NULL," +
                "FOREIGN KEY (driver_id) REFERENCES Drivers(driver_id) ON DELETE CASCADE," +
                "FOREIGN KEY (truck_id) REFERENCES Trucks(truck_id) ON DELETE CASCADE," +
                "FOREIGN KEY (origin_id) REFERENCES Sites(site_id) ON DELETE CASCADE" +
                ")";

        String sql_deliveries_destinations = "CREATE TABLE IF NOT EXISTS Deliveries_Destinations (" +
                "delivery_id INTEGER NOT NULL," +
                "site_id INTEGER NOT NULL," +
                "PRIMARY KEY (delivery_id, site_id)," +
                "FOREIGN KEY (delivery_id) REFERENCES Deliveries(delivery_id) ON DELETE CASCADE," +
                "FOREIGN KEY (site_id) REFERENCES Sites(site_id) ON DELETE CASCADE" +
                ")";

        String sql_deliveries_orders = "CREATE TABLE IF NOT EXISTS Deliveries_Orders (" +
                "delivery_id INTEGER NOT NULL," +
                "order_id INTEGER NOT NULL," +
                "PRIMARY KEY (delivery_id, order_id)," +
                "FOREIGN KEY (delivery_id) REFERENCES Deliveries(delivery_id) ON DELETE CASCADE," +
                "FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE" +
                ")";

        PreparedStatement pstmt = connection.prepareStatement(sql_deliveries);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = connection.prepareStatement(sql_deliveries_destinations);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = connection.prepareStatement(sql_deliveries_orders);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void addDelivery(DeliveryDTO delivery) throws SQLException {
        // Implement the method to add a delivery
        Connection connection = sqliteClient.getConnection();
        createTable();
        String sql = "INSERT INTO Deliveries (delivery_id,status,delivery_date,truck_id,driver_id,origin_id) VALUES (?, ?, ?, ?, ?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, delivery.id());
        pstmt.setString(2, delivery.status());
        // Format date as string in yyyy-MM-dd format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(delivery.deliveryDate());
        pstmt.setString(3, dateStr);
        pstmt.setString(4, delivery.truckId());
        pstmt.setString(5, delivery.driverId());
        pstmt.setString(6, delivery.originId());
        pstmt.executeUpdate();
        String sql_destinations = "INSERT INTO Deliveries_Destinations (delivery_id, site_id) VALUES (?, ?)";
        PreparedStatement pstmt_destinations = connection.prepareStatement(sql_destinations);
        for (String siteId : delivery.destinationIds()) {
            pstmt_destinations.setString(1, delivery.id());
            pstmt_destinations.setString(2, siteId);
            pstmt_destinations.executeUpdate();
        }
        String sql_orders = "INSERT INTO Deliveries_Orders (delivery_id, order_id) VALUES (?, ?)";
        PreparedStatement pstmt_orders = connection.prepareStatement(sql_orders);
        for (String orderId : delivery.orderIds()) {
            pstmt_orders.setString(1, delivery.id());
            pstmt_orders.setString(2, orderId);
            pstmt_orders.executeUpdate();
        }
    }

    public List<DeliveryDTO> getDeliveries() throws SQLException {
        Connection connection = sqliteClient.getConnection();
        createTable();
        List<DeliveryDTO> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM Deliveries";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String id = rs.getString("delivery_id");
            String status = rs.getString("status");
            //read Date format from database
            String deliveryDateStr = rs.getString("delivery_date");
            String truckId = rs.getString("truck_id");
            String driverId = rs.getString("driver_id");
            String originId = rs.getString("origin_id");

            // Fetch destination IDs
            List<String> destinationIds = new ArrayList<>();
            String sqlDestinations = "SELECT site_id FROM Deliveries_Destinations WHERE delivery_id = ?";
            PreparedStatement pstmtDestinations = connection.prepareStatement(sqlDestinations);
            pstmtDestinations.setString(1, id);
            ResultSet rsDestinations = pstmtDestinations.executeQuery();
            while (rsDestinations.next()) {
                destinationIds.add(rsDestinations.getString("site_id"));
            }
            pstmtDestinations.close();

            // Fetch order IDs
            List<String> orderIds = new ArrayList<>();
            String sqlOrders = "SELECT order_id FROM Deliveries_Orders WHERE delivery_id = ?";
            PreparedStatement pstmtOrders = connection.prepareStatement(sqlOrders);
            pstmtOrders.setString(1, id);
            ResultSet rsOrders = pstmtOrders.executeQuery();
            while (rsOrders.next()) {
                orderIds.add(rsOrders.getString("order_id"));
            }
            pstmtOrders.close();
            Date deliveryDate;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                deliveryDate = sdf.parse(deliveryDateStr);
            } catch (Exception e) {
                deliveryDate = new Date(); // Fallback to current date
            }
            deliveries.add(new DeliveryDTO(id, status, deliveryDate, truckId, driverId, originId, new ArrayList<>(orderIds), new ArrayList<>(destinationIds)));
        }
        pstmt.close();
        return deliveries;
    }

    public void deleteDelivery(String deliveryId) throws SQLException {
        // Don't reuse connections across operations; get a fresh one for each statement
            Connection connection = sqliteClient.getConnection();
            // Delete from Deliveries_Orders first
            String sql1 = "DELETE FROM Deliveries_Orders WHERE delivery_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql1);
            pstmt.setString(1, deliveryId);
            pstmt.executeUpdate();

            // Delete from Deliveries_Destinations second
            String sql2 = "DELETE FROM Deliveries_Destinations WHERE delivery_id = ?";
            PreparedStatement pstmt1 = connection.prepareStatement(sql2);
            pstmt1.setString(1, deliveryId);
            pstmt1.executeUpdate();

            // Finally delete from the main Deliveries table
            String sql3 = "DELETE FROM Deliveries WHERE delivery_id = ?";
            PreparedStatement pstmt2 = connection.prepareStatement(sql3);
            pstmt2.setString(1, deliveryId);
            pstmt2.executeUpdate();
            pstmt.close();
            pstmt1.close();
            pstmt2.close();



    }


    public void getDeliveryById (String deliveryId){
        // Implement the method to retrieve a delivery by its ID
    }

}
package Deliveries.Domain.Repository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Deliveries.DTO.OrderDTO;
import Deliveries.Domain.Exceptions;
import Deliveries.Domain.Order;

public interface OrderRepository {

    List<Order> getAllOrders() throws Exception;
    Order getOrderById(String orderId) throws Exception;
    void addOrder(OrderDTO order) throws Exception;
    void deleteOrder(String orderId) throws Exception;
    void connectDao(String url) throws Exception;
    public void loadOrders() throws SQLException, Exceptions.InputNotFoundException;
}

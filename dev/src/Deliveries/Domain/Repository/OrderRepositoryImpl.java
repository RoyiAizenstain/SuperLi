package Deliveries.Domain.Repository;
import Deliveries.DTO.CargoDTO;
import Deliveries.DTO.OrderDTO;
import Deliveries.Domain.*;
import Deliveries.DAO.OrderDAO;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {
    private OrderDAO orderDAO;
    private Map<String, Order> orderMap;
    private static OrderRepositoryImpl instance;
    private static CargoRepositoryImpl cargoRepository = CargoRepositoryImpl.getInstance();
    private static SiteRepositoryImpl siteRepository = SiteRepositoryImpl.getInstance();
    private static ShippingZoneRepositoryImpl shippingZoneRepository = ShippingZoneRepositoryImpl.getInstance();

    public static OrderRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new OrderRepositoryImpl();
        }
        return instance;
    }

    @Override
    public List<Order> getAllOrders() throws Exception {
        List<Order> orderList = new ArrayList<>();
        List<OrderDTO> orderDTOs = orderDAO.getOrders();
        for (OrderDTO orderDTO : orderDTOs) {
            List<Cargo> cargos = cargoRepository.getCargoByOrderId(orderDTO.id());
            if( cargos == null) {
                cargos = List.of();
                // If no cargos found, initialize with an empty list
            }
            Site destination = siteRepository.getSiteById(orderDTO.destinationID());
            ShippingZone shippingZone = shippingZoneRepository.getShippingZoneById(orderDTO.shippingZoneID());
            Order order = new Order(
                    orderDTO.id(),
                    destination,
                    shippingZone,
                    cargos
            );
            orderList.add(order);
            orderMap.put(orderDTO.id(), order);
        }
       return orderList;


    }

    @Override
    public Order getOrderById(String orderId) throws Exception {
        Order order = orderMap.get(orderId);
        if (order != null) {
            return order;
        }
        return null;
    }

    @Override
    public void addOrder(OrderDTO order) throws Exception {
        // Check if the order already exists
        if (orderMap.containsKey(order.id())) {
            throw new Exception("Order with ID " + order.id() + " already exists.");
        }

        // Create a new Order object from the DTO
        List<Cargo> cargos = cargoRepository.getCargoByOrderId(order.id());
        if (cargos == null) {
            cargos = new ArrayList<>(); // Initialize with an empty list if no cargos found
        }
        Site destination = siteRepository.getSiteById(order.destinationID());
        ShippingZone shippingZone = shippingZoneRepository.getShippingZoneById(order.shippingZoneID()); // Default values, will be set later

        Order newOrder = new Order(
                order.id(),
                destination,
                shippingZone,
                cargos
        );

        // Add the order to the DAO and the in-memory map
        orderDAO.addOrder(order);
        orderMap.put(order.id(), newOrder);

    }

    @Override
    public void deleteOrder(String orderId) throws Exception {
        // Check if the order exists
        if (!orderMap.containsKey(orderId)) {
            throw new Exception("Order with ID " + orderId + " does not exist.");
        }

        // Remove the order from the DAO and the in-memory map
        orderDAO.deleteOrder(orderId);
        orderMap.remove(orderId);

    }

    @Override
    public void connectDao(String url) throws Exception{
        orderDAO = new OrderDAO(url);
        orderMap = new HashMap<>();
        loadOrders(); // Load existing orders from the database
    }
    public void loadOrders() throws SQLException, Exceptions.InputNotFoundException {
        List<OrderDTO> orderDTOs = orderDAO.getOrders();
        for (OrderDTO orderDTO : orderDTOs) {
            List<Cargo> cargos = cargoRepository.getCargoByOrderId(orderDTO.id());
            if( cargos == null) {
                cargos = List.of();
                // If no cargos found, initialize with an empty list
            }
            Site destination = siteRepository.getSiteById(orderDTO.destinationID());
            ShippingZone shippingZone = shippingZoneRepository.getShippingZoneById(orderDTO.shippingZoneID());
            Order order = new Order(
                    orderDTO.id(),
                    destination,
                    shippingZone,
                    cargos
            );
            orderMap.put(orderDTO.id(), order);
        }
    }

    public void markOrderAsShipped(String orderId) throws SQLException, Exceptions.InputNotFoundException {
        // Check if the order exists
        if (!orderMap.containsKey(orderId)) {
            throw new SQLException("Order with ID " + orderId + " does not exist.");
        }

        // Mark the order as shipped in the DAO
        orderDAO.markOrderAsShipped(orderId);
        loadOrders();

    }





}

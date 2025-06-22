package Deliveries.Domain.Repository;

import Deliveries.DAO.DeliveriesDAO;
import Deliveries.DAO.OrderDAO;
import Deliveries.DAO.SiteDAO;
import Deliveries.DTO.DeliveryDTO;
import Deliveries.DTO.OrderDTO;
import Deliveries.Domain.*;
import Deliveries.Domain.Order;



import java.sql.SQLException;
import java.util.*;



public class DeliveriesRepositoryImpl implements DeliveriesRepository {
    private DeliveriesDAO deliveryDAO;
    private SiteDAO siteDAO;
    private OrderDAO orderDAO;
    private static DeliveriesRepositoryImpl instance;
    private static OrderRepositoryImpl orderRepository = OrderRepositoryImpl.getInstance();
    private static SiteRepositoryImpl siteRepository = SiteRepositoryImpl.getInstance();
    private static ShippingZoneRepositoryImpl shippingZoneRepository = ShippingZoneRepositoryImpl.getInstance() ;
    private static TruckRepositoryImpl truckRepository = TruckRepositoryImpl.getInstance();
    private static DriverRepositoryImpl driverRepository = DriverRepositoryImpl.getInstance();
    private static CargoRepositoryImpl cargoRepository = CargoRepositoryImpl.getInstance();
    private Map<String, Delivery> deliveryMap;


    public static DeliveriesRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new DeliveriesRepositoryImpl();
        }
        return instance;
    }


    public void addDelivery(DeliveryDTO delivery) throws Exception {

        deliveryDAO.addDelivery(delivery);
       List<Site> destinations = new ArrayList<>();
       List<Order> ordersToDeliver = new ArrayList<>();
       for (String orderId : delivery.orderIds()) {
           OrderDTO orderDTO = orderDAO.getOrderById(orderId);
           if (orderDTO == null) continue;
            Site destination = siteRepository.getSiteById(orderDTO.destinationID());
            destinations.add(destination);
           Order order = new Order(
                   orderDTO.id(),
                   destination,
                   shippingZoneRepository.getShippingZoneById(orderDTO.shippingZoneID()),
                     cargoRepository.getCargoByOrderId(orderDTO.id())
            );
           ordersToDeliver.add(order);
        }
       Site origin = siteRepository.getSiteById(delivery.originId());
        Status status = Status.valueOf(delivery.status().toUpperCase());
        Truck truck = truckRepository.getTruckById(delivery.truckId());
        Driver driver = driverRepository.getDriverById(delivery.driverId());
        Date deliveryDate  = delivery.deliveryDate();
        Delivery newDelivery = new Delivery(delivery.id(), status, deliveryDate, destinations, truck, driver, ordersToDeliver, origin);

        deliveryMap.put(delivery.id(), newDelivery);
    }

    @Override
    public void removeDelivery(String deliveryId) throws SQLException {
        deliveryDAO.deleteDelivery(deliveryId);
        deliveryMap.remove(deliveryId);

    }

    @Override
    public Delivery getDeliveryById(String deliveryId) throws SQLException {
        Delivery delivery = deliveryMap.get(deliveryId);
        if (delivery != null) {
            return delivery;
        }
        return null;
    }

    @Override
    public List<Delivery> getAllDeliveries() throws SQLException {
        List<DeliveryDTO> deliveryDTOs = deliveryDAO.getDeliveries();
        List<Delivery> deliveries = new ArrayList<>();

        for (DeliveryDTO dto : deliveryDTOs) {
            List<Site> destinations = new ArrayList<>();
            List<Order> ordersToDeliver = new ArrayList<>();

            for (String orderId : dto.orderIds()) {
                try {
                    OrderDTO orderDTO = orderDAO.getOrderById(orderId);
                    if (orderDTO == null) continue;

                    Site destination = siteRepository.getSiteById(orderDTO.destinationID());
                    destinations.add(destination);

                    ShippingZone zone = shippingZoneRepository.getShippingZoneById(orderDTO.shippingZoneID());
                    List<Cargo> cargos = cargoRepository.getCargoByOrderId(orderId);
                    if (cargos == null) cargos = List.of();

                    Order order = new Order(
                            orderDTO.id(),
                            destination,
                            zone,
                            cargos
                    );
                    ordersToDeliver.add(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                Site origin = siteRepository.getSiteById(dto.originId());
                Status status = Status.valueOf(dto.status().toUpperCase());
                Truck truck = truckRepository.getTruckById(dto.truckId());
                Driver driver = driverRepository.getDriverById(dto.driverId());
                Date deliveryDate = dto.deliveryDate();

                Delivery delivery = new Delivery(
                        dto.id(),
                        status,
                        deliveryDate,
                        destinations,
                        truck,
                        driver,
                        ordersToDeliver,
                        origin
                );

                deliveries.add(delivery);
                deliveryMap.put(dto.id(), delivery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deliveries;
    }


    @Override
    public void connectDao(String url) throws Exception {
        deliveryDAO = new DeliveriesDAO(url);
        siteDAO = new SiteDAO(url);
        orderDAO = new OrderDAO(url);
        deliveryMap = new HashMap<>();
        loadDeliveries();
    }

    public void loadDeliveries() throws Exception {
        List<DeliveryDTO> deliveryDTOs = deliveryDAO.getDeliveries();
        for (DeliveryDTO dto : deliveryDTOs) {
            List<Site> destinations = new ArrayList<>();
            List<Order> ordersToDeliver = new ArrayList<>();

            for (String orderId : dto.orderIds()) {
                OrderDTO orderDTO = orderDAO.getOrderById(orderId);
                if (orderDTO == null) continue;

                Site destination = siteRepository.getSiteById(orderDTO.destinationID());
                destinations.add(destination);

                ShippingZone shippingZone = shippingZoneRepository.getShippingZoneById(orderDTO.shippingZoneID());
                List<Cargo> cargos = cargoRepository.getCargoByOrderId(orderId);
                if (cargos == null) cargos = List.of();

                Order order = new Order(
                        orderDTO.id(),
                        destination,
                        shippingZone,
                        cargos
                );
                ordersToDeliver.add(order);
            }

            Site origin = siteRepository.getSiteById(dto.originId());
            Status status = Status.valueOf(dto.status().toUpperCase());
            Truck truck = truckRepository.getTruckById(dto.truckId());
            Driver driver = driverRepository.getDriverById(dto.driverId());
            Date deliveryDate = dto.deliveryDate();

            Delivery delivery = new Delivery(
                    dto.id(),
                    status,
                    deliveryDate,
                    destinations,
                    truck,
                    driver,
                    ordersToDeliver,
                    origin
            );

            deliveryMap.put(dto.id(), delivery);
        }
    }

}

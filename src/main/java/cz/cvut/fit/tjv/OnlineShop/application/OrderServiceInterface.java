package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface OrderServiceInterface {
    Order getOrderById(Long id) throws EntityNotFoundException;

    List<Order> getOrders();

    void executeOrder(Long order_id) throws IllegalArgumentException, EntityNotFoundException;

    Order createOrder(Order order) throws IllegalArgumentException;

    Order updateOrder(Order order) throws IllegalArgumentException, EntityNotFoundException;

    void deleteOrderById(Long order_id) throws EntityNotFoundException;

}

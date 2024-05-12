package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import cz.cvut.fit.tjv.OnlineShop.persistent.AvailabilityRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService implements OrderServiceInterface{
    private OrderRepository orderRepository;
    private AvailabilityRepository availabilityRepository;

    public OrderService(OrderRepository orderRepository, AvailabilityRepository availabilityRepository) {
        this.orderRepository = orderRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public Order getOrderById(Long id) throws EntityNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order with given id: "+ id +" not found!"));
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void executeOrder(Long order_id) throws IllegalArgumentException, EntityNotFoundException {
        if (orderRepository.existsById(order_id)){
            Order order = orderRepository.findById(order_id).get();
            if (!order.isCompleted()){
                if (order.getItems().size() == 0){
                    throw new IllegalArgumentException("Order with given id: "+ order_id +" have no items to execute!");
                }
                for (int i = 0; i < order.getItems().size(); i++){
                    Availability availability = availabilityRepository.findById(order.getItems().get(i).getId()).get();
                    availability.setQuantity(availability.getQuantity() - 1);
                    availabilityRepository.save(availability);
                }
                order.setCompleted(true);
                orderRepository.save(order);
                return;
            }
            throw new IllegalArgumentException("Order with given id: "+ order_id +" already executed!");
        }
        throw new EntityNotFoundException("Order with given id: "+ order_id +" does not exists!");
    }

    @Override
    public Order createOrder(Order order) throws IllegalArgumentException {
        if (order.getId() != null){
            throw new IllegalArgumentException("Entity must have id equals null while creation!");
        }
        for (Availability availability: order.getItems()){
            if (!availabilityRepository.existsById(availability.getId())){
                throw new IllegalArgumentException("Offer: " + availability.getId() + "does not exists!");
            }
        }
        if (order.isCompleted()){
            throw new IllegalArgumentException("Order could ot be completed during creating!");
        }
        if (order.getTime() == null){
            order.setTime(LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) throws IllegalArgumentException, EntityNotFoundException {
        if (orderRepository.existsById(order.getId())){
            for (Availability availability: order.getItems()){
                if (!availabilityRepository.existsById(availability.getId())){
                    throw new IllegalArgumentException("Offer: " + availability.getId() + "does not exists!");
                }
            }
            return orderRepository.save(order);
        }
        throw new EntityNotFoundException("Order with id: " + order.getId() + "does not exists!");
    }

    @Override
    public void deleteOrderById(Long order_id) throws EntityNotFoundException {
        if (orderRepository.existsById(order_id)) {
            orderRepository.deleteById(order_id);
        } else {
            throw new EntityNotFoundException("Order with id: " + order_id + " does not exists!");
        }
    }
}

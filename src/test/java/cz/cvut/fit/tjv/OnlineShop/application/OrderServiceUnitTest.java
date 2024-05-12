package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import cz.cvut.fit.tjv.OnlineShop.persistent.AvailabilityRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class OrderServiceUnitTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private AvailabilityRepository availabilityRepository;
    Order order1;
    Order order2;
    Order order3;
    Availability availability;
    Product product;
    OfflineShop offlineShop;

    @BeforeEach
    void setUp(){
        order1 = new Order();
        order2 = new Order();
        order3 = new Order();
        availability = new Availability();
        product = new Product();
        offlineShop = new OfflineShop();
        List<Availability> presence = new ArrayList<>();
        presence.add(availability);
        product.setId(1l);
        product.setName("banana");
        product.setDescription("good");
        product.setPresence(presence);
        offlineShop.setId(1l);
        offlineShop.setName("firuza");
        offlineShop.setAddress("kmochova4");
        offlineShop.setOffers(presence);
        order1.setId(1l);
        order1.setTime(LocalDateTime.now());
        order1.setCompleted(false);
        List<Order> customs = new ArrayList<Order>();
        customs.add(order1);
        availability.setId(1l);
        availability.setOfflineShop(offlineShop);
        availability.setProduct(product);
        availability.setPrice(10);
        availability.setQuantity(3);
        availability.setCustoms(customs);
        List<Availability> items = new ArrayList<Availability>();
        items.add(availability);
        order1.setItems(items);
        order2.setId(2l);
        order2.setTime(LocalDateTime.now());
        order2.setCompleted(false);
        order2.setItems(new ArrayList<>());
        order3.setId(3l);
        order3.setTime(LocalDateTime.now());
        order3.setCompleted(true);
        order3.setItems(items);
        Mockito.when(orderRepository.existsById(order1.getId())).thenReturn(true);
        Mockito.when(orderRepository.existsById(order2.getId())).thenReturn(true);
        Mockito.when(orderRepository.existsById(order3.getId())).thenReturn(true);
        Mockito.when(orderRepository.findById(order1.getId())).thenReturn(Optional.of(order1));
        Mockito.when(orderRepository.findById(order2.getId())).thenReturn(Optional.of(order2));
        Mockito.when(orderRepository.findById(order3.getId())).thenReturn(Optional.of(order3));
        Mockito.when(availabilityRepository.findById(availability.getId())).thenReturn(Optional.of(availability));
    }

    @Test
    public void executeOrderSuccess(){
        orderService.executeOrder(order1.getId());
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).existsById(order1.getId());
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).findById(order1.getId());
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).save(order1);
        Mockito.verify(availabilityRepository, Mockito.atLeastOnce()).findById(availability.getId());
        Mockito.verify(availabilityRepository, Mockito.atLeastOnce()).save(availability);

    }

    @Test
    public void executeOrderOrderNotFound(){
        Mockito.when(orderRepository.existsById(4l)).thenReturn(false);
        Assert.assertThrows(EntityNotFoundException.class, () -> orderService.executeOrder(4l));
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).existsById(4l);
        Mockito.verify(orderRepository, Mockito.never()).findById(4l);
    }

    @Test
    public void executeOrderOrderIsCompleted(){
        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.executeOrder(order3.getId()));
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).existsById(order3.getId());
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).findById(order3.getId());
        Mockito.verify(orderRepository, Mockito.never()).save(order3);
        Mockito.verify(availabilityRepository, Mockito.never()).findById(availability.getId());
        Mockito.verify(availabilityRepository, Mockito.never()).save(availability);
    }

    @Test
    public void executeOrderOrderHasNoItems(){
        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.executeOrder(order2.getId()));
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).existsById(order2.getId());
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).findById(order2.getId());
        Mockito.verify(orderRepository, Mockito.never()).save(order2);
    }




}

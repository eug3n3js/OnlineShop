package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import cz.cvut.fit.tjv.OnlineShop.persistent.AvailabilityRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.OfflineShopRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.OrderRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AvailabilityIntegrationTest {

    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OfflineShopRepository offlineShopRepository;
    private Order order;
    private Availability availability1;
    private Availability availability2;
    private Product product;
    private OfflineShop offlineShop;

    @BeforeEach
    void setUp(){
        order = new Order();
        product = new Product();
        offlineShop = new OfflineShop();
        availability1 = new Availability();
        availability2 = new Availability();
        List<Availability> presence = new ArrayList<>();
        presence.add(availability1);
        presence.add(availability2);
        product.setName("banana");
        product.setDescription("banana");
        product.setPresence(new ArrayList<>());
        product = productRepository.save(product);
        offlineShop.setName("alza");
        offlineShop.setAddress("kmochova4");
        offlineShop.setOffers(new ArrayList<>());
        offlineShop = offlineShopRepository.save(offlineShop);
        availability1.setProduct(product);
        availability1.setOfflineShop(offlineShop);
        availability1.setQuantity(2);
        availability1.setPrice(10);
        availability1.setCustoms(new ArrayList<>());
        availability1 = availabilityRepository.save(availability1);
        availability2.setProduct(product);
        availability2.setOfflineShop(offlineShop);
        availability2.setQuantity(2);
        availability2.setPrice(10);
        availability2.setCustoms(new ArrayList<>());
        availability2 = availabilityRepository.save(availability2);
        order.setCompleted(false);
        order.setTime(LocalDateTime.now().minusDays(1));
        order.setItems(new ArrayList<>());
        order = orderRepository.save(order);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        availability1.setCustoms(orders);
        availability1 = availabilityRepository.save(availability1);
    }

    @AfterEach
    void deSetUp(){
        Order order_ = orderRepository.findById(order.getId()).get();
        Availability availability1_ = availabilityRepository.findById(availability1.getId()).get();
        if (availability1_.getCustoms().contains(order_)){
            availability1_.getCustoms().remove(order_);
            availabilityRepository.save(availability1_);
        }
        Availability availability2_ = availabilityRepository.findById(availability2.getId()).get();
        if (availability2_.getCustoms().contains(order_)){
            availability2_.getCustoms().remove(order_);
            availabilityRepository.save(availability2_);
        }
        orderRepository.deleteById(order.getId());
        availabilityRepository.deleteById(availability1.getId());
        availabilityRepository.deleteById(availability2.getId());
        offlineShopRepository.deleteById(offlineShop.getId());
        productRepository.deleteById(product.getId());
    }


    @Test
    public void addAvailabilityToOrderTest(){
        availabilityService.addAvailabilityToOrder(order.getId(), availability2.getId());
        Order orderFromDb = orderRepository.findById(order.getId()).get();
        Availability offerFromDb = availabilityRepository.findById(availability2.getId()).get();
        Assertions.assertTrue(orderFromDb.getItems().contains(offerFromDb));
        Assertions.assertEquals(2, orderFromDb.getItems().size());
        Assertions.assertTrue(offerFromDb.getCustoms().contains(orderFromDb));
        Assertions.assertEquals(1, offerFromDb.getCustoms().size());
    }

    @Test
    public void deleteAvailabilityFromOrderTest(){
        availabilityService.deleteAvailabilityFromOrder(order.getId(), availability1.getId());
        Order orderFromDb = orderRepository.findById(order.getId()).get();
        Availability offerFromDb = availabilityRepository.findById(availability1.getId()).get();
        Assertions.assertFalse(orderFromDb.getItems().contains(offerFromDb));
        Assertions.assertEquals(0, orderFromDb.getItems().size());
        Assertions.assertFalse(offerFromDb.getCustoms().contains(orderFromDb));
        Assertions.assertEquals(0, offerFromDb.getCustoms().size());
    }
}

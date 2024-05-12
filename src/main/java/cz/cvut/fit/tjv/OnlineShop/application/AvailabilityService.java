package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import cz.cvut.fit.tjv.OnlineShop.persistent.AvailabilityRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.OfflineShopRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.OrderRepository;
import cz.cvut.fit.tjv.OnlineShop.persistent.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Component
public class AvailabilityService implements AvailabilityServiceInterface{
    private AvailabilityRepository availabilityRepository;
    private OrderRepository orderRepository;
    private OfflineShopRepository offlineShopRepository;
    private ProductRepository productRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository, OrderRepository orderRepository, OfflineShopRepository offlineShopRepository, ProductRepository productRepository) {
        this.availabilityRepository = availabilityRepository;
        this.orderRepository = orderRepository;
        this.offlineShopRepository = offlineShopRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Availability getAvailabilityById(Long id) throws EntityNotFoundException{
        return availabilityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Offer with given id: "+ id +" not found!"));
    }

    @Override
    public List<Availability> getAvailabilities() {
        return availabilityRepository.findAll();
    }

    @Override
    public Availability createAvailability(Availability availability) throws IllegalArgumentException {
        if (availability.getId() != null){
            throw new IllegalArgumentException("Entity must have id equals null while creation!");
        }
        if (!availabilityRepository.existsAvailabilityByProductIdAndOfflineShopId(availability.getProduct().getId(), availability.getOfflineShop().getId())) {
            if (productRepository.existsById(availability.getProduct().getId()) && offlineShopRepository.existsById(availability.getOfflineShop().getId())){
                Product product = productRepository.findById(availability.getProduct().getId()).get();
                OfflineShop offlineShop = offlineShopRepository.findById(availability.getOfflineShop().getId()).get();
                product.getPresence().add(availability);
                offlineShop.getOffers().add(availability);
                productRepository.save(product);
                offlineShopRepository.save(offlineShop);
                return availabilityRepository.save(availability);
            }
            throw new IllegalArgumentException("Product or Offline Shop in this offer does not exists!");
        }
        throw new IllegalArgumentException("Offer with Product id: " + availability.getProduct().getId() + " and Offline Shop id: " + availability.getOfflineShop().getId() + "already exists!");
    }

    @Override
    public Availability updateAvailability(Availability availability) throws IllegalArgumentException, EntityNotFoundException {
        if(availabilityRepository.existsById(availability.getId())){
            if (productRepository.existsById(availability.getProduct().getId()) && offlineShopRepository.existsById(availability.getOfflineShop().getId())){
                Availability oldOffer = availabilityRepository.findById(availability.getId()).get();
                if (oldOffer.getProduct().getId().equals(availability.getProduct().getId()) && oldOffer.getOfflineShop().getId().equals(availability.getOfflineShop().getId())){
                    return availabilityRepository.save(availability);
                }
                throw new IllegalArgumentException("Product or Offline Shop could not be changed!");
            }
            throw new IllegalArgumentException("Product or Offline Shop in this offer does not exists!");
        }
        throw new EntityNotFoundException("Offer with id: " + availability.getId() + " does not exists!");
    }

    @Override
    public void deleteAvailabilityById(Long availability_id) throws EntityNotFoundException {
        if (availabilityRepository.existsById(availability_id)) {
            availabilityRepository.deleteById(availability_id);
        } else {
            throw new EntityNotFoundException("Offer with id: " + availability_id + " does not exists!");
        }
    }

    @Override
    public List<Availability> checkAvailability(Long order_id) throws EntityNotFoundException {
        List<Availability>  unavailableOffers = new ArrayList<>();
        if (orderRepository.existsById(order_id)){
            Order order = orderRepository.findById(order_id).get();
            for (Availability availability: order.getItems()){
                if (availability.getQuantity() == 0){
                     unavailableOffers.add(availability);
                }
            }
            return  unavailableOffers;
        }
        throw new EntityNotFoundException("Order with given id: "+ order_id +" does not exists!");
    }

    @Override
    public void addAvailabilityToOrder(Long order_id, Long availability_id) throws EntityNotFoundException, IllegalArgumentException{
        if (orderRepository.existsById(order_id)){
            if (availabilityRepository.existsById(availability_id)){
                Availability availability = availabilityRepository.findById(availability_id).get();
                Order order = orderRepository.findById(order_id).get();
                if (availability.getCustoms().contains(order)){
                    throw new IllegalArgumentException("Already added relation between Order id: " + order_id + "and Offer id: " + availability_id);
                }
                availability.getCustoms().add(order);
                order.getItems().add(availability);
                availabilityRepository.save(availability);
                orderRepository.save(order);
                return;
            }
            throw new EntityNotFoundException("Offer with id: " + availability_id + " does not exists!");
        }
        throw new EntityNotFoundException("Order with id: " + order_id + " does not exists!");
    }

    @Override
    public void deleteAvailabilityFromOrder(Long order_id, Long availability_id) throws IllegalArgumentException, EntityNotFoundException {
        if (orderRepository.existsById(order_id)){
            if (availabilityRepository.existsById(availability_id)){
                Order order = orderRepository.findById(order_id).get();
                Availability availability = availabilityRepository.findById(availability_id).get();
                if (availability.getCustoms().contains(order)){
                    availability.getCustoms().remove(order);
                    availabilityRepository.save(availability);
                    return;
                }
                throw new IllegalArgumentException("Order: " + order_id + " does not have relation with Offer: " +  availability_id);
            }
            throw new EntityNotFoundException("Offer with id: " + availability_id + " does not exists!");
        }
        throw new EntityNotFoundException("Order with id: " + order_id + " does not exists!");
    }
}

package cz.cvut.fit.tjv.OnlineShop.controller.converter;

import cz.cvut.fit.tjv.OnlineShop.application.AvailabilityServiceInterface;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.OrderDTO;
import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDTOConverter implements DTOConverter<OrderDTO, Order>{
    AvailabilityServiceInterface availabilityService;

    public OrderDTOConverter(AvailabilityServiceInterface availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getTime(),
                order.isCompleted(),
                order.getItems().stream().map(Availability::getId).toList()
        );
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) throws  IllegalArgumentException {
        if (orderDTO.getItemIds() == null){
            throw new IllegalArgumentException("List object can not be null");
        }
        return new Order(
                orderDTO.getId(),
                orderDTO.getTime(),
                orderDTO.isCompleted(),
                orderDTO.getItemIds().stream().map(availabilityService::getAvailabilityById).toList()
        );
    }
}

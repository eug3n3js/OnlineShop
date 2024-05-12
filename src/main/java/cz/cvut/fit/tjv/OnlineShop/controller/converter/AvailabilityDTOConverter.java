package cz.cvut.fit.tjv.OnlineShop.controller.converter;

import cz.cvut.fit.tjv.OnlineShop.application.*;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.AvailabilityDTO;
import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityDTOConverter implements DTOConverter<AvailabilityDTO, Availability>{
    OrderServiceInterface orderService;
    ProductServiceInterface productService;
    OfflineShopServiceInterface offlineShopService;

    public AvailabilityDTOConverter(OrderServiceInterface orderService, ProductServiceInterface productService, OfflineShopServiceInterface offlineShopService) {
        this.orderService = orderService;
        this.productService = productService;
        this.offlineShopService = offlineShopService;
    }

    @Override
    public AvailabilityDTO toDTO(Availability availability) throws  IllegalArgumentException {
        return new AvailabilityDTO(
                availability.getId(),
                availability.getQuantity(),
                availability.getPrice(),
                availability.getProduct().getId(),
                availability.getOfflineShop().getId(),
                availability.getCustoms().stream().map(Order::getId).toList()
        );
    }

    @Override
    public Availability toEntity(AvailabilityDTO availabilityDTO) throws  IllegalArgumentException {
        if (availabilityDTO.getCustomIds() == null){
            throw new IllegalArgumentException("List object can not be null");
        }
        return new Availability(
                availabilityDTO.getId(),
                availabilityDTO.getQuantity(),
                availabilityDTO.getPrice(),
                productService.getProductById(availabilityDTO.getProductId()),
                offlineShopService.getOfflineShopById(availabilityDTO.getOfflineShopId()),
                availabilityDTO.getCustomIds().stream().map(orderService::getOrderById).toList()
        );
    }
}

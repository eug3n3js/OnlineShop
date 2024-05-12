package cz.cvut.fit.tjv.OnlineShop.controller;

import cz.cvut.fit.tjv.OnlineShop.application.AvailabilityServiceInterface;
import cz.cvut.fit.tjv.OnlineShop.application.OfflineShopServiceInterface;
import cz.cvut.fit.tjv.OnlineShop.application.OrderServiceInterface;
import cz.cvut.fit.tjv.OnlineShop.application.ProductServiceInterface;
import cz.cvut.fit.tjv.OnlineShop.controller.converter.DTOConverter;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.AvailabilityDTO;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.OfflineShopDTO;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.OrderDTO;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.ProductDTO;
import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/offer")
public class AvailabilityController {
    private final ProductServiceInterface productService;
    private final OfflineShopServiceInterface offlineShopService;
    private final AvailabilityServiceInterface availabilityService;
    private final OrderServiceInterface orderService;
    private final DTOConverter<ProductDTO, Product> productDTOConverter;
    private final DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter;
    private final DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter;
    private final DTOConverter<OrderDTO, Order> orderDTOConverter;

    public AvailabilityController(ProductServiceInterface productService, OfflineShopServiceInterface offlineShopService, AvailabilityServiceInterface availabilityService, OrderServiceInterface orderService, DTOConverter<ProductDTO, Product> productDTOConverter, DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter, DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter, DTOConverter<OrderDTO, Order> orderDTOConverter) {
        this.productService = productService;
        this.offlineShopService = offlineShopService;
        this.availabilityService = availabilityService;
        this.orderService = orderService;
        this.productDTOConverter = productDTOConverter;
        this.offlineShopDTOConverter = offlineShopDTOConverter;
        this.availabilityDTOConverter = availabilityDTOConverter;
        this.orderDTOConverter = orderDTOConverter;
    }

    @GetMapping
    public List<AvailabilityDTO> getOffers(){
        return availabilityService.getAvailabilities().stream().map(availabilityDTOConverter::toDTO).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found offer"),
            @ApiResponse(responseCode = "422", description = "Offer with given id does not exist")
    })
    @GetMapping(path = "{id}")
    public AvailabilityDTO getOffer(@PathVariable("id") Long id){
        return availabilityDTOConverter.toDTO(availabilityService.getAvailabilityById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer created"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for offer entity")
    })
    @PostMapping
    public AvailabilityDTO createOffer(@RequestBody AvailabilityDTO availabilityDTO){
        return availabilityDTOConverter.toDTO(availabilityService.createAvailability(availabilityDTOConverter.toEntity(availabilityDTO)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer updated"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for offer entity"),
            @ApiResponse(responseCode = "422", description = "Offer with given id does not exist")
    })
    @PutMapping(path = "{id}")
    public AvailabilityDTO updateOffer(@PathVariable("id") Long id, @RequestBody AvailabilityDTO availabilityDTO){
        Availability entity = availabilityDTOConverter.toEntity(availabilityDTO);
        entity.setId(id);
        return availabilityDTOConverter.toDTO(availabilityService.updateAvailability(entity));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer deleted"),
            @ApiResponse(responseCode = "422", description = "Offer with given id does not exist")
    })
    @DeleteMapping(path = "{id}")
    public void deleteOffer(@PathVariable("id") Long id){
        availabilityService.deleteAvailabilityById(id);
    }

    @Operation(summary = "Adds relation between offer instance and order instance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added offer to order"),
            @ApiResponse(responseCode = "400", description = "Relation already exists"),
            @ApiResponse(responseCode = "422", description = "Order or Offer with given id does not exist")
    })
    @PostMapping(path = "{id}/order")
    public List<OrderDTO> addToOrder(@PathVariable("id") Long id, @RequestBody Long order_id){
        availabilityService.addAvailabilityToOrder(order_id, id);
        return availabilityService.getAvailabilityById(id).getCustoms().stream().map(orderDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Deletes relation between offer instance and order instance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted offer from order"),
            @ApiResponse(responseCode = "400", description = "There is no relation to delete"),
            @ApiResponse(responseCode = "422", description = "Order or Offer with given id does not exist")
    })
    @DeleteMapping(path = "{id}/order")
    public List<OrderDTO> deleteFromOrder(@PathVariable("id") Long id, @RequestBody Long order_id){
        availabilityService.deleteAvailabilityFromOrder(order_id, id);
        return availabilityService.getAvailabilityById(id).getCustoms().stream().map(orderDTOConverter::toDTO).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found orders with this offer"),
            @ApiResponse(responseCode = "422", description = "Offer with given id does not exist")
    })
    @GetMapping(path = "{id}/order")
    public List<OrderDTO> getBoundOrders(@PathVariable("id") Long id){
        return availabilityService.getAvailabilityById(id).getCustoms().stream().map(orderDTOConverter::toDTO).toList();
    }
}

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
@RequestMapping("/rest/api/order")
public class OrderController {
    private final ProductServiceInterface productService;
    private final OfflineShopServiceInterface offlineShopService;
    private final AvailabilityServiceInterface availabilityService;
    private final OrderServiceInterface orderService;
    private final DTOConverter<ProductDTO, Product> productDTOConverter;
    private final DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter;
    private final DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter;
    private final DTOConverter<OrderDTO, Order> orderDTOConverter;

    public OrderController(ProductServiceInterface productService, OfflineShopServiceInterface offlineShopService, AvailabilityServiceInterface availabilityService, OrderServiceInterface orderService, DTOConverter<ProductDTO, Product> productDTOConverter, DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter, DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter, DTOConverter<OrderDTO, Order> orderDTOConverter) {
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
    public List<OrderDTO> getOrders(){
        return orderService.getOrders().stream().map(orderDTOConverter::toDTO).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order"),
            @ApiResponse(responseCode = "422", description = "Order with given id does not exist")
    })
    @GetMapping(path = "{id}")
    public OrderDTO getOrder(@PathVariable("id") Long id){
        return orderDTOConverter.toDTO(orderService.getOrderById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for order entity")
    })
    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO){
        return orderDTOConverter.toDTO(orderService.createOrder(orderDTOConverter.toEntity(orderDTO)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for order entity"),
            @ApiResponse(responseCode = "422", description = "Order with given id does not exist")
    })
    @PutMapping(path = "{id}")
    public OrderDTO updateOrder(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO){
        Order entity = orderDTOConverter.toEntity(orderDTO);
        entity.setId(id);
        return orderDTOConverter.toDTO(orderService.updateOrder(entity));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted"),
            @ApiResponse(responseCode = "422", description = "Order with given id does not exist")
    })
    @DeleteMapping(path = "{id}")
    public void deleteOrder(@PathVariable("id") Long id){
        orderService.deleteOrderById(id);
    }

    @Operation(summary = "Adds relation between offer instance and order instance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added offer to order"),
            @ApiResponse(responseCode = "400", description = "Relation already exists"),
            @ApiResponse(responseCode = "422", description = "Order or Offer with given id does not exist")
    })
    @PostMapping(path = "{id}/offer")
    public List<AvailabilityDTO> addAvailabilityToOrder(@PathVariable("id") Long id, @RequestBody Long offer_id){
        availabilityService.addAvailabilityToOrder(id, offer_id);
        return orderService.getOrderById(id).getItems().stream().map(availabilityDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Deletes relation between offer instance and order instance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted offer from order"),
            @ApiResponse(responseCode = "400", description = "There is no relation to delete"),
            @ApiResponse(responseCode = "422", description = "Order or Offer with given id does not exist")
    })
    @DeleteMapping(path = "{id}/offer")
    public List<AvailabilityDTO> deleteAvailabilityFromOrder(@PathVariable("id") Long id, @RequestBody Long offer_id){
        availabilityService.deleteAvailabilityFromOrder(id, offer_id);
        return orderService.getOrderById(id).getItems().stream().map(availabilityDTOConverter::toDTO).toList();
    }

    @Operation(summary = "Fecth offers for specific order. Takes action param. action=all => get all offers for order. action=unavailable => get only offers that have quantity=0")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found items in order"),
            @ApiResponse(responseCode = "422", description = "Order with given id does not exist")
    })
    @GetMapping(path = "{id}/offer")
    public List<AvailabilityDTO> getItems(@PathVariable("id") Long id, @RequestParam("action") String action){
        if (action.equals("all")) {
            return orderService.getOrderById(id).getItems().stream().map(availabilityDTOConverter::toDTO).toList();
        } else if (action.equals("unavailable")){
            return availabilityService.checkAvailability(id).stream().map(availabilityDTOConverter::toDTO).toList();
        } else {
            throw new IllegalArgumentException("Unsupported argument in request!");
        }
    }

    @Operation(summary = "Executes order. Updates quantity for every offer in specific order and updates state of order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order executed"),
            @ApiResponse(responseCode = "400", description = "Order could not be executed"),
            @ApiResponse(responseCode = "422", description = "Order with given id does not exist")
    })
    @PutMapping(path = "{id}/offer")
    public OrderDTO executeOrder(@PathVariable("id") Long id){
        if (availabilityService.checkAvailability(id).isEmpty()) {
            orderService.executeOrder(id);
            return orderDTOConverter.toDTO(orderService.getOrderById(id));
        } else {
            throw new IllegalArgumentException("Order with id: " + id + "could not be completed cause of invalid offers!");
        }
    }
}

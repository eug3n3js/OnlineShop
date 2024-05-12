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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/rest/api/shop")
public class OfflineShopController {
    private final ProductServiceInterface productService;
    private final OfflineShopServiceInterface offlineShopService;
    private final AvailabilityServiceInterface availabilityService;
    private final OrderServiceInterface orderService;
    private final DTOConverter<ProductDTO, Product> productDTOConverter;
    private final DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter;
    private final DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter;
    private final DTOConverter<OrderDTO, Order> orderDTOConverter;

    public OfflineShopController(ProductServiceInterface productService, OfflineShopServiceInterface offlineShopService, AvailabilityServiceInterface availabilityService, OrderServiceInterface orderService, DTOConverter<ProductDTO, Product> productDTOConverter, DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter, DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter, DTOConverter<OrderDTO, Order> orderDTOConverter) {
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
    public List<OfflineShopDTO> getShops(){
        return offlineShopService.getOfflineShops().stream().map(offlineShopDTOConverter::toDTO).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found shop"),
            @ApiResponse(responseCode = "422", description = "Shop with given id does not exist")
    })
    @GetMapping(path = "{id}")
    public OfflineShopDTO getShop(@PathVariable("id") Long id){
        return offlineShopDTOConverter.toDTO(offlineShopService.getOfflineShopById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found offers for shop"),
            @ApiResponse(responseCode = "422", description = "Shop with given id does not exist")
    })
    @GetMapping(path = "{id}/offer")
    public List<AvailabilityDTO> getOffersForShop(@PathVariable("id") Long id){
        return offlineShopService.getOfflineShopById(id).getOffers().stream().sorted(Comparator.comparingInt(Availability::getQuantity).reversed()).map(availabilityDTOConverter::toDTO).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop created"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for shop entity")
    })
    @PostMapping
    public OfflineShopDTO createShop(@RequestBody OfflineShopDTO offlineShopDTO){
        return offlineShopDTOConverter.toDTO(offlineShopService.createOfflineShop(offlineShopDTOConverter.toEntity(offlineShopDTO)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop updated"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for shop entity"),
            @ApiResponse(responseCode = "422", description = "Shop with given id does not exist")
    })
    @PutMapping(path = "{id}")
    public OfflineShopDTO updateShop(@PathVariable("id") Long id, @RequestBody OfflineShopDTO offlineShopDTO){
        OfflineShop entity = offlineShopDTOConverter.toEntity(offlineShopDTO);
        entity.setId(id);
        return offlineShopDTOConverter.toDTO(offlineShopService.updateOfflineShop(entity));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shop deleted"),
            @ApiResponse(responseCode = "422", description = "Shop with given id does not exist")
    })
    @DeleteMapping(path = "{id}")
    public void deleteShop(@PathVariable("id") Long id){
        offlineShopService.deleteOfflineShopById(id);
    }
}

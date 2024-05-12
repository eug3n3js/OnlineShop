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

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/rest/api/product")
public class ProductController {
    private final ProductServiceInterface productService;
    private final OfflineShopServiceInterface offlineShopService;
    private final AvailabilityServiceInterface availabilityService;
    private final OrderServiceInterface orderService;
    private final DTOConverter<ProductDTO, Product> productDTOConverter;
    private final DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter;
    private final DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter;
    private final DTOConverter<OrderDTO, Order> orderDTOConverter;

    public ProductController(ProductServiceInterface productService, OfflineShopServiceInterface offlineShopService, AvailabilityServiceInterface availabilityService, OrderServiceInterface orderService, DTOConverter<ProductDTO, Product> productDTOConverter, DTOConverter<OfflineShopDTO, OfflineShop> offlineShopDTOConverter, DTOConverter<AvailabilityDTO, Availability> availabilityDTOConverter, DTOConverter<OrderDTO, Order> orderDTOConverter) {
        this.productService = productService;
        this.offlineShopService = offlineShopService;
        this.availabilityService = availabilityService;
        this.orderService = orderService;
        this.productDTOConverter = productDTOConverter;
        this.offlineShopDTOConverter = offlineShopDTOConverter;
        this.availabilityDTOConverter = availabilityDTOConverter;
        this.orderDTOConverter = orderDTOConverter;
    }

    @Operation(summary = "Fetch products. Takes action param. action=all => get all products, action=available => get only available products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found products"),
            @ApiResponse(responseCode = "400", description = "Bad request param")
    })
    @GetMapping
    public List<ProductDTO> getProducts(@RequestParam("action") String action){
        if (action.equals("all")) {
            return productService.getProducts().stream().map(productDTOConverter::toDTO).toList();
        } else if (action.equals("available")) {
            return productService.getAvailableProducts().stream().map(productDTOConverter::toDTO).toList();
        } else {
            throw new IllegalArgumentException("Unsupported argument in request!");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found product"),
            @ApiResponse(responseCode = "422", description = "Product with given id does not exist")
    })
    @GetMapping(path = "{id}")
    public ProductDTO getProduct(@PathVariable("id") Long id){
        return productDTOConverter.toDTO(productService.getProductById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found offers for product"),
            @ApiResponse(responseCode = "422", description = "Product with given id does not exist")
    })
    @GetMapping(path = "{id}/offer")
    public List<AvailabilityDTO> getOffersForProduct(@PathVariable("id") Long id){
        return productService.getProductById(id).getPresence().stream().sorted(Comparator.comparingDouble(Availability::getPrice)).map(availabilityDTOConverter::toDTO).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for product entity")
    })
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO){
        return productDTOConverter.toDTO(productService.createProduct(productDTOConverter.toEntity(productDTO)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "400", description = "Bad attributes for product entity"),
            @ApiResponse(responseCode = "422", description = "Product with given id does not exist")
    })
    @PutMapping(path = "{id}")
    public ProductDTO updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO){
        Product entity = productDTOConverter.toEntity(productDTO);
        entity.setId(id);
        return productDTOConverter.toDTO(productService.updateProduct(entity));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "422", description = "Product with given id does not exist")
    })
    @DeleteMapping(path = "{id}")
    public void deleteProduct(@PathVariable("id") Long id){
        productService.deleteProductById(id);
    }
}

package cz.cvut.fit.tjv.OnlineShop.controller.converter;

import cz.cvut.fit.tjv.OnlineShop.application.AvailabilityServiceInterface;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.ProductDTO;
import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOConverter implements DTOConverter<ProductDTO, Product>{
    AvailabilityServiceInterface availabilityService;

    public ProductDTOConverter(AvailabilityServiceInterface availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPresence().stream().map(Availability::getId).toList()
        );
    }

    @Override
    public Product toEntity(ProductDTO productDTO) throws  IllegalArgumentException {
        if (productDTO.getPresenceIds() == null){
            throw new IllegalArgumentException("List object can not be null");
        }
        return new Product(
                productDTO.getId(),
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPresenceIds().stream().map(availabilityService::getAvailabilityById).toList()
        );
    }
}

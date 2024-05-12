package cz.cvut.fit.tjv.OnlineShop.controller.converter;

import cz.cvut.fit.tjv.OnlineShop.application.AvailabilityServiceInterface;
import cz.cvut.fit.tjv.OnlineShop.controller.dto.OfflineShopDTO;
import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import org.springframework.stereotype.Component;

@Component
public class OfflineShopDTOConverter implements DTOConverter<OfflineShopDTO, OfflineShop>{
    AvailabilityServiceInterface availabilityService;

    public OfflineShopDTOConverter(AvailabilityServiceInterface availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public OfflineShopDTO toDTO(OfflineShop offlineShop) {
        return new OfflineShopDTO(
                offlineShop.getId(),
                offlineShop.getName(),
                offlineShop.getAddress(),
                offlineShop.getOffers().stream().map(Availability::getId).toList()
        );
    }

    @Override
    public OfflineShop toEntity(OfflineShopDTO offlineShopDTO) throws  IllegalArgumentException {
        if (offlineShopDTO.getOfferIds() == null){
            throw new IllegalArgumentException("List object can not be null");
        }
        return new OfflineShop(
                offlineShopDTO.getId(),
                offlineShopDTO.getName(),
                offlineShopDTO.getAddress(),
                offlineShopDTO.getOfferIds().stream().map(availabilityService::getAvailabilityById).toList()
        );
    }
}

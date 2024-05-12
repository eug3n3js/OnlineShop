package cz.cvut.fit.tjv.OnlineShop.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AvailabilityDTO {
    private final Long id;
    private final int quantity;
    private final float price;
    private final Long productId;
    private final Long offlineShopId;
    private final List<Long> customIds;
}


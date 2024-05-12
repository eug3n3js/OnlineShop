package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface OfflineShopServiceInterface {
    OfflineShop getOfflineShopById(Long id) throws EntityNotFoundException;

    List<OfflineShop> getOfflineShops();

    OfflineShop createOfflineShop(OfflineShop offlineShop) throws IllegalArgumentException;

    OfflineShop updateOfflineShop(OfflineShop offlineShop) throws IllegalArgumentException, EntityNotFoundException;

    void deleteOfflineShopById(Long offlineShop_id) throws EntityNotFoundException;
}

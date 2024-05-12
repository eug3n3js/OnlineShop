package cz.cvut.fit.tjv.OnlineShop.persistent;

import cz.cvut.fit.tjv.OnlineShop.domain.OfflineShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfflineShopRepository extends JpaRepository<OfflineShop, Long> {
    boolean existsOfflineShopByNameAndAddress(String name, String address);
}

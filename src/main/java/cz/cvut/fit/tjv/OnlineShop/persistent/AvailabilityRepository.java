package cz.cvut.fit.tjv.OnlineShop.persistent;

import cz.cvut.fit.tjv.OnlineShop.domain.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    boolean existsAvailabilityByProductIdAndOfflineShopId(Long product_id, Long offlineShop_id);
}

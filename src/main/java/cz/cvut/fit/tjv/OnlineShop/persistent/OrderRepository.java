package cz.cvut.fit.tjv.OnlineShop.persistent;

import cz.cvut.fit.tjv.OnlineShop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

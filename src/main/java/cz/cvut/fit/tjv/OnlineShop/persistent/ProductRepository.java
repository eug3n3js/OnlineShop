package cz.cvut.fit.tjv.OnlineShop.persistent;

import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsProductByNameAndDescription(String name, String description);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN Availability a ON p = a.product WHERE a.quantity > 0")
    List<Product> findAvailableProducts();
}

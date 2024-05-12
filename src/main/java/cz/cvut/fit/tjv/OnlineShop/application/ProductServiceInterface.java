package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ProductServiceInterface {
    Product getProductById(Long id) throws EntityNotFoundException;

    List<Product> getAvailableProducts();

    List<Product> getProducts();

    Product createProduct(Product product) throws IllegalArgumentException;

    Product updateProduct(Product product) throws IllegalArgumentException,  EntityNotFoundException;

    void deleteProductById(Long product_id) throws EntityNotFoundException;
}

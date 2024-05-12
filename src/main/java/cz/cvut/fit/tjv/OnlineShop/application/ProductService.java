package cz.cvut.fit.tjv.OnlineShop.application;

import cz.cvut.fit.tjv.OnlineShop.domain.Product;
import cz.cvut.fit.tjv.OnlineShop.persistent.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService implements ProductServiceInterface {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(Long id) throws EntityNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with given id: "+ id +" not found!"));
    }

    @Override
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) throws IllegalArgumentException {
        if (product.getId() != null){
            throw new IllegalArgumentException("Entity must have id equals null while creation!");
        }
        if (!productRepository.existsProductByNameAndDescription(product.getName(), product.getDescription())){
            return productRepository.save(product);
        }
        throw new IllegalArgumentException("Product with name: "+ product.getName() + " and description: "+ product.getDescription() +" already exists!");
    }

    @Override
    public Product updateProduct(Product product) throws EntityNotFoundException, IllegalArgumentException {
        if (productRepository.existsById(product.getId())) {
            if (!productRepository.existsProductByNameAndDescription(product.getName(), product.getDescription())) {
                return productRepository.save(product);
            }
            throw new IllegalArgumentException("Updated product with name: " + product.getName() + " and description: " + product.getDescription() + " already exists!");
        }
        throw new EntityNotFoundException("Product with id: " + product.getId() + " does not exists!");
    }

    @Override
    public void deleteProductById(Long product_id) throws EntityNotFoundException {
        if (productRepository.existsById(product_id)) {
            productRepository.deleteById(product_id);
        } else {
            throw new EntityNotFoundException("Product with id: " + product_id + " does not exists!");
        }
    }
}

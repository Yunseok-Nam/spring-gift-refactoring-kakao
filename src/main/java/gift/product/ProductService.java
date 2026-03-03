package gift.product;

import gift.category.Category;
import gift.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product create(ProductRequest request) {
        validateName(request.name());
        var category = categoryRepository.findById(request.categoryId()).orElse(null);
        if (category == null) {
            return null;
        }
        return productRepository.save(request.toEntity(category));
    }

    public Product createFromAdmin(String name, int price, String imageUrl, Long categoryId) {
        var category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다. id=" + categoryId));
        return productRepository.save(new Product(name, price, imageUrl, category));
    }

    public Product update(Long id, ProductRequest request) {
        validateName(request.name());
        var category = categoryRepository.findById(request.categoryId()).orElse(null);
        if (category == null) {
            return null;
        }
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        product.update(request.name(), request.price(), request.imageUrl(), category);
        return productRepository.save(product);
    }

    public Product updateFromAdmin(Long id, String name, int price, String imageUrl, Long categoryId) {
        var product = productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다. id=" + id));
        var category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다. id=" + categoryId));
        product.update(name, price, imageUrl, category);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    private void validateName(String name) {
        var errors = ProductNameValidator.validate(name);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
}

package gift.category;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category create(CategoryRequest request) {
        return categoryRepository.save(request.toEntity());
    }

    public Category update(Long id, CategoryRequest request) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        category.update(request.name(), request.color(), request.imageUrl(), request.description());
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}

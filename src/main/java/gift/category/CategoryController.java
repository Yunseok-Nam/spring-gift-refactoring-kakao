package gift.category;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categories = categoryService.findAll().stream()
            .map(CategoryResponse::from)
            .toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category saved = categoryService.create(request);
        return ResponseEntity.created(URI.create("/api/categories/" + saved.getId()))
            .body(CategoryResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryRequest request
    ) {
        Category category = categoryService.update(id, request);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

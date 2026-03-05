package gift.product;

import gift.category.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "product/new";
    }

    @PostMapping
    public String create(ProductRequest request, Model model) {
        List<String> errors = ProductNameValidator.validate(request.name(), true);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("name", request.name());
            model.addAttribute("price", request.price());
            model.addAttribute("imageUrl", request.imageUrl());
            model.addAttribute("categoryId", request.categoryId());
            model.addAttribute("categories", categoryService.findAll());
            return "product/new";
        }

        productService.create(request);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "product/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, ProductRequest request, Model model) {
        List<String> errors = ProductNameValidator.validate(request.name(), true);
        if (!errors.isEmpty()) {
            Product product = productService.findById(id);
            model.addAttribute("errors", errors);
            model.addAttribute("product", product);
            model.addAttribute("name", request.name());
            model.addAttribute("price", request.price());
            model.addAttribute("imageUrl", request.imageUrl());
            model.addAttribute("categoryId", request.categoryId());
            model.addAttribute("categories", categoryService.findAll());
            return "product/edit";
        }

        productService.update(id, request);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}

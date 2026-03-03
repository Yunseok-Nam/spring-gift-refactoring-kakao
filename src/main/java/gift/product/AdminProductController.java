package gift.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("categories", productService.findAllCategories());
        return "product/new";
    }

    @PostMapping
    public String create(
        @RequestParam String name,
        @RequestParam int price,
        @RequestParam String imageUrl,
        @RequestParam Long categoryId,
        Model model
    ) {
        var errors = ProductNameValidator.validate(name, true);
        if (!errors.isEmpty()) {
            populateNewForm(model, errors, name, price, imageUrl, categoryId);
            return "product/new";
        }

        productService.createFromAdmin(name, price, imageUrl, categoryId);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var product = productService.findById(id);
        if (product == null) {
            throw new java.util.NoSuchElementException("상품이 존재하지 않습니다. id=" + id);
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.findAllCategories());
        return "product/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
        @PathVariable Long id,
        @RequestParam String name,
        @RequestParam int price,
        @RequestParam String imageUrl,
        @RequestParam Long categoryId,
        Model model
    ) {
        var product = productService.findById(id);
        if (product == null) {
            throw new java.util.NoSuchElementException("상품이 존재하지 않습니다. id=" + id);
        }

        var errors = ProductNameValidator.validate(name, true);
        if (!errors.isEmpty()) {
            populateEditForm(model, product, errors, name, price, imageUrl, categoryId);
            return "product/edit";
        }

        productService.updateFromAdmin(id, name, price, imageUrl, categoryId);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    private void populateNewForm(
        Model model,
        List<String> errors,
        String name,
        int price,
        String imageUrl,
        Long categoryId
    ) {
        model.addAttribute("errors", errors);
        model.addAttribute("name", name);
        model.addAttribute("price", price);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", productService.findAllCategories());
    }

    private void populateEditForm(
        Model model,
        Product product,
        List<String> errors,
        String name,
        int price,
        String imageUrl,
        Long categoryId
    ) {
        model.addAttribute("errors", errors);
        model.addAttribute("product", product);
        model.addAttribute("name", name);
        model.addAttribute("price", price);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", productService.findAllCategories());
    }
}

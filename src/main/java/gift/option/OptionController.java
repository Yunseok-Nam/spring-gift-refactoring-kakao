package gift.option;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable Long productId) {
        var options = optionService.findByProductId(productId);
        if (options == null) {
            return ResponseEntity.notFound().build();
        }
        var response = options.stream()
            .map(OptionResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OptionResponse> createOption(
        @PathVariable Long productId,
        @Valid @RequestBody OptionRequest request
    ) {
        var saved = optionService.create(productId, request);
        if (saved == null) {
            return ResponseEntity.notFound().build();
        }
        var location = URI.create("/api/products/" + productId + "/options/" + saved.getId());
        return ResponseEntity.created(location)
            .body(OptionResponse.from(saved));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(
        @PathVariable Long productId,
        @PathVariable Long optionId
    ) {
        var deleted = optionService.delete(productId, optionId);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

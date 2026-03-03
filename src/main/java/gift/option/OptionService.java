package gift.option;

import gift.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> findByProductId(Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }
        return optionRepository.findByProductId(productId);
    }

    public Option create(Long productId, OptionRequest request) {
        validateName(request.name());

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }

        if (optionRepository.existsByProductIdAndName(productId, request.name())) {
            throw new IllegalArgumentException("이미 존재하는 옵션명입니다.");
        }

        return optionRepository.save(new Option(product, request.name(), request.quantity()));
    }

    public Option delete(Long productId, Long optionId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }

        var options = optionRepository.findByProductId(productId);
        if (options.size() <= 1) {
            throw new IllegalArgumentException("옵션이 1개인 상품은 옵션을 삭제할 수 없습니다.");
        }

        var option = optionRepository.findById(optionId).orElse(null);
        if (option == null || !option.getProduct().getId().equals(productId)) {
            return null;
        }

        optionRepository.delete(option);
        return option;
    }

    private void validateName(String name) {
        var errors = OptionNameValidator.validate(name);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
}

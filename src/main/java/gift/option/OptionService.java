package gift.option;

import gift.product.Product;
import gift.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        validateProductExists(productId);
        return optionRepository.findByProductId(productId);
    }

    @Transactional
    public Option create(Long productId, OptionRequest request) {
        validateName(request.name());

        var product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + productId));

        if (optionRepository.existsByProductIdAndName(productId, request.name())) {
            throw new IllegalArgumentException("이미 존재하는 옵션명입니다.");
        }

        return optionRepository.save(new Option(product, request.name(), request.quantity()));
    }

    @Transactional
    public void delete(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + productId));

        if (!product.canDeleteOption()) {
            throw new IllegalArgumentException("옵션이 1개인 상품은 옵션을 삭제할 수 없습니다.");
        }

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다. id=" + optionId));

        if (!option.belongsTo(productId)) {
            throw new IllegalArgumentException("해당 상품의 옵션이 아닙니다.");
        }

        optionRepository.delete(option);
    }

    private void validateProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + productId);
        }
    }

    private void validateName(String name) {
        var errors = OptionNameValidator.validate(name);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }
    }
}

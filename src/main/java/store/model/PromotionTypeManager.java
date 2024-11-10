package store.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.exception.ExceptionMessage;
import store.dto.PromotionTypeInputDto;
import store.exception.ExceptionUtils;
import store.model.product.Product;

public class PromotionTypeManager {
    private final List<PromotionType> promotionTypes = new ArrayList<>();

    public PromotionTypeManager(List<PromotionTypeInputDto> promotionTypesInputDto) {
        checkNullException(promotionTypesInputDto);
        for (PromotionTypeInputDto promotionTypeDto : promotionTypesInputDto) {
            validate(promotionTypeDto);
            promotionTypes.add(new PromotionType(
                    promotionTypeDto.name(),
                    promotionTypeDto.buy(),
                    promotionTypeDto.get(),
                    promotionTypeDto.startDate(),
                    promotionTypeDto.endDate()));
        }
    }

    public Optional<PromotionType> getValidPromotionType(String promotionName) {
        Optional<PromotionType> matchingPromotionType = findMatchingPromotionType(
                promotionName);
        if (!promotionName.isEmpty() && matchingPromotionType.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PROMOTION_NAME);
        }
        return matchingPromotionType;
    }

    public Optional<PromotionType> findMatchingPromotionType(String promotionName) {
        return promotionTypes.stream()
                .filter(promotionType -> promotionType.isNameEqual(promotionName))
                .findFirst();
    }

    public boolean isPromotionTypeMatched(String productName, String promotionName, List<Product> matchingProducts) {
        if (matchingProducts == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
        return matchingProducts.stream()
                .anyMatch(product -> product.isSamePromotionType(productName, promotionName));
    }

    private void checkNullException(List<PromotionTypeInputDto> promotionTypesInputDto) {
        if (promotionTypesInputDto == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
    }

    private void validate(PromotionTypeInputDto promotionTypeInputDto) {
        boolean isDuplicate = promotionTypes.stream().anyMatch(promotionType ->
                promotionType.getName().equals(promotionTypeInputDto.name())
        );
        if (isDuplicate) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.DUPLICATE_PROMOTION_NAME_ERROR);
        }
    }
}

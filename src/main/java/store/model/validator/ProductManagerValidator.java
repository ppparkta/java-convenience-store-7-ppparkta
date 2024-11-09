package store.model.validator;

import java.util.List;
import store.exception.ExceptionMessage;
import store.constant.StoreConfig;
import store.exception.ExceptionUtils;
import store.model.Product;

public class ProductManagerValidator {
    public static void validateProductPrice(int priceInput, List<Product> matchingProducts) {
        if (isPriceMismatch(priceInput, matchingProducts)) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_PRICE);
        }
    }

    public static void validateProductVariety(List<Product> matchingProducts, String promotionName) {
        if (isProductVarietyExceeded(matchingProducts)) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.EXCEEDED_MAX_PRODUCT_PROMOTION_LIMIT);
        }
        if (isMaxPromotionTypesReached(matchingProducts, promotionName)) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.MAX_PROMOTION_TYPES_PER_PRODUCT_EXCEEDED);
        }
    }

    private static boolean isPriceMismatch(int priceInput, List<Product> matchingProducts) {
        return matchingProducts.stream().anyMatch(product -> product.getPrice() != priceInput);
    }

    private static boolean isProductVarietyExceeded(List<Product> matchingProducts) {
        return matchingProducts.size() >= StoreConfig.PRODUCT_MAX_COUNT.getValue();
    }

    private static boolean isMaxPromotionTypesReached(List<Product> matchingProducts, String promotionName) {
        return !promotionName.isEmpty() && matchingProducts.size() == StoreConfig.PROMOTION_TYPE_MAX_COUNT.getValue();
    }
}

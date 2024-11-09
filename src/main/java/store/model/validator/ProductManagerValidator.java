package store.model.validator;

import java.util.List;
import store.constant.ExceptionMessage;
import store.constant.StoreConfig;
import store.exception.ExceptionUtils;
import store.model.Product;

public class ProductManagerValidator {
    public static void validateProductPrice(int priceInput, List<Product> matchingProducts) {
        boolean priceMismatch = matchingProducts.stream()
                .anyMatch(product -> product.getPrice() != priceInput);
        if (priceMismatch) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_PRICE);
        }
    }

    public static void validateProductVariety(List<Product> matchingProducts, String promotionName) {
        if (matchingProducts.size() >= StoreConfig.PRODUCT_MAX_COUNT.getValue()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.EXCEEDED_MAX_PRODUCT_PROMOTION_LIMIT);
        }
        if (matchingProducts.size() == StoreConfig.PROMOTION_TYPE_MAX_COUNT.getValue() && !promotionName.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.MAX_PROMOTION_TYPES_PER_PRODUCT_EXCEEDED);
        }
    }
}

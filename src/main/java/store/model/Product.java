package store.model;

import store.constant.ExceptionMessage;
import store.constant.GeneralConfig;
import store.exception.ExceptionUtils;

public class Product {
    private final String name;
    private final long price;
    private final PromotionType promotionType;

    public Product(String name, long price, PromotionType promotionType) {
        validate(name, price);
        this.name = name;
        this.price = price;
        this.promotionType = promotionType;
    }

    private void validate(String name, long price) {
        if (name == null || name.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.ERROR_EMPTY_INPUT);
        }
        validatePrice(price);
    }

    private void validatePrice(long price) {
        if (price < GeneralConfig.MIN_PRICE.getValue()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_MIN_QUANTITY);
        }
        if (price > Integer.MAX_VALUE) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_INTEGER_RANGE);
        }
    }
}

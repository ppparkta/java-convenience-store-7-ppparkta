package store.model;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(
                promotionType, product.promotionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, promotionType);
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

package store.model.product;

import java.util.Objects;
import store.exception.ExceptionMessage;
import store.constant.StoreConfig;
import store.exception.ExceptionUtils;
import store.model.PromotionType;

public class Product implements Comparable<Product> {
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
        return Objects.equals(price, product.price) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, promotionType);
    }

    @Override
    public int compareTo(Product o) {
        if (this.promotionType != null && o.promotionType == null) {
            return 1;
        }
        if (this.promotionType == null && o.promotionType != null) {
            return -1;
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public boolean isSamePromotionType(String productName, String promotionName) {
        if (!this.name.equals(productName)) {
            return false;
        }
        if (this.promotionType == null && promotionName.isEmpty()) {
            return true;
        }
        if (promotionName.equals(this.promotionType.getName())) {
            return true;
        }
        return false;
    }

    private void validate(String name, long price) {
        if (name == null || name.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_EMPTY_INPUT);
        }
        validatePrice(price);
    }

    private void validatePrice(long price) {
        if (price < StoreConfig.PRODUCT_MIN_PRICE.getValue()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_MIN_QUANTITY);
        }
        if (price > Integer.MAX_VALUE) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_INTEGER_RANGE);
        }
    }
}
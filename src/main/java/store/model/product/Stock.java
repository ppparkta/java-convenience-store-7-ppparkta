package store.model.product;

import java.util.Objects;
import store.exception.ExceptionMessage;
import store.constant.StoreConfig;
import store.exception.ExceptionUtils;

public class Stock implements Comparable<Stock> {
    private final Product product;
    private int quantity;

    public Stock(Product product, int quantity) {
        validate(product, quantity);
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isNameEqual(String productName) {
        return product.getName().equals(productName);
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void reduceQuantity(int quantity) {
        if (this.quantity < quantity) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_ITEM_QUANTITY);
        }
        this.quantity -= quantity;
    }

    @Override
    public int compareTo(Stock o) {
        if (this.product.getPromotionType() == null && o.product.getPromotionType() != null) {
            return 1;
        } else if (this.product.getPromotionType() != null && o.product.getPromotionType() == null) {
            return -1;
        }
        return 0;
    }

    private void validate(Product product, int quantity) {
        if (product == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
        if (quantity < StoreConfig.PRODUCT_MIN_QUANTITY.getValue()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_MIN_QUANTITY);
        }
    }
}

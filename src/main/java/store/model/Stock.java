package store.model;

import store.exception.ExceptionMessage;
import store.constant.StoreConfig;
import store.exception.ExceptionUtils;

public class Stock {
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

    private void validate(Product product, int quantity) {
        if (product == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
        if (quantity < StoreConfig.PRODUCT_MIN_QUANTITY.getValue()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_MIN_QUANTITY);
        }
    }
}

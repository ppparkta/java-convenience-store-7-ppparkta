package store.model;

import store.constant.ExceptionMessage;
import store.constant.GeneralConfig;
import store.exception.ExceptionUtils;

public class Stock {
    private final Product product;
    private final int quantity;

    public Stock(Product product, int quantity) {
        validate(product, quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private void validate(Product product, int quantity) {
        if (product == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
        if (quantity < GeneralConfig.PRODUCT_MIN_QUANTITY.getValue()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_MIN_QUANTITY);
        }
    }
}

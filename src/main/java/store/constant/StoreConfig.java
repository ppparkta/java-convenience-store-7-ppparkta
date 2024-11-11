package store.constant;

import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;

public enum StoreConfig {
    PROMOTION_MIN_QUANTITY(1),
    PROMOTION_TYPE_MAX_COUNT(1),
    PRODUCT_MIN_PRICE(1),
    PRODUCT_MAX_COUNT(2),
    PRODUCT_MIN_QUANTITY(0),
    BASIC_MEMBERSHIP(30),
    BASIC_MEMBERSHIP_LIMIT(8000),
    ;
    private final int value;

    static {
        if (BASIC_MEMBERSHIP.getValue() == 0) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.ZERO_VALUE_ERROR);
        }
    }

    StoreConfig(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getValueToString() {
        return String.valueOf(value);
    }
}

package store.constant;

public enum StoreConfig {
    PROMOTION_MIN_QUANTITY(1),
    PROMOTION_TYPE_MAX_COUNT(1),
    PRODUCT_MAX_COUNT(2),
    PRODUCT_MIN_PRICE(1),
    PRODUCT_MIN_QUANTITY(0),
    ;
    private final int value;

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

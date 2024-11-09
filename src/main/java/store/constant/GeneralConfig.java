package store.constant;

public enum GeneralConfig {
    PROMOTION_MIN_QUANTITY(1),
    PRODUCT_MIN_PRICE(1),
    PRODUCT_MIN_QUANTITY(0),
    ;
    private final int value;

    GeneralConfig(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getValueToString() {
        return String.valueOf(value);
    }
}

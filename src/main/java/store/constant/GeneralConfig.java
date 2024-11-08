package store.constant;

public enum GeneralConfig {
    PROMOTION_MIN_QUANTITY(1),
    MIN_PRICE(1),
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

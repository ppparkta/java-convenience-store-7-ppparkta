package store.constant;

public enum FileConfig {
    FILE_HEADER(0),
    PROMOTION_HEADER_SIZE(5),
    PRODUCT_HEADER_SIZE(4),
    ;

    private final int value;

    FileConfig(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

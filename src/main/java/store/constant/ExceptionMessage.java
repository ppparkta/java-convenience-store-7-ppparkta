package store.constant;

public enum ExceptionMessage {
    INVALID_EMPTY_INPUT("빈 문자열은 들어올 수 없습니다."),
    INVALID_NUMBER_FORMAT("숫자 형식이 잘못되었습니다."),
    INVALID_MIN_QUANTITY(""),
    INVALID_MIN_PRICE(""),
    INVALID_INTEGER_RANGE("값이 int 범위를 초과했습니다."),
    INVALID_PROMOTION_END_DATE("지난 날짜의 프로모션은 생성할 수 없습니다."),
    INVALID_PROMOTION_START_DATE("프로모션 시작일은 프로모션 종료일보다 빨라야 합니다."),
    NULL_VALUE_ERROR("null 값은 들어올 수 없습니다."),
    INVALID_PRODUCT_MIN_QUANTITY(""),
    DUPLICATE_PROMOTION_NAME_ERROR("프로모션 이름은 중복될 수 없습니다."),
    ;

    static {
        INVALID_MIN_QUANTITY.message = GeneralConfig.PROMOTION_MIN_QUANTITY.getValueToString() + " 이상 입력해주세요.";
        INVALID_MIN_PRICE.message = GeneralConfig.PRODUCT_MIN_PRICE.getValueToString() + " 이상 입력해주세요.";
        INVALID_PRODUCT_MIN_QUANTITY.message = GeneralConfig.PRODUCT_MIN_QUANTITY.getValueToString() + " 이상 입력해주세요.";
    }

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR]" + message;
    }
}

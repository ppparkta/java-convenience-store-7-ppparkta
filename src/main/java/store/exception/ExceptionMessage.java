package store.exception;

import store.constant.StoreConfig;

public enum ExceptionMessage {
    FOUND_NOT_FILE("파일을 찾을 수 없습니다."),
    FILE_READ_ERROR("파일 읽기에 실패했습니다."),
    FILE_FORMAT_ERROR("파일 형식이 잘못되었습니다."),
    EMPTY_FILE_ERROR("파일 형식이 잘못되었습니다."),

    INVALID_EMPTY_INPUT("빈 문자열은 들어올 수 없습니다."),
    NULL_VALUE_ERROR("null 값은 들어올 수 없습니다."),
    INVALID_NUMBER_FORMAT("숫자 형식이 잘못되었습니다."),
    INVALID_INTEGER_RANGE("값이 int 범위를 초과했습니다."),

    INVALID_PRODUCT_MIN_QUANTITY(StoreConfig.PRODUCT_MIN_QUANTITY.getValueToString() + " 이상 입력해주세요."),
    INVALID_MIN_PRICE(StoreConfig.PRODUCT_MIN_PRICE.getValueToString() + " 이상 입력해주세요."),
    INVALID_PRODUCT_PRICE("기존 상품과 같은 가격으로만 등록하실 수 있습니다."),

    INVALID_MIN_QUANTITY(StoreConfig.PROMOTION_MIN_QUANTITY.getValueToString() + " 이상 입력해주세요."),
    INVALID_PROMOTION_END_DATE("지난 날짜의 프로모션은 생성할 수 없습니다."),
    INVALID_PROMOTION_START_DATE("프로모션 시작일은 프로모션 종료일보다 빨라야 합니다."),
    DUPLICATE_PROMOTION_NAME_ERROR("프로모션 이름은 중복될 수 없습니다."),
    INVALID_PROMOTION_NAME("일치하는 프로모션이 없습니다."),
    EXCEEDED_MAX_PRODUCT_PROMOTION_LIMIT("같은 상품은 최대 " + StoreConfig.PRODUCT_MAX_COUNT + "개 등록할 수 있습니다."),
    MAX_PROMOTION_TYPES_PER_PRODUCT_EXCEEDED(
            "같은 상품의 프로모션은 최대 " + StoreConfig.PROMOTION_TYPE_MAX_COUNT + "개 등록할 수 있습니다."),

    INVALID_ORDER_ITEM_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_ORDER_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    ;

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR]" + message;
    }
}

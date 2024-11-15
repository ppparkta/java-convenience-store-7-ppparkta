package store.model.product;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import store.exception.ExceptionMessage;
import store.constant.StoreConfig;
import store.exception.ExceptionUtils;

public class PromotionType {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public PromotionType(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        validate(name, buy, get, startDate, endDate);
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isNameEqual(String name) {
        return this.name.equals(name);
    }

    private void validate(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        if (name == null || name.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_EMPTY_INPUT);
        }
        validateQuantity(buy);
        validateQuantity(get);
        validateStartDate(startDate, endDate);
        validateEndDate(endDate);
    }

    private void validateEndDate(LocalDate endDate) {
        if (endDate == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
        if (endDate.isBefore(DateTimes.now().toLocalDate())) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PROMOTION_END_DATE);
        }
    }

    private void validateStartDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
        if (startDate.isAfter(endDate)) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PROMOTION_START_DATE);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < StoreConfig.PROMOTION_MIN_QUANTITY.getValue()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_MIN_QUANTITY);
        }
        if (quantity > Integer.MAX_VALUE) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_INTEGER_RANGE);
        }
    }
}

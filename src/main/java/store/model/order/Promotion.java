package store.model.order;

import java.time.LocalDate;
import store.model.product.PromotionType;

public class Promotion {
    private final PromotionType promotionType;

    public Promotion(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public boolean validatePromotion(LocalDate orderDate) {
        if (promotionType == null) {
            return false;
        }
        if (!promotionType.getStartDate().isAfter(orderDate) && !promotionType.getEndDate().isBefore(orderDate)) {
            return true;
        }
        return false;
    }

    public boolean isExactPromotionMatch(int allQuantity, int orderQuantity) {
        return false;
    }
}

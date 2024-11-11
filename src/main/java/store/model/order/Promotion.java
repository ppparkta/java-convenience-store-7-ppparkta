package store.model.order;

import java.time.LocalDate;
import java.util.List;
import store.model.product.ProductManager;
import store.model.product.PromotionType;

public class Promotion {
    private final ProductManager productManager;
    private final PromotionType promotionType;
    private final List<OrderItem> applicableOrderItems;
    private int totalBonusQuantity;
    private int remainingQuantity;
    private int additionalReceivable;
    private int benefitQuantity;
    private boolean canReceiveMorePromotion;

    public Promotion(ProductManager productManager, List<OrderItem> applicableOrderItems, LocalDate orderDate) {
        this.productManager = productManager;
        this.promotionType = findPromotionType(applicableOrderItems);
        this.applicableOrderItems = applicableOrderItems;
        if (isPromotionValid(orderDate)) {
            applyPromotion();
        }
    }

    public int getTotalBonusQuantity() {
        return totalBonusQuantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public int getAdditionalReceivable() {
        return additionalReceivable;
    }

    public int getBenefitQuantity() {
        return benefitQuantity;
    }

    public boolean isCanReceiveMorePromotion() {
        return canReceiveMorePromotion;
    }

    private PromotionType findPromotionType(List<OrderItem> applicableOrderItems) {
        return applicableOrderItems.stream()
                .filter(orderItem -> orderItem.getPromotionType().isPresent())
                .map(orderItem -> orderItem.getPromotionType().get())
                .findFirst()
                .orElse(null);
    }

    private boolean isPromotionValid(LocalDate orderDate) {
        return promotionType != null &&
                !orderDate.isBefore(promotionType.getStartDate()) &&
                !orderDate.isAfter(promotionType.getEndDate());
    }

    private void applyPromotion() {
        int promotionProductQuantity = getPromotionProductQuantity();
        int promotionUnit = promotionType.getBuy() + promotionType.getGet();

        totalBonusQuantity = calculateTotalPromotionQuantity(promotionUnit, promotionProductQuantity);
        remainingQuantity = calculateRemainingQuantity(totalBonusQuantity);
        benefitQuantity = totalBonusQuantity / promotionUnit;

        if (remainingQuantity > 0) {
            calculateAdditionalReceivable(remainingQuantity);
        }
        canReceiveMorePromotion = canReceivePromotion(remainingQuantity);
    }

    private void calculateAdditionalReceivable(int remainingQuantity) {
        if (remainingQuantity >= promotionType.getBuy()) {
            additionalReceivable = promotionType.getGet() - (remainingQuantity - promotionType.getBuy());
            if (additionalReceivable < 0) {
                additionalReceivable = 0;
            }
            return;
        }
        additionalReceivable = 0;
    }

    private int calculateRemainingQuantity(int totalBonusQuantity) {
        return Math.max(0, getTotalOrderQuantity() - totalBonusQuantity);
    }

    private boolean canReceivePromotion(int remainingQuantity) {
        int availableStockForPromotion = productManager.getPromotionProductQuantity(
                applicableOrderItems.get(0).getProductName());
        return remainingQuantity >= promotionType.getBuy() && availableStockForPromotion >= promotionType.getGet();
    }

    private int getTotalOrderQuantity() {
        return applicableOrderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    private int getPromotionProductQuantity() {
        return applicableOrderItems.stream()
                .filter(orderItem -> orderItem.getPromotionType().isPresent())
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    private int calculateTotalPromotionQuantity(int promotionUnit, int promotionProductQuantity) {
        int promotionCount = promotionProductQuantity / promotionUnit;
        return promotionCount * promotionUnit;
    }
}

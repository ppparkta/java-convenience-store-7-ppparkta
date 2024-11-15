package store.dto.response;

public record PromotionResultDto(String productName, int totalBonusQuantity, int remainingQuantity,
                                 int getAdditionalReceivable, int benefitQuantity, boolean canReceiveMorePromotion) {
}

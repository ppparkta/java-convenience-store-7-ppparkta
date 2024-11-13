package store.dto.response;

import java.util.List;

public record ReceiptResultDto(List<OrderItemResultDto> orderItems,
                               List<PromotionBenefitResultDto> promotionBenefits, long totalPurchaseAmount,
                               long totalPromotionDiscount, double membershipDiscount, int finalAmount) {
}

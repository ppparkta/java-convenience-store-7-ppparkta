package store.model.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.constant.StoreConfig;
import store.dto.response.PromotionResultDto;

public class Membership {
    public static double calculateMembershipDiscount(Order order, List<PromotionResultDto> promotionResult) {
        Map<String, List<OrderItem>> groupedOrderItems = groupOrderItemsByProductName(order);
        long totalAmountWithoutPromotion = calculateTotalAmountWithoutPromotion(groupedOrderItems, promotionResult);
        long totalPromotionAmount = calculateTotalPromotionAmount(order, promotionResult);

        double discountRate = StoreConfig.BASIC_MEMBERSHIP.getValue() / 100.0;
        double discountAmount = (totalAmountWithoutPromotion + totalPromotionAmount) * discountRate;

        return Math.min(discountAmount, StoreConfig.BASIC_MEMBERSHIP_LIMIT.getValue());
    }

    private static Map<String, List<OrderItem>> groupOrderItemsByProductName(Order order) {
        return order.getOrderItems().stream()
                .collect(Collectors.groupingBy(OrderItem::getProductName));
    }

    private static long calculateTotalAmountWithoutPromotion(Map<String, List<OrderItem>> groupedOrderItems,
                                                             List<PromotionResultDto> promotionResult) {
        return groupedOrderItems.entrySet().stream()
                .filter(entry -> !isPromotionProduct(entry.getKey(), promotionResult))
                .flatMap(entry -> entry.getValue().stream())
                .mapToLong(orderItem -> (long) orderItem.getQuantity() * orderItem.getProduct().getPrice())
                .sum();
    }

    private static boolean isPromotionProduct(String productName, List<PromotionResultDto> promotionResult) {
        return promotionResult.stream()
                .anyMatch(promo -> promo.productName().equals(productName));
    }

    private static int calculateTotalPromotionAmount(Order order, List<PromotionResultDto> promotionResult) {
        return promotionResult.stream()
                .filter(promo -> promo.remainingQuantity() > 0)
                .mapToInt(promo -> calculateAmountForRemainingQuantity(order, promo))
                .sum();
    }

    private static int calculateAmountForRemainingQuantity(Order order, PromotionResultDto promotionResultDto) {
        return (int) order.findOrderItemByProductName(promotionResultDto.productName()).stream()
                .mapToLong(orderItem -> {
                    int applicableQuantity = Math.min(orderItem.getQuantity(), promotionResultDto.remainingQuantity());
                    return (long) applicableQuantity * orderItem.getProduct().getPrice();
                })
                .sum();
    }
}
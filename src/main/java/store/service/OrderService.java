package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.request.OrderItemInputDto;
import store.dto.response.PromotionResultDto;
import store.model.order.Order;
import store.model.order.OrderItem;
import store.model.order.Promotion;
import store.model.product.ProductManager;

public class OrderService {
    private final ProductManager productManager;

    public OrderService(ProductManager productManager) {
        this.productManager = productManager;
    }

    public Order createOrder(List<OrderItemInputDto> orderItemInputsDto) {
        return new Order(productManager, orderItemInputsDto, DateTimes.now().toLocalDate());
    }

    public List<PromotionResultDto> processPromotions(Order order) {
        Map<String, List<OrderItem>> groupedOrderItems = order.getOrderItems().stream()
                .collect(Collectors.groupingBy(OrderItem::getProductName));
        return createPromotionResultDto(groupedOrderItems);
    }

    private List<PromotionResultDto> createPromotionResultDto(Map<String, List<OrderItem>> groupedOrderItems) {
        return groupedOrderItems.values().stream()
                .map(orderItems -> {
                    Promotion promotion = new Promotion(productManager, orderItems, DateTimes.now().toLocalDate());
                    return new PromotionResultDto(
                            orderItems.get(0).getProductName(),
                            promotion.getTotalBonusQuantity(),
                            promotion.getRemainingQuantity(),
                            promotion.getAdditionalReceivable(),
                            promotion.isCanReceiveMorePromotion()
                    );
                })
                .toList();
    }
}


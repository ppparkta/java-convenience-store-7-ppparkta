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
import store.model.product.Stock;

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

    public void processAdditionalPromotion(PromotionResultDto promotionResultDto, Order order) {
        List<OrderItem> promotionOrderItems = order.findOrderItemByProductName(promotionResultDto.productName())
                .stream()
                .filter(orderItem -> orderItem.getPromotionType().isPresent()).toList();
        if (!promotionOrderItems.isEmpty()) {
            promotionOrderItems.getFirst().addQuantity(promotionResultDto.getAdditionalReceivable());
            List<Stock> stocksByProductName = productManager.findStocksByProductName(
                    promotionResultDto.productName());
            stocksByProductName.getFirst().reduceQuantity(promotionResultDto.getAdditionalReceivable());
        }
    }

    public void processRemoveOrderItem(PromotionResultDto promotionResultDto, Order order) {
        List<OrderItem> orderItems = order.findOrderItemByProductName(promotionResultDto.productName());

        for (OrderItem orderItem : orderItems) {
            int removedQuantity = removeOrderItem(order, orderItem);
            updateStock(promotionResultDto, order, orderItem, removedQuantity);
        }
    }

    private static int removeOrderItem(Order order, OrderItem orderItem) {
        int removedQuantity = orderItem.getQuantity();
        order.removeOrderItem(orderItem);
        return removedQuantity;
    }

    private void updateStock(PromotionResultDto promotionResultDto, Order order, OrderItem orderItem,
                             int removedQuantity) {
        Stock matchingStock = productManager.findStocksByProductName(promotionResultDto.productName()).stream()
                .filter(stock -> stock.getProduct().equals(orderItem.getProduct()))
                .findFirst()
                .orElse(null);

        if (matchingStock != null && removedQuantity > 0) {
            matchingStock.addQuantity(removedQuantity);
        }
    }

    private List<PromotionResultDto> createPromotionResultDto(Map<String, List<OrderItem>> groupedOrderItems) {
        return groupedOrderItems.values().stream()
                .map(this::mapOrderItemsToDto)
                .toList();
    }

    private PromotionResultDto mapOrderItemsToDto(List<OrderItem> orderItems) {
        Promotion promotion = new Promotion(productManager, orderItems, DateTimes.now().toLocalDate());
        return createDtoFromPromotion(orderItems, promotion);
    }

    private PromotionResultDto createDtoFromPromotion(List<OrderItem> orderItems, Promotion promotion) {
        String productName = orderItems.get(0).getProductName();
        return new PromotionResultDto(
                productName,
                promotion.getTotalBonusQuantity(),
                promotion.getRemainingQuantity(),
                promotion.getAdditionalReceivable(),
                promotion.isCanReceiveMorePromotion()
        );
    }
}


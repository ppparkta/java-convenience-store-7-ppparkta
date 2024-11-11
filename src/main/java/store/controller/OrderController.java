package store.controller;

import java.util.List;
import store.dto.request.OrderItemInputDto;
import store.dto.response.PromotionResultDto;
import store.model.order.Order;
import store.model.product.ProductManager;
import store.service.OrderService;
import store.view.OutputView;

public class OrderController {
    private final ProductManager productManager;
    private final OutputView outputView;
    private final ConsoleInputHandler inputHandler;
    private final OrderService orderService;

    public OrderController(ProductManager productManager) {
        this.productManager = productManager;
        this.outputView = new OutputView();
        this.inputHandler = new ConsoleInputHandler();
        this.orderService = new OrderService(this.productManager);
    }

    public void processOrder() {
        while (true) {
            try {
                if (inputAndProcessOrder()) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean inputAndProcessOrder() {
        List<OrderItemInputDto> orderItemsDto = getOrderItemsInputDto();
        Order order = orderService.createOrder(orderItemsDto);
        List<PromotionResultDto> promotionResults = handlePromotion(order);
        handleMembership(order, promotionResults);
        outputView.printReceipt();
        if (!shouldContinueOrder()) {
            return true;
        }
        return false;
    }

    private List<PromotionResultDto> handlePromotion(Order order) {
        List<PromotionResultDto> promotionResults = orderService.processPromotions(order);
        handlePromotionResults(promotionResults, order);
        return orderService.processPromotions(order);
    }

    private void handleMembership(Order order, List<PromotionResultDto> promotionResultDtos) {
    }

    private void handlePromotionResults(List<PromotionResultDto> promotionResults, Order order) {
        for (PromotionResultDto promotionResultDto : promotionResults) {
            if (promotionResultDto.canReceiveMorePromotion()) {
                handleAdditionalPromotion(promotionResultDto, order);
            }
            if (!promotionResultDto.canReceiveMorePromotion() && promotionResultDto.remainingQuantity() > 0) {
                handleNoPromotion(promotionResultDto, order);
            }
        }
    }

    private void handleAdditionalPromotion(PromotionResultDto promotionResultDto, Order order) {
        String receiveMorePromotion = inputHandler.getReceiveMorePromotion(promotionResultDto);
        if ("Y".equals(receiveMorePromotion)) {
            orderService.processAdditionalPromotion(promotionResultDto, order);
        }
    }

    private void handleNoPromotion(PromotionResultDto promotionResultDto, Order order) {
        String noPromotion = inputHandler.getNoPromotion(promotionResultDto);
        if ("N".equals(noPromotion)) {
            orderService.processRemoveOrderItem(promotionResultDto, order);
        }
    }

    private boolean shouldContinueOrder() {
        return !"N".equals(inputHandler.getContinueOrder());
    }

    private List<OrderItemInputDto> getOrderItemsInputDto() {
        outputView.printWelcomeMessage();
        outputView.printStocks(productManager.getStocks());
        return inputHandler.getOrderItems();
    }
}

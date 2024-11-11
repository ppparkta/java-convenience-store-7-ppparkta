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
                List<OrderItemInputDto> orderItemsDto = getOrderItemsInputDto();
                Order order = orderService.createOrder(orderItemsDto);
                List<PromotionResultDto> promotionResults = orderService.processPromotions(order);
                for (PromotionResultDto promotionResultDto : promotionResults) {
                    if (promotionResultDto.canReceiveMorePromotion()) {
                        String receiveMorePromotion = inputHandler.getReceiveMorePromotion(promotionResultDto);
                        if (receiveMorePromotion.equals("Y")) {
                            // 무료 수량 추가하기
                        }
                    }
                    if (!promotionResultDto.canReceiveMorePromotion() && promotionResultDto.remainingQuantity() > 0) {
                        String noPromotion = inputHandler.getNoPromotion(promotionResultDto);
                        if (noPromotion.equals("N")) {
                            // 현재 상품 삭제하기
                        }
                    }
                }
                // 영수증 출력하기
                // 반복할지 물어보기
                if (inputHandler.getContinueOrder().equals("N")) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<OrderItemInputDto> getOrderItemsInputDto() {
        outputView.printWelcomeMessage();
        outputView.printStocks(productManager.getStocks());
        return inputHandler.getOrderItems();
    }
}

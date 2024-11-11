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

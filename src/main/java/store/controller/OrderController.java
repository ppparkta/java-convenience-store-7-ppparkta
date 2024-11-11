package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import store.dto.OrderItemInputDto;
import store.model.order.Order;
import store.model.order.Promotion;
import store.model.product.ProductManager;
import store.view.OutputView;

public class OrderController {
    private final ProductManager productManager;
    private final OutputView outputView;
    private final ConsoleInputHandler inputHandler;

    public OrderController(ProductManager productManager) {
        this.productManager = productManager;
        this.outputView = new OutputView();
        this.inputHandler = new ConsoleInputHandler();
    }

    public void processOrder() {
        while (true) {
            try {
                List<OrderItemInputDto> orderItemsDto = getOrderItemsInputDto();
                Order order = new Order(productManager, orderItemsDto, DateTimes.now().toLocalDate());
                while (true) {
                    Promotion
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

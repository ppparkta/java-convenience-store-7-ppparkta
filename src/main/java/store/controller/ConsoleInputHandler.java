package store.controller;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.dto.OrderItemInputDto;
import store.parser.OrderParser;
import store.view.ConsoleInputView;

public class ConsoleInputHandler {
    private final ConsoleInputView inputView;
    private final OrderParser orderParser;

    public ConsoleInputHandler() {
        this.inputView = new ConsoleInputView();
        this.orderParser = new OrderParser();
    }

    public List<OrderItemInputDto> getOrderItems() {
        String orderItemsInput = inputView.getUserInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return orderParser.parseOrderItems(orderItemsInput);
    }
}
